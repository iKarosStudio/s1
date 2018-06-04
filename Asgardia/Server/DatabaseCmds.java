package Asgardia.Server;

import java.sql.*;

import Asgardia.World.Npc.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Items.*;

public class DatabaseCmds
{
	static HikariCP Db = HikariCP.getInstance () ;

	
	public static ResultSet LoadAccount (String user_account) {
		String q = String.format ("SELECT * FROM accounts WHERE login=\'%s\' LIMIT 1;" , user_account) ;
		return Db.Query (q) ;
	}
	
	public static void CreateAccount (String user_account, String user_pw, String ip, String hostname) {
		String q = String.format ("INSERT INTO accounts SET login=\'%s\',password=\'%s\',lastactive=\'%s\',access_level=\'0\',ip=\'%s\',host=\'%s\',banned=\'0\';",
				user_account,
				user_pw,
				new Timestamp (System.currentTimeMillis () ),
				ip,
				hostname) ;
		Db.Insert (q) ;
	}
	
	public static ResultSet CheckOnlineCharacters (String user_account) {
		String onlinecheck = String.format ("SELECT * FROM characters WHERE account_name=\'%s\' AND OnlineStatus=\'1\';",
				user_account) ;
		return Db.Query (onlinecheck) ;
	}
	
	public static int AccountCharacterAmount (String user_account) {
		String q = String.format ("SELECT count(*) as cnt FROM characters WHERE account_name=\'%s\'", user_account) ;
		ResultSet rs = Db.Query (q) ;
		try {
			if (rs.next () ) {
				return rs.getInt ("cnt") ;
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace () ;
			return 0;
		}
	}
	
	public static ResultSet AccountCharacters (String user_account) {
		String q = String.format ("SELECT * FROM characters WHERE account_name=\'%s\' ORDER BY objid" , user_account) ;
		return Db.Query (q) ;
	}
	
	public static void UpdateAccountLoginTime (String user_account, String ip, String hostname) {
		String q = String.format ("UPDATE accounts SET ip=\'%s\', host=\'%s\', lastactive=\'%s\' WHERE login=\'%s\';",
				ip,
				hostname,
				new Timestamp (System.currentTimeMillis () ) ,
				user_account) ;
		Db.Insert (q) ;
	}
	
	public static ResultSet LoadItem (int uuid) {
		String q = String.format ("SELECT * FROM character_items WHERE char_id=\'%s\';", uuid) ;
		return Db.Query (q) ;
	}
	
	public static ResultSet LoadTeleportBookmark (int uuid) {
		String q = String.format ("SELECT * FROM character_teleport WHERE char_id=\'%d\' ORDER BY name ASC", uuid) ;
		return Db.Query (q) ;
	}
	
	public static void SetItemEquip (int uuid, int isEquip) {
		//
	}
	
	public static void InsertPcItem (ItemInstance item) {
		 String Quere = String.format ("INSERT INTO character_items SET id=\'%d\',item_id=\'%d\',char_id=\'%d\',item_name=\'%s\',count=\'%d\',is_equipped=\'%d\',enchantlvl=\'%d\',is_id=\'%d\',durability=\'%d\',charge_count=\'%d\';",
					item.Uuid,
					item.ItemId,
					item.OwnerId,
					item.Name,
					item.Count,
					(item.IsEquipped) ? 1:0,
					item.Enchant,
					(item.IsIdentified) ? 1:0,
					item.Durability,
					item.ChargeCount) ;
		 Db.Insert (Quere) ;
	}
	
	public static void UpdatePcItem (ItemInstance item) {
		String Quere = String.format ("UPDATE character_items SET item_id=\'%d\',char_id=\'%d\',item_name=\'%s\',count=\'%d\',is_equipped=\'%d\',enchantlvl=\'%d\',is_id=\'%d\',durability=\'%d\',charge_count=\'%d\' where id=\'%d\';",
				item.ItemId,
				item.OwnerId,
				item.Name,
				item.Count,
				(item.IsEquipped) ? 1:0,
				item.Enchant,
				(item.IsIdentified) ? 1:0,
				item.Durability,
				item.ChargeCount,
				item.Uuid) ;
		
		Db.Insert (Quere) ;
	}
	
	public static void DeletePcItem (ItemInstance item) {
		String Quere = String.format ("DELETE FROM character_items WHERE id=\'%d\';", item.Uuid) ;
		Db.Insert (Quere) ;
	}
	
	public static void SavePc (PcInstance p) {
		String q = String.format ("UPDATE characters SET level=\'%d\',Exp=\'%d\',MaxHp=\'%d\',CurHp=\'%d\',MaxMp=\'%d\',CurMp=\'%d\',Ac=\'%d\',Status=\'%d\',LocX=\'%d\',LocY=\'%d\',Heading=\'%d\',MapID=\'%d\' WHERE objid=\'%d\';",
				p.Level,
				p.Exp,
				p.BasicParameter.MaxHp,
				p.Hp,
				p.BasicParameter.MaxMp,
				p.Mp,
				p.BasicParameter.Ac,
				p.Status,
				p.location.x,
				p.location.y,
				p.location.Heading,
				p.location.MapId,
				p.Uuid) ;
		Db.Insert (q);
	}
	
	public static ResultSet DoorSpawinlist (int mapid) {
		String q = String.format ("SELECT * FROM spawnlist_door WHERE mapid=\'%d\';", mapid) ;
		return Db.Query (q) ;		
	}
	
	public static ResultSet MobSpawnlist (int mapid) {
		String q = String.format ("SELECT * FROM spawnlist WHERE mapid=\'%d\' and id='62000';", mapid) ; //test
		//String q = String.format ("SELECT * FROM spawnlist WHERE mapid=\'%d\';", mapid) ;
		return Db.Query (q) ;
	}
	
	public static ResultSet MobDroplist (int mob_id) {
		String q = String.format ("SELECT * FROM droplist WHERE mobId=\'%d\';", mob_id) ;
		return Db.Query (q) ;
	}
}
