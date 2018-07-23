package Asgardia.Server;

import java.sql.*;

import Asgardia.World.Npc.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Items.*;

public class DatabaseCmds
{
	static HikariCP Db = HikariCP.getInstance () ;

	
	public static ResultSet LoadAccount (String user_account) {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM accounts WHERE login=? LIMIT 1;") ;
			ps.setString (1, user_account) ;
			rs = ps.executeQuery () ;
		} catch (Exception e) {
			e.printStackTrace () ; 
		} finally {
			//DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
		return rs;
	}
	
	public static void CreateAccount (String user_account, String user_pw, String ip, String hostname) {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement ("INSERT INTO accounts set login=?, password=?, lastactive=?, access_level=?, ip=?, host=?, banned=?;") ;
			ps.setString (1, user_account) ;
			ps.setString (2, user_pw) ;
			ps.setTimestamp (3, new Timestamp (System.currentTimeMillis () ) ) ;
			ps.setString (4, ip) ;
			ps.setString (5, hostname) ;
			ps.execute () ;
			
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
	}
	
	public static ResultSet CheckOnlineCharacters (String user_account) {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM characters WHERE account_name=? AND OnlineStatus=?") ;
			ps.setString (1, user_account) ;
			ps.setInt (2, 1) ;
			
			rs = ps.executeQuery () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			//DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
		return rs;
	}
	
