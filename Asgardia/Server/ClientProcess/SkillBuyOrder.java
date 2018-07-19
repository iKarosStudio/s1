package Asgardia.Server.ClientProcess;

import java.util.*;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.*;
import Asgardia.World.Objects.*;

public class SkillBuyOrder
{
	PacketReader reader;
	
	public SkillBuyOrder (SessionHandler handle, byte[] data) {
		reader = new PacketReader (data) ;
		PcInstance pc = handle.getPc () ;
		
		int OrderCount = reader.ReadWord () ;
		int SkillItem[] = new int[OrderCount] ;
		int L1 = 0, L1Cost = 0;
		int L2 = 0, L2Cost = 0;
		int L3 = 0, L3Cost = 0;
		int Price = 0;
		
		/* 讀取買入的選項 */
		for (int i = 0; i < OrderCount; i++) {
			SkillItem[i] = reader.ReadDoubleWord () ;
			
			if (SkillItem[i] < 8) { //level 1
				L1 |= (1 << SkillItem[i]) ;
				L1Cost += 100;
			} else if (SkillItem[i] < 16) { //level 2
				L2 |= (1 << (SkillItem[i]-8)) ;
				L2Cost += 400;
			} else if (SkillItem[i] < 24) { //level 3
				L3 |= (1 << (SkillItem[i]-16)) ;
				L3Cost += 900;
			}
			
		}
		
		/* 根據職業及角色等級過濾還不能學的選項 */
		if (pc.isRoyal ()) {
			if (pc.Level < 10) {
				L1 = 0; L1Cost = 0;
				L2 = 0; L2Cost = 0;
			} else if (pc.Level < 20) {
				L2 = 0; L2Cost = 0;
			}
			L3 = 0; L3Cost = 0;
		}
		
		if (pc.isKnight ()) {
			if (pc.Level < 50) {
				L1 = 0; L1Cost = 0;
			}
			L2 = 0; L2Cost = 0;
			L3 = 0; L3Cost = 0;
		}
		
		if (pc.isElf ()) {
			if (pc.Level < 8) {
				L1 = 0; L1Cost = 0;
				L2 = 0; L2Cost = 0;
				L3 = 0; L3Cost = 0;
			} else if (pc.Level < 16) {
				L2 = 0; L2Cost = 0;
				L3 = 0; L3Cost = 0;
			} else if (pc.Level < 24) {
				L3 = 0; L3Cost = 0;
			}
		}
		
		if (pc.isMage ()) {
			if (pc.Level < 4) {
				L1 = 0; L1Cost = 0;
				L2 = 0; L2Cost = 0;
				L3 = 0; L3Cost = 0;
			} else if (pc.Level < 8) {
				L2 = 0; L2Cost = 0;
				L3 = 0; L3Cost = 0;
			} else if (pc.Level < 12) {
				L3 = 0; L3Cost = 0;
			}
		}
		
		if (pc.isDarkElf ()) {
			if (pc.Level < 12) {
				L1 = 0; L1Cost = 0;
				L2 = 0; L2Cost = 0;
			} else if (pc.Level < 24) {
				L2 = 0; L2Cost = 0;
			}
			L3 = 0; L3Cost = 0;
		}
		
		if ( (L1 | L2 | L3) == 0) {
			return;
		}
		
		Price = L1Cost + L2Cost + L3Cost;
		if (pc.getMoney () < Price) {
			/* 錢不夠 */
			handle.SendPacket (new ServerMessage (189).getRaw () ) ;
		} else {
			HashMap<Integer, Integer> s = new HashMap<Integer, Integer> () ;
			for (int level = 1; level <= 24; level++) {
				if (level == 1) {
					s.put (level, L1) ;
				} else if (level == 2) {
					s.put (level, L2) ;
				} else if (level == 3) {
					s.put (level, L3) ;
				} else {
					s.put (level, 0) ;
				}
			}
			
			if (L1 != 0) {
				for (int b = 0; b < 7; b++) {
					if (((L1 >>> b) & 0x01) > 0) {
						System.out.printf ("Level1 skill id:%d\n", 1+b) ;
					}
				}
			}
			
			if (L2 != 0) {
				for (int b = 0; b < 7; b++) {
					if (((L2 >>> b) & 0x01) > 0) {
						System.out.printf ("Level2 skill id:%d\n", 9+b) ;
					}
				}
			}
			
			if (L3 != 0) {
				for (int b = 0; b < 7; b++) {
					if (((L3 >>> b) & 0x01) > 0) {
						System.out.printf ("Level3 skill id:%d\n", 17+b) ;
					}
				}
			}
			
			/* show virtual effect */
			handle.SendPacket (new SkillGfx (pc.Uuid, 224).getRaw () ) ;
			handle.SendPacket (new SkillTable (pc.Type,s).getRaw () ) ;
			
			//扣錢
			//Pc.removeItemByItemId (40308, Price) ;
		}
		
	}
}
