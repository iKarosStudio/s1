package Asgardia.World.Npc;

import java.util.*;

public class NpcShop
{
	public int NpcId;
	public HashMap<Integer, NpcShopMenu> Menu = new HashMap <Integer, NpcShopMenu> () ;
	
	public NpcShop (int npc_id) {
		NpcId = npc_id;
	}
	
	public void addMenuItem (NpcShopMenu item) {
		Menu.put (item.OrderId, item) ;
	}
}
