package Asgardia.World.Objects.Items;

import Asgardia.World.*;

public class ItemUtility
{
	public static int getMajorType (int item_id) {
		
		if (CacheData.ItemCache.containsKey (item_id) ) {
			return 0;
		} else if (CacheData.WeaponCache.containsKey (item_id) ) {
			return 1;
		} else if (CacheData.ArmorCache.containsKey (item_id) ) {
			return 2;
		} 
		
		return 0xFF;
	}
}
