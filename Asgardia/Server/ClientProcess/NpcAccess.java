package Asgardia.Server.ClientProcess;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.*;
import Asgardia.World.Npc.*;
import Asgardia.World.Objects.*;

/*
 * 參考C_NPCTalk & NpcActionTable
 */

public class NpcAccess
{
	
	public NpcAccess (SessionHandler Handle, byte[] Packet) {
		PacketReader Reader = new PacketReader (Packet) ;
		PcInstance Pc = Handle.Account.ActivePc;
		int Uuid = Reader.ReadDoubleWord () ;
		
		System.out.printf ("使用者:%s\t訪問NPC UUID:%d\n", Pc.Name, Uuid) ;
		
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
		
		if (CacheData.NpcTalkDataCache.containsKey (Uuid) ) {
			NpcTalkData TalkData = CacheData.NpcTalkDataCache.get (Uuid) ;
			NpcAccessResult Result = new NpcAccessResult (Uuid, TalkData.NormalAction) ;
			Handle.SendPacket (Result.getRaw () ) ;
		} else {
			System.out.printf ("找不到NPCID\n") ;
		}
	}
}
