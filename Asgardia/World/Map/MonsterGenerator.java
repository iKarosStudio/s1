package Asgardia.World.Map;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

import Asgardia.Server.*;

public class MonsterGenerator extends Thread implements Runnable
{
	AsgardiaMap Map;
	ConcurrentHashMap<Integer, MonsterSpawnList> SpawnList = null;
	ConcurrentHashMap<Integer, MonsterDropList[]> DropList = null;
	
	public void run () {
		//System.out.printf ("monster generator updating -> map:%d\n", Map.MapId) ;
	}
	
	
	public MonsterGenerator (AsgardiaMap map) {
		Map = map;
		SpawnList = new ConcurrentHashMap<Integer, MonsterSpawnList> () ;
		DropList  = new ConcurrentHashMap<Integer, MonsterDropList[]> () ;
		UpdateSpawnList () ;
	}
	
	public void UpdateSpawnList () {
		ResultSet rs = DatabaseCmds.MobSpawnlist (Map.MapId) ;
		try {
			int count = 0;
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
				
				SpawnList.put (msl.ListId, msl) ;
				
				int MonsterId = rs.getInt ("npc_templateid") ;
				ResultSet rs_drop = DatabaseCmds.MobDroplist (MonsterId) ;
				rs_drop.last () ;
				System.out.printf ("map:%d,", Map.MapId) ;
				System.out.printf ("%s : %d\n", rs.getString("location"), rs_drop.getRow () ) ;
				
				while (rs_drop.next () ) {
					//DropList.putIfAbsent ()
				}
				
				/*
				MonsterDropList mdl = new MonsterDropList (
					
				) ;*/
				
				count++;
			}
			System.out.printf ("load %d spawn list in map:%d\n", count, Map.MapId) ;
		} catch (Exception e) {e.printStackTrace () ;}
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
		
		//
		
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
