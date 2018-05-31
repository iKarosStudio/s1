package Asgardia.World.Map;

import java.util.ArrayList;
import java.util.*;
import java.util.concurrent.*;


import Asgardia.Config.*;
import Asgardia.Types.*;
import Asgardia.Server.*;
import Asgardia.World.*;
import Asgardia.World.Npc.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Items.*;

/*
 * 讀取的地圖實例
 * 
 * BIT[7] 1:不可通過 0:可通過 (GENERAL)
 * 
 * BIT[6] //
 * 
 * BIT[5]
 * BIT[4] 0:Normal Zone 1:Safe Zone 2:Combat Zone
 * 
 * BIT[3] 1:Arraw passble
 * BIT[2]
 * BIT[1]
 * BIT[0] 
 * 
 */
public class AsgardiaMap
{
	private byte[][] Tile;
	public  final int MapId;
	private final int StartX;
	private final int EndX;
	private final int StartY;
	private final int EndY;
	
	public final int SizeX;
	public final int SizeY;
	
	public MonsterGenerator MobGenerator = null;
	
	public ConcurrentHashMap<Integer, Location> TpLocation; //傳送點列表
	
	private ConcurrentHashMap<Integer, PcInstance> Pcs; //Online player's table
	private ConcurrentHashMap<Integer, NpcInstance> Npcs; //Npcs' table in this map
	private ConcurrentHashMap<Integer, ItemInstance> GndItems; //Items on ground
	private ConcurrentHashMap<Integer, DoorInstance> Doors; //Doors
	private ConcurrentHashMap<Integer, MonsterInstance> Monsters;
	//private ConcurrentHashMap<Integer, PetInstance> Pets;
	
	public AsgardiaMap (int id, int start_x, int end_x, int start_y, int end_y) {
		MapId = id;
		StartX = start_x;
		EndX = end_x;
		StartY = start_y;
		EndY = end_y;
		
		SizeX = EndX - StartX + 1;
		SizeY = EndY - StartY + 1;
		Tile = new byte[SizeX][SizeY];
		for (int x = 0; x < SizeX; x++) {
			for (int y = 0; y < SizeY; y++) {
				Tile[x][y] = 0;
			}
		}
		
		TpLocation = new ConcurrentHashMap<Integer, Location> () ;
		
		Pcs = new ConcurrentHashMap<Integer, PcInstance> () ;
		Npcs = new ConcurrentHashMap<Integer, NpcInstance> () ;
		GndItems = new ConcurrentHashMap<Integer, ItemInstance> () ;
		Doors = new ConcurrentHashMap<Integer, DoorInstance> () ;
		Monsters = new ConcurrentHashMap<Integer, MonsterInstance> () ;
		
		//MobAi = new MonsterAiController (this) ;
		//KernelThreadPool.getInstance ().ScheduleAtFixedRate (MobAi, 1000, Configurations.MONSTER_AI_UPDATE_RATE) ;
		
	}
	
	public int SpawnMonster () {
		return 0;
	}
	
	public void setAccessible (int x, int y, boolean passable) {
		if (passable) {
			Tile[x - StartX][y - StartY] &= 0x7F;
		} else {
			Tile[x - StartX][y - StartY] |= 0x80;
		}
	}
	
	/*
	 * 檢查p(x, y)->heading p'(x', y')是否可通過
	 */
	public boolean isNextTileAccessible (int x, int y, int heading) {
		byte Next = getHeadingTile (x, y, heading) ;
		
		/*
		 * 檢查動態物件佔有
		 */
		if ((Next & 0x80) > 0) {
			return false;
		}
		
		if ((Next & 0x03) > 0) {	
			/*
			 * 檢查方向單位是否可通行
			 */
			if (heading == 0) {
				return isYAxisAccessible (Next) ;
			} else if (heading == 2) { //
				return isXAxisAccessible (Next) ;
			} else if (heading == 4) {
				return isYAxisAccessible (Next) ;
			} else if (heading == 6) {
				return isXAxisAccessible (Next) ;
				
				//
			} else if (heading == 1) {
				byte[] side = getHeadingSideTile (x, y, heading) ;
				return isYAxisAccessible (side[0]) || isXAxisAccessible (side[1]) ;
				
			} else if (heading == 3) { //
				byte[] side = getHeadingSideTile (x, y, heading) ;
				return isYAxisAccessible (side[0]) || isXAxisAccessible (side[1]) ;
	
			} else if (heading == 5) {
				byte[] side = getHeadingSideTile (x, y, heading) ;
				return isYAxisAccessible (side[0]) || isXAxisAccessible (side[1]) ;
	
			} else if (heading == 7) {
				byte[] side = getHeadingSideTile (x, y, heading) ;
				return isYAxisAccessible (side[0]) || isXAxisAccessible (side[1]) ;
	
			} else {
				//return false;
			}
		}
		return false;
	}
	
