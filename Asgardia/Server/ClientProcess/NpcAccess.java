package Asgardia.Server.ClientProcess;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.*;
import Asgardia.World.Npc.*;
import Asgardia.World.Objects.*;

/*
 * 由使用者發起一個NPC(uuid)的內容訪問
 * 參考C_NPCTalk & NpcActionTable
 */

public class NpcAccess
{
	
	public NpcAccess (SessionHandler Handle, byte[] Packet) {
		PacketReader Reader = new PacketReader (Packet) ;
		PcInstance Pc = Handle.Account.ActivePc;
		int Uuid = Reader.ReadDoubleWord () ;
		
		System.out.printf ("%s 訪問NPC(%d)\n", Pc.Name, Uuid) ;
		
		if (Uuid == 70522) { //甘特
			String HtmlKey = null;
			switch (Pc.Type) {
			case 0:
				HtmlKey = "gunterp9"; break;
			case 1:
				HtmlKey = "gunterk9"; break;
			case 2:
				HtmlKey = "guntere1"; break;
			case 3:
				HtmlKey = "gunterw1"; break;
			case 4:
				HtmlKey = "gunterde1"; break;
			}
			
			byte[] packet = new NpcAccessResult (Uuid, HtmlKey).getRaw () ;
			Handle.SendPacket (packet) ;
			return ;
		}
		
		if (Uuid == 70009) { //吉倫
			String HtmlKey = null;
			switch (Pc.Type) {
			case 0: //Roayn
				HtmlKey = "gerengp1"; break;
			case 1: //Knight
				HtmlKey = "gerengk1"; break;
			case 2: //Elf
				HtmlKey = "gerenge1"; break;
			case 3: //Mage
				HtmlKey = "gerengw3"; break;
			case 4: //DarkElf
				HtmlKey = "gerengde1"; break;
			default:
				break;
			}
			
			byte[] packet = new NpcAccessResult (Uuid, HtmlKey).getRaw () ;
			Handle.SendPacket (packet) ;
			return ;
		}
		
		if (CacheData.NpcTalkDataCache.containsKey (Uuid) ) {
			NpcTalkData TalkData = CacheData.NpcTalkDataCache.get (Uuid) ;
			NpcAccessResult Result;
			if (Pc.Lawful < 0) {//邪惡
				Result = new NpcAccessResult (Uuid, TalkData.CaoticAction) ;
			} else {//中立, 正義
				Result = new NpcAccessResult (Uuid, TalkData.NormalAction) ;
			}
			
			Handle.SendPacket (Result.getRaw () ) ;
			
		} else {
			System.out.printf ("找不到NPCID\n") ;
		}
	}
}
