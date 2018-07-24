package Asgardia.World.Npc;

import Asgardia.Server.Utility.*;
import Asgardia.World.*;
import Asgardia.World.Objects.Items.*;

public class NpcShopMenu
{
	public int NpcId;
	public int ItemId;
	public int OrderId;
	public int SellingPrice;
	public int PackCount;
	public int PurchasingPrice;
	
	public NpcShopMenu (
			int npc_id,
			int item_id,
			int order_id,
			int selling_price,
			int pack_count,
			int purchasing_price) {
		NpcId = npc_id;
		ItemId = item_id;
		OrderId = order_id;
		SellingPrice = selling_price; //販售價
		PackCount = pack_count;
		PurchasingPrice = purchasing_price; //收購價
	}
	
	public boolean isStackable () {
		boolean result = false;
		
		if (CacheData.ItemCache.containsKey (ItemId) ) {
			result = CacheData.ItemCache.get (ItemId).Stackable ;
		} 
		
		return result;
	}
	
	public ItemInstance getItemInstance () {
		ItemInstance i = new ItemInstance (
				UuidGenerator.Next (), //<-UuidGenerator.getInstance().Next()
				ItemId, 
				0,
				PackCount, 
				0, //Enchant level
				0, //Durability 
				100, //Charge Count
				false, //isEquipped
				true) ; //isIdentified
		
		return i;
	}
}
