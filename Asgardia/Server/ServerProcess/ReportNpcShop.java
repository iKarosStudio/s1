package Asgardia.Server.ServerProcess;

import java.util.*;
import java.math.*;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;
import Asgardia.World.*;
import Asgardia.World.Npc.*;
import Asgardia.World.Objects.Template.*;

public class ReportNpcShop
{
	PacketBuilder builder = new PacketBuilder () ;
	public int NpcId;
	
	public ReportNpcShop (int npc_id) {
		NpcId = npc_id;
	}
	
	public void BuyList () {
		NpcShop shop = CacheData.ShopCache.get (NpcId) ;
		//List<NpcShopMenu> menu = shop.Menu;
		HashMap<Integer, NpcShopMenu> menu = shop.Menu;
		
		builder.writeByte (ServerOpcodes.NPC_SELL_LIST) ;
		builder.WriteDoubleWord (NpcId) ;
		builder.WriteWord (menu.size () ) ;
		
		for (int i = 0; i < menu.size (); i++) {
			byte[] Detail = null;
			int InvGfx = 0;
			int Price = 0;
			String Name = null;
			/*
			 * 解析道具單項細節
			 */
			if (CacheData.ItemCache.containsKey (menu.get (i).ItemId) ) {
				ItemTemplate item = CacheData.ItemCache.get (menu.get (i).ItemId) ;
				InvGfx = item.InvGfx;
				Price = menu.get (i).SellingPrice;
				Name = item.Name;
				Detail = item.ParseItemDetail () ;
				
			} else if (CacheData.WeaponCache.containsKey (menu.get (i).ItemId) ) {
				WeaponTemplate weapon = CacheData.WeaponCache.get (menu.get (i).ItemId) ;
				InvGfx = weapon.InvGfx;
				Price = menu.get (i).SellingPrice;
				Name = weapon.Name;
				Detail = weapon.ParseWeaponDetail () ;
				
			} else if (CacheData.ArmorCache.containsKey (menu.get (i).ItemId) ) {
				ArmorTemplate armor = CacheData.ArmorCache.get (menu.get (i).ItemId) ;
				InvGfx = armor.InvGfx;
				Price = menu.get (i).SellingPrice;
				Name = armor.Name;
				Detail = armor.ParseArmorDetail () ;
				
			} else {
				System.out.printf ("unknow itemid : %d\n", menu.get (i).ItemId) ;
			}
			
			/*
			 * 單項標頭
			 */
			builder.WriteDoubleWord (i) ; //index
			builder.WriteWord (InvGfx) ;
			builder.WriteDoubleWord (Price) ;
			if (menu.get (i).PackCount > 1) {
				String n = String.format ("%s(%d)", Name, menu.get(i).PackCount) ;
				builder.WriteString (n) ;
			} else {
				builder.WriteString (Name) ;
			}
			/*
			 * 寫入每個道具細節數值
			 */
			builder.writeByte (Detail.length) ;
			if (Detail.length > 0) {
				for (byte b : Detail) {
					builder.writeByte (b) ;
				}
			}
		}
	}
	
	public void SellList () {
		builder.writeByte (ServerOpcodes.NPC_BUY_LIST) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
