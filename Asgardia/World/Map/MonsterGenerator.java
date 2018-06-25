package Asgardia.World.Map;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

import Asgardia.Config.*;
import Asgardia.Types.*;
import Asgardia.Server.*;
import Asgardia.Server.Utility.*;
import Asgardia.World.*;
import Asgardia.World.Objects.Items.*;
import Asgardia.World.Objects.Monster.MonsterInstance;
import Asgardia.World.Objects.Template.*;

public class MonsterGenerator extends Thread implements Runnable
{
	AsgardiaMap Map;
	
	static Random random = new Random (System.currentTimeMillis () ) ;
	
	/* <K, V> = <spawnlist主鍵, 產生參數> */
	ConcurrentHashMap<Integer, MonsterSpawnList> SpawnList = null;
	
	/* <K, V> = <怪物編號, 物品掉落清單> */
	ConcurrentHashMap<Integer, List<MonsterDropList>> DropList = null;
	
	public void run () {
		try {
			SpawnList.forEach ((Integer u, MonsterSpawnList msl)->{
				//while (msl.Mobs.size () < msl.Count) {
				//if (msl.Mobs.size () < msl.Count) {
				if (msl.Mobs.size () < 1) {
					NpcTemplate Temp = CacheData.NpcCache.get (msl.NpcTemplateId) ;
					Location SpawnLoc = new Location (msl.MapId, msl.LocX, msl.LocY, msl.Heading) ;
					
					/*
					 * 製作怪物實體
					 */
					MonsterInstance Mob = new MonsterInstance (Temp, SpawnLoc) ;
					Mob.MovementDistance = msl.MovementDistance;
					Mob.Uuid = UuidGenerator.Next () ;
					
					//System.out.printf ("要求產生[%s]%d隻在地圖%d", msl.Location, (msl.Count-msl.Mobs.size()), msl.MapId) ;
					//System.out.printf (":0x%08X\n", Mob.Uuid) ;
					
					/*
					 * 產生怪物持有道具
					 */
					List<MonsterDropList> mdl = DropList.get (msl.NpcTemplateId) ;
					if (mdl != null) {
						for (MonsterDropList i : mdl) {		
							
							int DropRate = random.nextInt (1000000) * Configurations.RateDropItem;
							if (DropRate < i.Posibility) {
								ItemInstance s = new ItemInstance (
									0, //init uuid
									i.ItemId,
									0, //owner char uuid
									i.Min + random.nextInt (i.Max),
									0, //enchant
									0, //dura
									100, //charge
									false, //is_equi
									false) ; //is_identi
							
								/* 金幣倍率修正 */
								if (s.ItemId == 40308) {
									s.Count *= Configurations.RateDropGold;
								}
								
								Mob.Items.put (s.ItemId, s) ;
							}
						}
					}
					
					Map.addMonster (Mob) ;
					msl.Mobs.add (Mob) ;
					Mob.ActionStatus = 1; /* 生怪後的初始狀態 */
				}
			});
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	public MonsterGenerator (AsgardiaMap map) {
		Map = map;
		SpawnList = new ConcurrentHashMap<Integer, MonsterSpawnList> () ;
		DropList  = new ConcurrentHashMap<Integer, List<MonsterDropList>> () ;
		UpdateSpawnList () ;
	}
	
	public void removeMonster (MonsterInstance m) {
		SpawnList.forEach ((Integer u, MonsterSpawnList msl)->{
			msl.Mobs.remove (m) ;
		});
	}
	
	public void UpdateSpawnList () {
		ResultSet rs = DatabaseCmds.MobSpawnlist (Map.MapId) ;
		try {
			while (rs.next () ) {
				MonsterSpawnList msl = new MonsterSpawnList (
					rs.getInt ("id"),
					rs.getString ("location"),
					rs.getInt ("count"),
					rs.getInt ("npc_templateid"),
					rs.getInt ("group_id"),
					rs.getInt ("locx"),
					rs.getInt ("locy"),
					rs.getInt ("randomx"), 
					rs.getInt ("randomy"),
					rs.getInt ("mapid"), 
					rs.getInt ("locx1"), 
					rs.getInt ("locy1"), 
					rs.getInt ("locx2"), 
					rs.getInt ("locy2"), 
					rs.getInt ("heading"), 
					rs.getInt ("min_respawn_delay"),
					rs.getInt ("max_respawn_delay"),
					rs.getInt ("respawn_screen"),
					rs.getInt ("movement_distance"),
					rs.getInt ("rest"),
					rs.getInt ("near_spawn") 
				) ; //End of new MonsterSpawnList
				
				if (msl.MovementDistance < 1) {
					msl.MovementDistance = Configurations.DEFAULT_MOVEMENT_RANGE;
				}
				
				/*
				 * 確認生怪座標有在地圖範圍內
				 */	
				int[] MapSizeInfo = MapInfo.getInstance ().Table.get (msl.MapId) ;
				if ((msl.LocX < MapSizeInfo[0]) ||
					(msl.LocX > MapSizeInfo[1]) ||
					(msl.LocY < MapSizeInfo[2]) ||
					(msl.LocY > MapSizeInfo[3])) {
					
					System.out.printf ("MSL ERR, CHECK->id:%d name:%s location(%d:%d,%d)\n",
						msl.ListId,
						CacheData.NpcCache.get (msl.NpcTemplateId).Name,
						msl.MapId, msl.LocX, msl.LocY) ;
					continue;
					
				} 
				
				SpawnList.put (msl.ListId, msl) ;
				
				int MonsterId = rs.getInt ("npc_templateid") ;
				ResultSet rs_drop = DatabaseCmds.MobDroplist (MonsterId) ;
				rs_drop.last () ;
				int DropListSize = rs_drop.getRow () ;
				
				if (DropListSize > 0 && !DropList.containsKey (MonsterId) ) {
					rs_drop.first () ;
					List<MonsterDropList> DropTable= new ArrayList<MonsterDropList> () ;
					
					while (rs_drop.next () ) {
						MonsterDropList mdl = new MonsterDropList (
							rs_drop.getInt ("mobId"),
							rs_drop.getInt ("itemId"),
							rs_drop.getInt ("min"),
							rs_drop.getInt ("max"),
							rs_drop.getInt ("chance") ) ;
						DropTable.add (mdl) ;
					}
					DropList.putIfAbsent (MonsterId, DropTable) ;
					
				}
			} //End of rs.next()
			
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	/*
	 * 快取資料庫的spawnlist用
	 */
	class MonsterSpawnList {
		public int ListId;
		public String Location;
		public int Count;
		public int NpcTemplateId;
		public int GroupId;
		public int LocX, LocY;
		public int RandomX;
		public int RandomY;
		public int MapId;
		public int LocX1, LocY1;
		public int LocX2, LocY2;
		public int Heading;
		public int MinRespawnDelay;
		public int MaxRespawnDelay;
		public int RespawnScreen;
		public int MovementDistance;
		public int Rest;
		public int NearSpawn;
		
		public List<MonsterInstance> Mobs;
		
		public MonsterSpawnList (
			int list_id,
			String location,
			int count,
			int npc_template_id,
			int group_id,
			int locx,
			int locy,
			int random_x,
			int random_y,
			int map_id,
			int locx1, int locy1,
			int locx2, int locy2,
			int heading,
			int min_respawn_delay,
			int max_respawn_delay,
			int respawn_screen,
			int movement_distance,
			int rest,
			int near_spawn
		) {
			ListId = list_id;
			Location = location;
			Count = count;
			NpcTemplateId = npc_template_id;
			GroupId = group_id;
			LocX = locx; LocY = locy;
			RandomX = random_x;
			RandomY = random_y;
			MapId = map_id;
			LocX1 = locx1; LocY1 = locy1;
			LocX2 = locx2; LocY2 = locy2;
			Heading = heading;
			MinRespawnDelay = min_respawn_delay;
			MaxRespawnDelay = max_respawn_delay;
			RespawnScreen = respawn_screen;
			MovementDistance = movement_distance;
			Rest = rest;
			NearSpawn = near_spawn;
			
			Mobs = new ArrayList<MonsterInstance> () ;
		}
	}
	
	class MonsterDropList {
		public int MobId;
		public int ItemId;
		public int Min, Max;
		public int Posibility;
		
		public MonsterDropList (
			int mob_id,
			int item_id,
			int min,
			int max,
			int posibility
		) {
			MobId = mob_id;
			ItemId = item_id;
			Min = min;
			Max = max;
			Posibility = posibility;
		}
	}
}
