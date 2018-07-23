package Asgardia.World.Map;

import java.sql.*;

import Asgardia.Server.*;
import Asgardia.World.*;
import Asgardia.World.Objects.*;

public class DoorGenerator
{
	private static DoorGenerator instance;
	private static Asgardia World;
	
	public static DoorGenerator getInstance () {
		if (instance == null) {
			instance = new DoorGenerator () ;
		}
		return instance;
	}
	
	public DoorGenerator () {
		World = Asgardia.getInstance () ;
		
		for (int MapId = 0; MapId < MapLoader.MAPID_LIMIT; MapId++) {
			AsgardiaMap map = World.getMap (MapId) ;
			if (map != null) {
				ResultSet rs = DatabaseCmds.DoorSpawinlist (MapId) ;
				try {
					while (rs.next () ) {
						DoorInstance d = new DoorInstance (
							rs.getInt ("id"),
							rs.getString ("location"),
							rs.getInt ("gfxid"),
							rs.getInt ("locx"),
							rs.getInt ("locy"),
							rs.getInt ("mapid"),
							rs.getInt ("direction"),
							rs.getInt ("entrancex"),
							rs.getInt ("entrancey"),
							rs.getInt ("hp"),
							rs.getBoolean ("keeper"),
							rs.getInt ("key"),
							rs.getInt ("size"),
							rs.getInt ("castle"),
							rs.getInt ("order")
						) ;
						
						map.addDoor (d) ;
						map.setAccessible (d.location.x, d.location.y, false) ;
					}
				} catch (Exception e) {
					e.printStackTrace () ;
				} finally {
					DatabaseUtil.close (rs) ;
				}
			}
		}
		
		
		
		//DatabaseCmds.DoorSpawinlist (0) ;
	}
}
