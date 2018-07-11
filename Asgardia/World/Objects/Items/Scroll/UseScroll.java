package Asgardia.World.Objects.Items.Scroll;

import java.util.*;

import Asgardia.Types.*;
import Asgardia.Server.*;
import Asgardia.World.Map.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Items.ItemInstance;

public class UseScroll
{
	PcInstance Pc;
	SessionHandler Handle;
	public UseScroll (PcInstance pc, ItemInstance i, int target_uuid) {
		Pc = pc;
		Handle = Pc.getHandler () ;
			
		if (i.ItemId == 40100) { //順捲
			while (!checkScrollDelay (i) ) {
				try {
					Thread.sleep (500);
				} catch (Exception e) {} 
			}
			TeleportScroll () ;
			
		} else if (i.ItemId== 40079) { //回捲 
			
		} else if (i.ItemId == 40088) { //變形卷軸
			
		} else if (i.ItemId == 40126) { //鑑定卷軸
			ItemInstance t = Pc.FindItemByUuid (target_uuid) ;
			if (t != null) {
				//byte[] packet = new ItemIdentify (t).getRaw () ;
				//fix
				//Handle.SendPacket (packet) ;
			}

						
		} else {
			System.out.printf ("對%d使用卷軸:%s %d\n", target_uuid, i.getName (), i.ItemId) ;
		}
	}
	
	public boolean checkScrollDelay (ItemInstance i) {
		boolean res;
		long now_time = System.currentTimeMillis () ;
		
		if (Pc.getItemDelay (i.ItemId, now_time) > i.DelayTime) {
			Pc.setItemDelay (i.ItemId, now_time) ;
			res = true;
		} else {
			res = false;
		}
		
		return res;
	}
	
	private void TeleportScroll () {
		Location Dest;
		Dest = Pc.Map.getRandomLocation () ;		
		new Teleport (Pc, Dest, true) ;
		Dest = null;
	}
}