	public boolean isNormalZone (int x, int y) {
		return ((Tile[x - StartX][y - StartY] & 0x30) == 0x00) ;
	}
	
	public boolean isSafeZone (int x, int y) {
		return ((Tile[x - StartX][y - StartY] & 0x30) == 0x10) ;
	}
	 
	public boolean isCombatZone (int x, int y) {
		return ((Tile[x - StartX][y - StartY] & 0x30) == 0x20) ;
	}
	
	public void setTile (int x, int y, byte tile) {
		Tile[x][y] = tile;
	}
	
	public byte getTile (int x, int y) {
		return Tile[x - StartX][y - StartY];
	}
	
	public byte getHeadingTile (int x, int y, int heading) {
		if (heading == 0) {
			return getTile (x, y-1) ;
		} else if (heading == 1) {
			return getTile (x+1, y-1) ;
		} else if (heading == 2) {
			return getTile (x+1, y) ;
		} else if (heading == 3) {
			return getTile (x+1, y+1) ;
		} else if (heading == 4) {
			return getTile (x, y+1) ;
		} else if (heading == 5) {
			return getTile (x-1, y+1) ;
		} else if (heading == 6) {
			return getTile (x-1, y) ;
		} else if (heading == 7) {
			return getTile (x-1, y-1) ;
		} else {
			return 0;
		}
	}
	
	public byte[] getHeadingSideTile (int x, int y, int heading) {
		byte[] res = new byte[2];
		
		if (heading == 0) {
			res[0] = getHeadingTile (x, y, 7) ;
			res[1] = getHeadingTile (x, y, 1) ;
		} else if (heading == 1) {
			res[0] = getHeadingTile (x, y, 0) ;
			res[1] = getHeadingTile (x, y, 2) ;
		} else if (heading == 2) {
			res[0] = getHeadingTile (x, y, 1) ;
			res[1] = getHeadingTile (x, y, 3) ;
		} else if (heading == 3) {
			res[0] = getHeadingTile (x, y, 2) ;
			res[1] = getHeadingTile (x, y, 4) ;
		} else if (heading == 4) {
			res[0] = getHeadingTile (x, y, 3) ;
			res[1] = getHeadingTile (x, y, 5) ;
		} else if (heading == 5) {
			res[0] = getHeadingTile (x, y, 4) ;
			res[1] = getHeadingTile (x, y, 6) ;
		} else if (heading == 6) {
			res[0] = getHeadingTile (x, y, 5) ;
			res[1] = getHeadingTile (x, y, 7) ;
		} else if (heading == 7) {
			res[0] = getHeadingTile (x, y, 6) ;
			res[1] = getHeadingTile (x, y, 0) ;
		} else {
			res[0] = 0;
			res[1] = 0;
		}
		
		return res;
	}
	
	public boolean isXAxisAccessible (byte t) {
		return (t & 0x01) > 0;
	}
	
	public boolean isYAxisAccessible (byte t) {
		return (t & 0x02) > 0;
	}
	
	public void addPc (PcInstance pc) {
		Pcs.putIfAbsent (pc.Uuid, pc) ;
	}
	
	public void removePc (PcInstance pc) {
		System.out.printf ("地圖[%d] 移除 %s\n", MapId, pc.Name) ;
		Pcs.remove (pc.Uuid) ;
	}
	
	public void addNpc (NpcInstance Npc) {
		Npcs.put (Npc.Uuid, Npc) ;
	}
	
	public void removeNpc (NpcInstance Npc) {
		Npcs.remove (Npc.Uuid) ;
	}
	
	public void addGndItem (ItemInstance i) {
		GndItems.put (i.Uuid, i) ;
	}
	
	public void removeGndItem (ItemInstance i) {
		GndItems.remove (i.Uuid) ;
	}
	
	public void addDoor (DoorInstance d) {
		Doors.put (d.Uuid, d) ;
	}
	
	public void removeDoor (DoorInstance d) {
		Doors.remove (d.Uuid) ;
	}
	
	/*
	 * 未來考慮取得視線內MonsterInstance物件改由MonsterSpawnList內登記的Mobs取得所有怪物物件的參考
	 */
	public void addMonster (MonsterInstance m) {
		Monsters.put (m.Uuid, m) ;
	}
	
	public long getMonsterAmount () {
		return Monsters.mappingCount () ;
	}
	
	public void removeMonster (MonsterInstance m) {
		Monsters.remove (m.Uuid) ;
	}
	
	public boolean isInTpLocation (int x, int y) {
		int pos = (x << 16) | y;
		
		return TpLocation.containsKey (pos)	;	
	}
	
	public Location getTpDestination (int src_x, int src_y) {
		int src = (src_x << 16) | src_y;
		return TpLocation.get (src) ;
	}
	
	public void addTpLocation (int src_x, int src_y, Location dest) {
		int src = (src_x << 16) | src_y;
		TpLocation.put (src, dest) ;
	}
	
