package Asgardia.World;

import java.util.concurrent.*;
import java.sql.*;
import org.w3c.dom.*;

import Asgardia.Server.*;
import Asgardia.World.Npc.*;
import Asgardia.World.Objects.Template.*;

public class CacheData
{
	private static CacheData instance;
	private static HikariCP Db;
	
	public static ConcurrentHashMap<Integer, NpcTemplate> NpcCache;
	public static ConcurrentHashMap<String, Document> NpcActionCache;
	public static ConcurrentHashMap<Integer, NpcTalkData> NpcTalkDataCache;
	//public static ConcurrentHashMap MonsterCache;
	//public static ConcurrentHashMap MerchantCache;
	//public static ConcurrentHashMap HousekeeperCache;
	public static ConcurrentHashMap<Integer, ItemTemplate> ItemCache;
	public static ConcurrentHashMap<Integer, WeaponTemplate> WeaponCache;
	public static ConcurrentHashMap<Integer, ArmorTemplate> ArmorCache;
	
	public static ConcurrentHashMap<Integer, NpcShop> ShopCache;
	
	public static CacheData getInstance () {
		if (instance == null) {
			instance = new CacheData () ;
		}
		return instance;
	}
	
	public CacheData () {
		Db = HikariCP.getInstance () ;
		
		System.out.print ("Load Game data...\n") ;
		
		System.out.print ("\t-> Load npc cache data...") ;
		LoadNpcCache () ;
		
		System.out.print ("\t-> Load npc action cache data...") ;
		LoadNpcActionCache () ;
		
		System.out.print ("\t-> Load npc talk cache data...") ;
		LoadNpcTalkDataCache () ;
		
		System.out.print ("\t-> Load items cache data...") ;
		LoadItemCache () ;
		
		System.out.print ("\t-> Load shop cache data...") ;
		LoadShopCache () ;
		
		System.out.print ("\t-> Load weapons cache data...") ;
		LoadWeaponCache () ;
		
		System.out.print ("\t-> Load armor cache data...") ;
		LoadArmorCache () ;
		
		System.out.println () ;
		//cache pets types
		//cache monster types
		//cache npc types
	}
	
	
	
