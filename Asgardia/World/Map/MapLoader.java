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
		load (Handle) ;		
	}
	
	public static void load (Asgardia Handle) {
		/*
		 * 載入伺服器地圖檔案
		 */
		System.out.printf ("Load map files...") ;
		
		long t_starts = System.currentTimeMillis () ;

		MapInfo.getInstance ().Table.forEach ((Integer map_id, int[] Info)->{
			Connection con = HikariCP.getConnection () ;
			PreparedStatement ps = null;
			ResultSet rs = null;
			
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
				
				ps = con.prepareStatement ("SELECT * FROM dungeon WHERE src_mapid=?;") ;
				ps.setInt (1, map.MapId) ;
				rs = ps.executeQuery () ;
				
				while (rs.next () ) {
					Location Dest = new Location (rs.getInt("new_mapid"), rs.getInt("new_x"), rs.getInt ("new_y"), rs.getInt ("new_heading") ) ;
					map.addTpLocation (rs.getInt ("src_x"), rs.getInt ("src_y"), Dest) ;
				}
				
				//ValidCount++;
			} catch (Exception e) {
				e.printStackTrace () ;
			} finally {
				DatabaseUtil.close (rs) ;
				DatabaseUtil.close (ps) ;
				DatabaseUtil.close (con) ;
			}
		}); //end for mapid	
		long t_ends = System.currentTimeMillis () ;
		
		float used_time = (float) (t_ends - t_starts) / 1000;
		System.out.printf ("loaded in\t%.3f s\n", used_time) ;
	}
}