	public Object getObject (int Uuid) {
		Object obj = null;
		
		if (Pcs.containsKey (obj) ) {
			obj = Pcs.get (obj) ;
		} else if (Npcs.containsKey (obj) ) {
			obj = Npcs.get (obj) ;
		} else if (GndItems.containsKey (obj) ) {
			obj = GndItems.get (obj) ;
		}
		
		return obj;
	}
	
	/*
	 * 回報p(x, y)距離range內的Pc物件
	 */
	public List<PcInstance> getPcInDistance (int x, int y, int range) {
		ArrayList<PcInstance> Results = new ArrayList<PcInstance> () ;
		
		try {
			Pcs.forEach ((Integer uuid, PcInstance pc)->{
				int distance = pc.getDistance (x, y) ;
				if (distance < range) {
					Results.add (pc) ;
				}
			}) ;
			
		} catch (Exception e) {
			System.out.printf ("%s : %s\n", this.toString (), e.toString () ) ;
		} 
		return Results;
	}
	
	/*
	 * 回報p(x, y)視距內的PC物件
	 */
	public List<PcInstance> getPcInstance (int x, int y) {
		ArrayList<PcInstance> Results = new ArrayList<PcInstance> () ;
		
		try {
			Pcs.forEach ((Integer uuid, PcInstance pc)->{
				int distance = pc.getDistance (x, y) ;
				if (distance < Configurations.SIGHT_RAGNE) {
					Results.add (pc) ;
				}
			}) ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
		
		return Results;
	}
	
	/*
	 * 回報p(x, y)視距內NPC物件
	 */
	public List<NpcInstance> getNpcInstance (int x, int y) {
		ArrayList<NpcInstance> Results = new ArrayList<NpcInstance> () ;
		
		try {
			Npcs.forEach ((Integer u, NpcInstance node) -> {
				int distance = node.getDistance (x, y) ;
				if (distance < Configurations.SIGHT_RAGNE) {
					Results.add (node) ;
				}
			}) ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
		
		return Results;
	}
	
	/*
	 * 回報p(x, y)視距內地面道具
	 */
	public List<ItemInstance> getGndItemInstance (int x, int y) {
		ArrayList<ItemInstance> Results = new ArrayList<ItemInstance> () ;
		
		try {
			GndItems.forEach ((Integer u, ItemInstance i)->{
				int distance = i.getDistance (x, y) ;
				if (distance < Configurations.SIGHT_RAGNE) {
					Results.add (i) ;
				}
			}) ;
		} catch (Exception e) {e.printStackTrace () ; }
		
		return Results;
	}
	
	/*
	 * 回報p(x, y)視距內門物件
	 */
	public List<DoorInstance> getDoorInstance (int x, int y) {
		ArrayList<DoorInstance> Results = new ArrayList<DoorInstance> () ;
		
		try {
			Doors.forEach ((Integer u, DoorInstance d)->{
				int distance = d.getDistance (x, y) ;
				if (distance < Configurations.SIGHT_RAGNE) {
					Results.add (d) ;
				}
			});
		} catch (Exception e) {e.printStackTrace () ;}
		
		return Results;
	}
	
	/*
	 * 回報p(x, y)視距內AOE物件
	 */
	
	public ItemInstance getGndItemInstanceByUuid (int uuid) {
		if (GndItems.containsKey (uuid) ) {
			return GndItems.get (uuid) ;
		}
		return null;
	}
	
	/*
	 * 回報p(x, y)視距內怪物
	 */
	public List<MonsterInstance> getMonsterInstance (int x, int y) {
		ArrayList<MonsterInstance> Results = new ArrayList<MonsterInstance> () ;
		
		try {
			Monsters.forEach ((Integer u, MonsterInstance m)->{
				int distance = m.getDistance (x, y) ;
				if (distance < Configurations.SIGHT_RAGNE) {
					Results.add (m) ;
				}
			}) ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
		
		return Results;
	}
	
	public List<MonsterInstance> getAllMonster () {
		ArrayList<MonsterInstance> Results = new ArrayList<MonsterInstance> () ;
		try {
			Monsters.forEach ((Integer u, MonsterInstance m)->{
				Results.add (m) ;
			}) ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
		
		return Results;
	}
	
	public List<PcInstance> getAllPcs () {
		List<PcInstance> Results = new ArrayList<PcInstance> () ;

		try {
			if (Pcs.size () > 0) {
				Pcs.forEach ((Integer uuid, PcInstance pc)->{
					Results.add (pc) ;
				}) ;
			} 
		} catch (Exception e) {	e.printStackTrace () ; }
		
		return Results;
	}
	
	public PcInstance getPc (int uuid) {
		return Pcs.get (uuid) ;
	}
	
	public int getPcAmount () {
		return Pcs.size () ;
	}
}