	public static void LoadNpcCache () { 
		NpcCache = new ConcurrentHashMap<Integer, NpcTemplate> () ;
		
		try {
			ResultSet rs = Db.Query ("SELECT * FROM npc;") ;
			int Counter = 0;
			long t_starts = System.currentTimeMillis () ;
			while (rs.next () ) {
				NpcTemplate NpcData = new NpcTemplate (
						rs.getInt ("npcid"), 
						rs.getString ("name"),
						rs.getString ("nameid"),
						rs.getString ("note"),
						rs.getString ("impl"),
						rs.getInt ("gfxid"), 
						rs.getInt ("lvl"),
						rs.getInt ("hp"),
						rs.getInt ("mp"), 
						rs.getInt ("ac"), 
						rs.getInt ("str"),
						rs.getInt ("con"),
						rs.getInt ("dex"),
						rs.getInt ("wis"),
						rs.getInt ("intel"),
						rs.getInt ("mr"),
						rs.getInt ("exp"),
						rs.getInt ("lawful"),
						rs.getString ("size"),
						rs.getInt ("weak_water"),
						rs.getInt ("weak_wind"), 
						rs.getInt ("weak_fire"),
						rs.getInt ("weak_earth"), 
						rs.getInt ("ranged"),
						rs.getBoolean ("tamable"),
						rs.getInt ("passispeed"),
						rs.getInt ("atkspeed"),
						rs.getInt ("atk_magic_speed"),
						rs.getInt ("sub_magic_speed"),
						rs.getInt ("undead"),
						rs.getInt ("poison_atk"),
						rs.getInt ("paralysis_atk"),
						rs.getInt ("agro"),
						rs.getInt ("agrososc"),
						rs.getInt ("agrocoi"),
						rs.getString ("family"),
						rs.getInt ("agrofamily"),
						rs.getInt ("picupitem"), 
						rs.getInt ("digestitem"),
						rs.getInt ("bravespeed"),
						rs.getInt ("hprinterval"), 
						rs.getInt ("hpr"),
						rs.getInt ("mprinterval"),
						rs.getInt ("mpr"),
						rs.getInt ("teleport"),
						rs.getInt ("randomlevel"),
						rs.getInt ("randomhp"),
						rs.getInt ("randommp"),
						rs.getInt ("randomac"),
						rs.getInt ("randomexp"),
						rs.getInt ("randomlawful"),
						rs.getInt ("damage_reduction"),
						rs.getInt ("hard"), 
						rs.getInt ("doppel"),
						rs.getInt ("IsTU"),
						rs.getInt ("IsErase"),
						rs.getInt ("bowActId"), 
						rs.getInt ("karma"),
						rs.getInt ("transform_id"),
						rs.getInt ("light_size"),
						rs.getInt ("amount_fixed"),
						rs.getInt ("atkexspeed"),
						rs.getInt ("attStatus"),
						rs.getInt ("bowUseId"),
						rs.getInt ("hascastle"),
						rs.getInt ("broad") ) ;
				
				NpcCache.put (NpcData.Uuid, NpcData) ;
				Counter ++;
			}
			long t_ends = System.currentTimeMillis () ;
			float used_time = (float) (t_ends - t_starts) / 1000;
			System.out.printf ("\t\t" + Counter + " npc cached in\t%.3f s\n", used_time) ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	public static void LoadNpcActionCache () {
		NpcActionCache = new ConcurrentHashMap<String, Document> () ;
		long t_starts = System.currentTimeMillis () ;
		
		NpcActionXmlLoader XmlLoader = new NpcActionXmlLoader (NpcActionCache) ;
		
		long t_ends = System.currentTimeMillis () ;
		float used_time = (float) (t_ends - t_starts) / 1000;
		System.out.printf ("\tnpc xml cached in\t%.3f s\n", used_time) ;
	}
	
	public static void LoadNpcTalkDataCache () {
		NpcTalkDataCache = new ConcurrentHashMap<Integer, NpcTalkData> () ;

		try {
			ResultSet rs = Db.Query ("SELECT * FROM npcaction;") ;
			int Counter = 0;
			long t_starts = System.currentTimeMillis () ;
			while (rs.next () ) {
				NpcTalkData TalkData = new NpcTalkData (
						rs.getInt ("npcid"),
						rs.getString ("normal_action"),
						rs.getString ("caotic_action"),
						rs.getString ("teleport_url"),
						rs.getString ("teleport_urla") ) ;
				
				NpcTalkDataCache.put (TalkData.NpcId, TalkData) ;
				
				Counter ++;
			}
			long t_ends = System.currentTimeMillis () ;
			float used_time = (float) (t_ends - t_starts) / 1000;
			System.out.printf ("\t\t" + Counter + " npc talk cached in\t%.3f s\n", used_time) ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
		
	}
	
	public static void LoadItemCache () {
		ItemCache = new ConcurrentHashMap<Integer, ItemTemplate> () ;
		
		try {
			ResultSet rs = Db.Query ("SELECT * FROM etcitem;") ;
			int Counter = 0;
			long t_starts = System.currentTimeMillis () ;
			while (rs.next () ) {
				ItemTemplate i = new ItemTemplate (
						rs.getInt ("item_id"),
						rs.getString ("name"),
						rs.getString ("name_id"),
						rs.getString ("item_type"),
						rs.getString ("use_type"),
						rs.getString ("material"),
						rs.getInt ("weight"),
						rs.getInt ("invgfx"),
						rs.getInt ("grdgfx"),
						rs.getInt ("itemdesc_id"),
						rs.getBoolean ("stackable"),
						rs.getInt ("max_charge_count"),
						rs.getInt ("dmg_small"),
						rs.getInt ("dmg_large"),
						rs.getInt ("min_lvl"),
						rs.getInt ("max_lvl"),
						rs.getInt ("bless"),
						rs.getBoolean ("trade"),
						rs.getInt ("delay_id"),
						rs.getInt ("delay_time"),
						rs.getInt ("food_volume"),
						rs.getBoolean ("save_at_once") ) ;
				
				ItemCache.put (rs.getInt ("item_id"), i) ;
				
				Counter ++;
			}
			long t_ends = System.currentTimeMillis () ;
			float used_time = (float) (t_ends - t_starts) / 1000;
			System.out.printf ("\t\t" + Counter + " items cached in\t%.3f s\n", used_time) ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	public static void LoadShopCache () {
		ShopCache = new ConcurrentHashMap<Integer, NpcShop> () ;
		
		try {
			ResultSet rs = Db.Query ("SELECT * FROM shop;") ;
			int Counter = 0;
			long t_starts = System.currentTimeMillis () ;
			while (rs.next () ) {
				int NpcId = rs.getInt ("npc_id") ;
				int ItemId = rs.getInt ("item_id") ;
				int OrderId = rs.getInt ("order_id") ;
				int SellingPrice = rs.getInt ("selling_price") ;
				int PackCount = rs.getInt ("pack_count") ;
				int PurchasingPrice = rs.getInt ("purchasing_price") ;
				
				if (!ShopCache.containsKey (NpcId) ) {
					NpcShop npc_shop = new NpcShop (NpcId) ;
					ShopCache.put (NpcId, npc_shop) ;
				}
				
				NpcShop ns = ShopCache.get (NpcId) ;
				NpcShopMenu item = new NpcShopMenu (NpcId, ItemId, OrderId, SellingPrice, PackCount, PurchasingPrice) ;
				
				ns.addMenuItem (item);
				//Item _i = CacheData.ItemCache.get (item.ItemId) ;
				//if (_i == null) {
				//	System.out.printf ("no cache data : %d\n", item.ItemId) ;
				//}
				//System.out.println (_i.Name) ;
				
				Counter ++;
			}
			long t_ends = System.currentTimeMillis () ;
			float used_time = (float) (t_ends - t_starts) / 1000;
			System.out.printf ("\t\t" + Counter + " shop cached in\t%.3f s\n", used_time) ;
			
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	public static void LoadWeaponCache () {
		WeaponCache = new ConcurrentHashMap<Integer, WeaponTemplate> () ;
		
		try {
			ResultSet rs = Db.Query ("SELECT * FROM weapon") ;
			int Counter = 0;
			long t_starts = System.currentTimeMillis () ;
			while (rs.next () ) {
				int item_id = rs.getInt ("item_id") ;
				WeaponTemplate w = new WeaponTemplate (
					item_id,
					rs.getString ("name"),
					rs.getString ("name_id"),
					rs.getString ("type"),
					rs.getString ("material"),
					rs.getInt ("weight"),
					rs.getInt ("invgfx"),
					rs.getInt ("grdgfx"),
					rs.getInt ("itemdesc_id"),
					rs.getInt ("dmg_small"),
					rs.getInt ("dmg_large"),
					rs.getInt ("safenchant"),
					rs.getBoolean ("use_royal"),
					rs.getBoolean ("use_knight"),
					rs.getBoolean ("use_mage"),
					rs.getBoolean ("use_elf"),
					rs.getBoolean ("use_darkelf"),
					rs.getInt ("hitmodifier"),
					rs.getInt ("dmgmodifier"),
					rs.getInt ("add_str"),
					rs.getInt ("add_con"),
					rs.getInt ("add_dex"),
					rs.getInt ("add_int"),
					rs.getInt ("add_wis"),
					rs.getInt ("add_cha"),
					rs.getInt ("add_hp"),
					rs.getInt ("add_mp"),
					rs.getInt ("add_hpr"),
					rs.getInt ("add_mpr"),
					rs.getInt ("add_sp"),
					//rs.getInt ("add_mr"),
					0, //no mr
					rs.getInt ("m_def"),
					rs.getBoolean ("haste_item"),
					rs.getInt ("magicdmgmodifier"),
					rs.getBoolean ("canbedmg"),
					rs.getInt ("min_lvl"),
					rs.getInt ("max_lvl"),
					rs.getInt ("bless"),
					rs.getBoolean ("trade"),
					rs.getBoolean ("mana_item") ) ;
				
				WeaponCache.putIfAbsent (item_id, w) ;
				Counter ++;
			}
			long t_ends = System.currentTimeMillis () ;
			float used_time = (float) (t_ends - t_starts) / 1000;
			System.out.printf ("\t\t" + Counter + " weapons cached in\t%.3f s\n", used_time) ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	public static void LoadArmorCache () {
		ArmorCache = new ConcurrentHashMap<Integer, ArmorTemplate> () ;
		
		try {
			ResultSet rs = Db.Query ("SELECT * FROM armor") ;
			int Counter = 0;
			long t_starts = System.currentTimeMillis () ;
			while (rs.next () ) {
				int item_id = rs.getInt ("item_id") ;
				
				ArmorTemplate a = new ArmorTemplate (
					item_id,
					rs.getString ("name"),
					rs.getString ("name_id"),
					rs.getString ("type"),
					rs.getString ("material"),
					rs.getInt ("weight"),
					rs.getInt ("invgfx"),
					rs.getInt ("grdgfx"),
					rs.getInt ("itemdesc_id"),
					rs.getInt ("ac"),
					rs.getInt ("safenchant"),
					rs.getBoolean ("use_royal"),
					rs.getBoolean ("use_knight"),
					rs.getBoolean ("use_mage"),
					rs.getBoolean ("use_elf"),
					rs.getBoolean ("use_darkelf"),
					rs.getInt ("add_str"),
					rs.getInt ("add_con"),
					rs.getInt ("add_dex"),
					rs.getInt ("add_int"),
					rs.getInt ("add_wis"),
					rs.getInt ("add_cha"),
					rs.getInt ("add_hp"),
					rs.getInt ("add_mp"),
					rs.getInt ("add_hpr"),
					rs.getInt ("add_mpr"),
					rs.getInt ("add_sp"),
					rs.getInt ("min_lvl"),
					rs.getInt ("max_lvl"),
					rs.getInt ("m_def"),
					rs.getBoolean ("haste_item"),
					rs.getInt ("damage_reduction"),
					rs.getInt ("weight_reduction"),
					rs.getInt ("bow_hit_rate"),
					rs.getInt ("bless"),
					rs.getBoolean ("trade"),
					rs.getInt ("defense_water"),
					rs.getInt ("defense_wind"),
					rs.getInt ("defense_fire"),
					rs.getInt ("defense_earth"),
					rs.getInt ("regist_stan"),
					rs.getInt ("regist_stone"),
					rs.getInt ("regist_sleep"),
					rs.getInt ("regist_freeze")	) ;
				ArmorCache.putIfAbsent (item_id, a) ;
				Counter ++;
			}
			long t_ends = System.currentTimeMillis () ;
			float used_time = (float) (t_ends - t_starts) / 1000;
			System.out.printf ("\t\t" + Counter + " armors cached in \t%.3f s\n", used_time) ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
}
