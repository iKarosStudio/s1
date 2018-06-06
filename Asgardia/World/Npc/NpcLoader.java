package Asgardia.World.Npc;

import java.sql.*;
import java.util.*;

import Asgardia.Server.*;
import Asgardia.World.*;
import Asgardia.World.Npc.*;
import Asgardia.World.Objects.Template.NpcTemplate;
import Asgardia.World.Map.*;

public class NpcLoader
{
	private static HikariCP Db;
	
	public NpcLoader (Asgardia Asg) {
		Db = HikariCP.getInstance () ;
		int ValidCount = 0;

		System.out.printf ("Load npc datas...") ;
		long t_starts = System.currentTimeMillis () ;
		
		/* 載入NPC產生清單 by Map */
		List<String> ErrorList = new ArrayList <String> () ;
		for (int[] Info : MapInfo.INFO) {
			if (Info[MapLoader.OFFSET_MAPID] > MapLoader.MAPID_LIMIT) {
				break;
			}
			
			String spawnlist_npc_q = String.format ("SELECT * FROM spawnlist_npc WHERE mapid=\'%d\';", Info[MapLoader.OFFSET_MAPID]) ;
			ResultSet spawnlist_rs = Db.Query (spawnlist_npc_q) ;
			try {
				while (spawnlist_rs.next () ) {
					int Uuid = spawnlist_rs.getInt ("id") ;
					String Location = spawnlist_rs.getString ("location") ;
					int Count = spawnlist_rs.getInt ("count") ;
					int NpcTemplateId = spawnlist_rs.getInt ("npc_templateid") ;
					int LocX = spawnlist_rs.getInt ("locx") ;
					int LocY = spawnlist_rs.getInt ("locy") ;
					int MapId = spawnlist_rs.getInt ("mapid") ;
					int Heading = spawnlist_rs.getInt ("heading") ;
					int RandomX = spawnlist_rs.getInt ("randomx") ;
					int RandomY = spawnlist_rs.getInt ("randomy") ;
					int RespawnDelay = spawnlist_rs.getInt ("respawn_delay") ;
					int MovementDistance = spawnlist_rs.getInt ("movement_distance") ;

					if (CacheData.NpcCache.containsKey (NpcTemplateId) ) {
						NpcTemplate NpcData = CacheData.NpcCache.get (NpcTemplateId) ;
						NpcInstance Npc = new NpcInstance (NpcData) ;
						
						Npc.location.x = LocX;
						Npc.location.y = LocY;
						Npc.location.MapId = MapId;
						Npc.location.Heading = Heading;
						
						Asg.addNpc (Npc) ;
						ValidCount ++;
					} else {
						String msg = String.format ("\tNpc Template NOT EXIST, UUID:%d/Template:%d/Location:%s", 
							Uuid,
							NpcTemplateId,
							Location
						) ;
						ErrorList.add (msg) ;
					}
				}
			} catch (Exception e) {
				e.printStackTrace () ;
			}
		}
		
		long t_ends = System.currentTimeMillis () ;
		float used_time = (float) (t_ends - t_starts) / 1000;
		System.out.printf ("%d npcs loaded in\t%.3f s\n", ValidCount, used_time) ;
		
		if (ErrorList.size () > 0) {
			System.out.printf ("[***WARN!***] %d NPC LOAD FAIL:\n", ErrorList.size () ) ;
			for (String s : ErrorList) {
				System.out.println (s) ;
			}
		}
	}
}
