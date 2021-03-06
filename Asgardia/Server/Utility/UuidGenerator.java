package Asgardia.Server.Utility;

import java.sql.*;

import Asgardia.Server.*;

public class UuidGenerator
{
	private static UuidGenerator instance;

	private static int Uuid;
	
	public static UuidGenerator getInstance () {
		if (instance == null) {
			instance = new UuidGenerator () ;
		}
		return instance;
	}
	
	public static synchronized int Next () {
		Uuid ++;
		return Uuid;
	}
	
	public static int Now () {
		return Uuid;
	}
	
	public UuidGenerator () {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			String quere = "SELECT MAX(id)+1 AS nextid FROM (select id from character_items union all select id from character_teleport union all select id from character_warehouse union all select objid as id from characters union all select clan_id as id from clan_data union all select id from clan_warehouse union all select objid as id from pets) t" ;
			ps = con.prepareStatement (quere) ;
			rs = ps.executeQuery () ;
			
			while (rs.next () ) {
				int uuid = rs.getInt ("nextid") ;
				
				if (uuid < 0x10000000) {
					uuid = 0x10000000;
				} 
				Uuid = uuid;
				
				System.out.printf ("UUID Generator:0x%08X\n", Uuid) ;
				System.out.printf ("\t %d UUIDs reaches limit\n", 0xFFFFFFFF - Uuid) ;
			}
			
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
	}
}
