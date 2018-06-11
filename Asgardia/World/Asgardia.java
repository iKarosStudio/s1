package Asgardia.World;

import java.lang.Thread;
import java.util.*;
import java.util.concurrent.*;

import Asgardia.Config.*;
import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.Server.Utility.*;
import Asgardia.World.Map.*;
import Asgardia.World.Npc.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Monster.*;

public class Asgardia extends Thread
{
	private static Asgardia instance;
	
	private static ConcurrentHashMap<Integer, AsgardiaMap> Maps;
	
	public MemoryMonitor MemMonitor = null;
	public BoardcastMessage SysMessage = null;
	
	public long UsedMemory = 0;
	
	/* 全局等級狀態 */
	public static ServerTime Time = null; 
	private static int OnlinePcs = 0;
	
	/* 
	 * Bit[1-0] : 程度控制
	 * 0:不下
	 * 1:小
	 * 2:中
	 * 3:大
	 * 
	 * Bit[8] :
	 * 0:下雪
	 * 1:下雨
	 *  */
	public int Weather = 0x13 ;
	
	public void run () {
		/*
		 * 暫時沒有被使用, 預定做世界級監控調整
		 */
	}
	
	public static Asgardia getInstance () {
		if (instance == null) {
			instance = new Asgardia () ;
		}
		return instance;
	}
	
	public Asgardia () {
		System.out.println ("Creating Asgardia world instance " + this) ;
		Maps = new ConcurrentHashMap<Integer, AsgardiaMap> () ;
	}
	
	public void Initialize () {
		try {
			/*
			 * Cache items, equipment data, npc types...
			 */
			CacheData.getInstance () ;
			
			/*
			 * Fetch Server last UUID
			 */
			UuidGenerator.getInstance () ;
			
			/*
			 * Load maps
			 */
			MonsterAiQueue.getInstance () ;
			MonsterAiExecutor.getInstance () ;
			new MapLoader (instance) ;
			
			/*
			 * Load npc
			 */
			new NpcLoader (instance) ;
			
			System.out.println () ;
			
			/* 
			 * Load Door
			 */
			DoorGenerator.getInstance () ;
			
			/*
			 * Generate monster
			 */
			System.out.printf ("Generate Monster, Generator interval:%.1f Sec...", (float)Configurations.MONSTER_GENERATOR_UPDATE_RATE/1000) ;
			Maps.forEach ((Integer map_id, AsgardiaMap map)->{
				map.MobGenerator = new MonsterGenerator (map) ;
				KernelThreadPool.getInstance ().ScheduleAtFixedRate (map.MobGenerator, 1000, Configurations.MONSTER_GENERATOR_UPDATE_RATE) ;
			}) ;
			System.out.printf ("success\n") ;
			
			/*
			 * Monster AI controller
			 */
			
			/*
			 * Start server time
			 */
			Time = ServerTime.getInstance () ;
			KernelThreadPool.getInstance ().ScheduleAtFixedRate (Time, 0, 1000) ;
			
			//load boss
			//load special system
			
			/*
			 * Server memory usage monitor
			 */
			MemMonitor = MemoryMonitor.getInstance () ;
			KernelThreadPool.getInstance ().ScheduleAtFixedRate (MemMonitor, 0, 5000) ;
			
			/*
			 * Game boardcast message
			 */
			SysMessage = BoardcastMessage.getInstance () ;
			KernelThreadPool.getInstance ().ScheduleAtFixedRate (SysMessage, 10000, 30000) ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	public void UpdateWeather () {
		BoardcastToAll (new ReportWeather (Weather).getRaw () ) ;
		System.out.printf ("Update Weather:0x%02x\n", Weather) ;
	}
	
	public void BoardcastToAll (byte[] Data) {
		List<PcInstance> AllPcs = getAllPc () ;
		
		if (AllPcs.size () > 0) {
			for (PcInstance pc : AllPcs) {
				SessionHandler Handle = pc.getHandler () ;
				Handle.SendPacket (Data) ;
			}
		}
		
		AllPcs = null;
	}
	
	public List<PcInstance> getAllPc () {
		List<PcInstance> AllPcs = new ArrayList<PcInstance> () ;
		
		try {
			Maps.forEach ((Integer id, AsgardiaMap map)->{
				List<PcInstance> pcs = map.getAllPcs () ;
				if (pcs.size () > 0) {
					for (PcInstance p : pcs) {
						AllPcs.add (p) ;
					}
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace () ;
		}
		
		return AllPcs;
	}
	
	public PcInstance getPcByUuid (int uuid) {
		return null;
	}
	
	public List<PcInstance> getPcsByClanId (int clan_id) {
		return null;
	}
	
	public List<PcInstance> getPcsByParty (int party_id) {
		return null;
	}

	public synchronized void addPc (PcInstance Pc) {
		try {
			AsgardiaMap m = Maps.get (Pc.location.MapId) ;
			m.addPc (Pc) ;
			Pc.UpdateOnlineStatus (true) ;
			OnlinePcs ++;
			System.out.printf ("角色:%s(UUID:0x%08X) 進入世界\n", Pc.Name, Pc.Uuid) ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	public synchronized void removePc (PcInstance Pc) {
		try {
			AsgardiaMap m = Maps.get (Pc.location.MapId) ;
			m.removePc (Pc) ;
			Pc.UpdateOnlineStatus (false) ;
			OnlinePcs --;
			System.out.printf ("角色:%s(UUID:0x%08X) 離開世界\n", Pc.Name, Pc.Uuid) ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	public void addNpc (NpcInstance Npc) {
		try {
			AsgardiaMap map = Maps.get(Npc.location.MapId) ;
			map.addNpc (Npc) ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	public void removeNpc (NpcInstance Npc) {
		try {
			AsgardiaMap m = Maps.get (Npc.location.MapId) ;
			m.removeNpc (Npc) ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	public void addMap (AsgardiaMap map) {
		try {
			Maps.put (map.MapId, map) ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	public AsgardiaMap getMap (int id) {
		return Maps.get (id) ;
	}
	
	public int getOnlinePlayersAmount () {
		int amount = 0;
		
		List<PcInstance> pcs = getAllPc () ;
		amount = pcs.size () ;
		pcs = null;
		
		return amount ;
	}
	
	public void kickOnlinePlayers () {
		try {
			Maps.forEach ((Integer id, AsgardiaMap map)->{
				List<PcInstance> pcs = map.getAllPcs () ;
				if (pcs.size () > 0) {
					for (PcInstance p : pcs) {
						p.Offline () ;
					}
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
}