	public static int AccountCharacterAmount (String user_account) {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;	
		ResultSet rs = null;
		int count = 0;
				
		try {
			ps = con.prepareStatement ("SELECT count(*) as cnt FROM characters WHERE account_name=?") ;
			ps.setString (1, user_account) ;
			
			rs = ps.executeQuery () ;
			if (rs.next () ) {
				count = rs.getInt ("cnt") ;
			}
		} catch (Exception e) {
			e.printStackTrace () ;
			return 0;
		} finally {
			DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
		
		return count;
	}
	
	public static ResultSet AccountCharacters (String user_account) {
		Connection con = HikariCP.getConnection () ;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM characters WHERE account_name=? ORDER BY objid;") ;
			ps.setString (1, user_account) ;
			rs = ps.executeQuery () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			//DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
		
		return rs;
	}
	
	public static void UpdateAccountLoginTime (String user_account, String ip, String hostname) {
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement ("UPDATE accounts SET ip=?, host=?, lastactive=? WHERE login=?;") ;
			ps.setString (1, ip);
			ps.setString (2, hostname);
			ps.setTimestamp (3, new Timestamp (System.currentTimeMillis () ) ) ;
			ps.setString (4, user_account);
			
			ps.execute () ;
			
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
	}
	
	public static ResultSet LoadItem (int uuid) {
		Connection con = HikariCP.getConnection () ;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM character_items WHERE char_id=?;") ;
			ps.setInt (1, uuid) ;
			rs = ps.executeQuery () ;
			
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			//DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
		
		return rs;
	}
	
	public static ResultSet LoadSkills (int uuid) {
		Connection con = HikariCP.getConnection () ;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM character_skills WHERE char_obj_id=?;") ;
			ps.setInt (1, uuid) ;
			rs = ps.executeQuery () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			//DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
		
		return rs;		
	}
	
	public static void SaveSkills (int uuid, int skill_id, String skill_name) {
		Connection con = HikariCP.getConnection () ;
		PreparedStatement ps = null;
		
		try {
			ps = con.prepareStatement ("INSERT INTO character_skills SET char_obj_id=?, skill_id=?, skill_name=?") ;
			ps.setInt (1, uuid) ;
			ps.setInt (2, skill_id) ;
			ps.setString (3, skill_name) ;
			ps.execute () ;
			
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
	}
	
	public static boolean CheckSkill (int uuid, int skill_id) {
		Connection con = HikariCP.getConnection () ;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result = false;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM character_skills WHERE char_obj_id=? AND skill_id=?;") ;
			ps.setInt (1, uuid) ;
			ps.setInt (2, skill_id) ;
			
			rs = ps.executeQuery () ;
			
			if (rs.next ()) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
		
		return result;
	}
	
	public static ResultSet LoadTeleportBookmark (int uuid) {
		Connection con = HikariCP.getConnection () ;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM charater_teleport WHERE char_id=? ORDER BY name ASC;") ;
			ps.setInt (1, uuid) ;
			rs = ps.executeQuery () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
		
		return rs;
	}
	
	public static void SetItemEquip (int uuid, int isEquip) {
		//
	}
	
	public static void InsertPcItem (ItemInstance item) {
		Connection con = HikariCP.getConnection () ;
		PreparedStatement ps = null;
		
		try {
			ps = con.prepareStatement ("INSERT INTO character_items SET id=?, item_id=?, char_id=?, item_name=?, count=?, is_equipped=?, enchantlvl=?, is_id=?, durability=? ,charge_count=?;") ;
			ps.setInt (1, item.Uuid) ;
			ps.setInt (2, item.ItemId) ;
			ps.setInt (3, item.OwnerId) ;
			ps.setString (4, item.Name) ;
			ps.setInt (5, item.Count) ;
			ps.setInt (6, (item.IsEquipped) ? 1:0) ;
			ps.setInt (7, item.Enchant) ;
			ps.setInt (8, (item.IsIdentified) ? 1:0) ;
			ps.setInt (9, item.Durability) ;
			ps.setInt (10,item.ChargeCount) ;
			
			ps.execute () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
	}
	
	public static void UpdatePcItem (ItemInstance item) {
		Connection con = HikariCP.getConnection () ;
		PreparedStatement ps = null;
		
		try {
			ps = con.prepareStatement ("UPDATE character_items SET item_id=?, char_id=?, item_name=?, count=?, is_equipped=?, enchantlvl=?, is_id=?, durability=? ,charge_count=? where id=?;") ;
			ps.setInt (1, item.ItemId) ;
			ps.setInt (2, item.OwnerId) ;
			ps.setString (3, item.Name) ;
			ps.setInt (4, item.Count) ;
			ps.setInt (5, (item.IsEquipped) ? 1:0) ;
			ps.setInt (6, item.Enchant) ;
			ps.setInt (7, (item.IsIdentified) ? 1:0) ;
			ps.setInt (8, item.Durability) ;
			ps.setInt (9, item.ChargeCount) ;
			ps.setInt (10,item.Uuid) ;
			
			ps.execute () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
	}
	
	public static void DeletePcItem (ItemInstance item) {
		Connection con = HikariCP.getConnection () ;
		PreparedStatement ps = null;
		
		try {
			ps = con.prepareStatement ("DELETE FROM character_items WHERE id=?;") ;
			ps.setInt (1, item.Uuid) ;
			ps.execute () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
	}
	
	public static void SavePcEffect (PcInstance p) {
		//
	}
	
	public static void SavePc (PcInstance p) {
		Connection con = HikariCP.getConnection () ;
		PreparedStatement ps = null;
		
		try {
			ps = con.prepareStatement ("UPDATE characters SET level=?, Exp=?, MaxHp=?, CurHp=?, MaxMp=?, CurMp=?, Ac=?, Status=?, LocX=?, LocY=?, Heading=?, MapID=? WHERE objid=?;") ;
			ps.setInt (1, p.Level) ;
			ps.setInt (2, p.Exp) ;
			ps.setInt (3, p.BasicParameter.MaxHp) ;
			ps.setInt (4, p.Hp) ;
			ps.setInt (5, p.BasicParameter.MaxMp) ;
			ps.setInt (6, p.Mp) ;
			ps.setInt (7, p.getBaseAc () + p.getEquipAc () ) ;
			ps.setInt (8, p.Status) ;
			ps.setInt (9, p.location.x) ;
			ps.setInt (10, p.location.y) ;
			ps.setInt (11, p.location.Heading) ;
			ps.setInt (12, p.location.MapId) ;
			ps.setInt (13, p.Uuid) ;
			
			ps.execute () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
	}
	
	public static ResultSet DoorSpawinlist (int mapid) {
		Connection con = HikariCP.getConnection () ;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM spawnlist_door WHERE mapid=?;") ;
			ps.setInt (1, mapid) ;
			
			rs = ps.executeQuery () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			//DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
		
		return rs;
	}
	
	public static ResultSet MobSpawnlist (int mapid) {
		Connection con = HikariCP.getConnection () ;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM spawnlist WHERE mapid=?;") ;
			ps.setInt (1, mapid) ;
			rs = ps.executeQuery () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			//DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
		
		return rs;
	}
	
	public static ResultSet MobDroplist (int mob_id) {
		Connection con = HikariCP.getConnection () ;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM droplist WHERE mobId=?;") ;
			ps.setInt (1, mob_id) ;
			rs = ps.executeQuery () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			//DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
		
		return rs;
	}
}
