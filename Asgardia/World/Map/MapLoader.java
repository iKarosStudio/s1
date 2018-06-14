package Asgardia.World.Map;

import java.io.*;
import java.sql.*;

import Asgardia.Types.*;
import Asgardia.Server.*;
import Asgardia.World.*;

public class MapLoader
{
	public static final int OFFSET_MAPID = 0;
	public static final int OFFSET_START_X = 0;
	public static final int OFFSET_END_X = 1;
	public static final int OFFSET_START_Y = 2;
	public static final int OFFSET_END_Y = 3;
	
	public static final int MAPID_LIMIT = 3;
	
	public MapLoader (Asgardia Handle) 
	{
		//int x = 0;
		//int y = 0;
		//int ValidCount = 0;
		
		/*
		 * 載入伺服器地圖檔案
		 */
		System.out.printf ("Load map files...") ;
		
		long t_starts = System.currentTimeMillis () ;
		//for (int[] Info : MapInfo.INFO) {
		MapInfo.getInstance ().Table.forEach ((Integer map_id, int[] Info)->{
			try {
				if (map_id > MAPID_LIMIT) return ;
				
				String Path = String.format ("./maps/%d.txt", map_id) ;
				FileReader FileHandler = new FileReader (Path) ;
				BufferedReader b = new BufferedReader (FileHandler) ;
				//System.out.printf ("\tLoad map->%s\n", Path) ;
				
				AsgardiaMap map = new AsgardiaMap (
						map_id,
						Info[OFFSET_START_X],
						Info[OFFSET_END_X],
						Info[OFFSET_START_Y],
						Info[OFFSET_END_Y] ) ;
				
				String s = null;
				
				int y = 0;
				while ((s = b.readLine () ) != null) {
					if (s.length () == 0) {
						continue;
					}
					
					String[] tile = s.split (",") ;
					int x = 0;
					for (String t : tile) {
						map.setTile (x, y, Byte.valueOf (t) ) ;
						x++;
					}
					y++;
				}
				
				/*
				 * 將地圖加入世界管理
				 */
				Handle.addMap (map) ;
				FileHandler.close () ;
				
				/*
				 * 讀取地圖傳送點並新增
				 */
				HikariCP Db = HikariCP.getInstance () ;
				String ReqTpTable = String.format ("SELECT * FROM dungeon WHERE src_mapid=\'%d\';", map.MapId) ;
				ResultSet Tps = Db.Query (ReqTpTable) ;
				
				while (Tps.next () ) {
					Location Dest = new Location (Tps.getInt("new_mapid"), Tps.getInt("new_x"), Tps.getInt ("new_y"), Tps.getInt ("new_heading") ) ;
					map.addTpLocation (Tps.getInt ("src_x"), Tps.getInt ("src_y"), Dest) ;
				}
				
				//ValidCount++;
			} catch (Exception e) {
				//System.out.println (e.toString () + " x:" + x + " y:" + y) ;
				e.printStackTrace () ;
			}
		}); //end for mapid	
		long t_ends = System.currentTimeMillis () ;
		
		float used_time = (float) (t_ends - t_starts) / 1000;
		System.out.printf ("loaded in\t%.3f s\n", used_time) ;
	}
}
