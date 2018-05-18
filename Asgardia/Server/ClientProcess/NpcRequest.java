package Asgardia.Server.ClientProcess;

import java.util.*;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.*;
import Asgardia.World.Npc.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Items.*;

/*
 * type 
 * 0 : shop buy (contains player's shop)
 * 1 : shop sell
 * 2 : storage put in
 * 3 : storage take out
 * 4 : clan storage put in
 * 5 : clan storage take out
 */

public class NpcRequest
{
	int NpcId = 0;
	int ReqType = 0;
	int Size = 0;
	int Unknown = 0;
	
	PcInstance Pc = null;
	PacketReader reader = null;
	
	public NpcRequest (SessionHandler Handle, byte[] Data) {
		Pc = Handle.Account.ActivePc;
		reader = new PacketReader (Data) ;
		
		NpcId = reader.ReadDoubleWord () ;
		ReqType = reader.ReadByte () ;
		Size = reader.ReadByte () ;
		Unknown = reader.ReadByte () ;
		
		
		switch (ReqType) {
		case 0: //Shop buy
			ParseBuyList (Handle, Data) ;
			break;
			
		case 1: //Shop sell
			
			break;
			
		case 2: //storage put in
			break;
			
		case 3: //storage take out
			break;
			
		case 4: //clan storage put in
			break;
			
		case 5: //clan storage take out
			break;
			
		default:
			System.out.println ("unknown npc request!\n") ;
			break;
		}
		
	}
	
	void ParseBuyList (SessionHandler Handle, byte[] Data) {
		HashMap<Integer, NpcShopMenu> menu = CacheData.ShopCache.get (NpcId).Menu;
		
		List<ItemInstance> OrderItems = new ArrayList<ItemInstance> () ;
		int[] OrderId = new int[Size];
		int[] Count   = new int[Size];
		int TotalPrice = 0;
		int TotalWeight = 0;
		
		for (int index = 0; index < Size; index++) {
			OrderId[index] = reader.ReadDoubleWord () ;
			Count[index]   = reader.ReadDoubleWord () ;  
			NpcShopMenu i = menu.get (OrderId[index]) ;
			
			int PackCount = menu.get (OrderId[index]).PackCount;
			
			boolean Stackable = menu.get (OrderId[index]).isStackable () ;
			
			if (Stackable) {
				if (PackCount < 1) {
					PackCount = 1;
				}
				
				int TotalAmount = Count[index] * PackCount;
				
				ItemInstance item = menu.get (OrderId[index]).getItemInstance () ;
				item.OwnerId = Pc.Uuid;
				item.Count = TotalAmount;
				TotalPrice += item.Count * i.SellingPrice;
				TotalWeight += item.Count * item.Weight;
				OrderItems.add (item) ;
				
			} else {
				if (Count[index] > 1) {
					for (int j = 0; j < Count[index]; j++) {
						ItemInstance item = menu.get (OrderId[index]).getItemInstance () ;
						item.OwnerId = Pc.Uuid;
						item.Count = 1;
						TotalPrice += item.Count * i.SellingPrice;
						TotalWeight += item.Count * item.Weight;
						OrderItems.add (item) ;
					}
				} else {
					ItemInstance item = menu.get (OrderId[index]).getItemInstance () ;
					item.OwnerId = Pc.Uuid;
					item.Count = 1;
					TotalPrice += item.Count * i.SellingPrice;
					TotalWeight += item.Count * item.Weight;
					OrderItems.add (item) ;
				}
			}
		}
		
		if ((Pc.getWeight () + TotalWeight) > Pc.getMaxWeight () ) {
			Handle.SendPacket (new ServerMessage (82).getRaw () ) ; //超過最大負重
			return;
		}
		/*
		if (Pc.getMoney () < TotalPrice) {
			Handle.SendPacket (new ServerMessage (189).getRaw () ) ; //金幣不足
			return;
		}*/
		
		//Pc.removeItemByItemId (40308, TotalPrice) ;
		
		for (int index = 0; index < OrderItems.size () ; index++) {
			Pc.addItem (OrderItems.get (index) ) ;
		}
		
		/* PAY TAX */
		
		/*
		 * 更新資料庫
		 */
		Pc.SaveItem () ;
		
		/*
		 *更新角色狀況
		 */
		Handle.SendPacket (new NodeStatus (Pc).getRaw () ) ;
	}
}
