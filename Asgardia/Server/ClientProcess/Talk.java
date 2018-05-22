package Asgardia.Server.ClientProcess;


import Asgardia.Types.*;
import Asgardia.Config.*;
import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.Server.Opcodes.*;
import Asgardia.World.Objects.*;

public class Talk
{
	public Talk (SessionHandler Handle, byte[] Data) {
		PacketReader Reader = new PacketReader (Data) ;
		
		PcInstance Pc = Handle.Account.ActivePc;
		int TalkType = Reader.ReadByte () ;
		String Content = Reader.ReadString () ;
		
		
		/* 說壞壞的字>3<
		if (Talks.contains ("壞") ) {
		}
		*/
		
		if (Content.startsWith (".help") ) {
			byte[] packet = new SystemMessage (String.format ("RD開發資訊") ).getRaw () ;
			Handle.SendPacket (packet);
			return;
		}
		
		if (Content.startsWith (".tile") ) {
			
			String msg = String.format ("Tile(%d:%5d, %5d):0x%02X", Pc.CurrentMap.MapId, Pc.location.x, Pc.location.y, Pc.CurrentMap.getTile (Pc.location.x, Pc.location.y) ) ;
			Handle.SendPacket (new SystemMessage (msg).getRaw () ) ;
		}
		
		if (Content.startsWith (".tp") ) {
			String[] TpLocation = Content.split (" ") ;
			if (TpLocation.length == 4) {
				int DestMapId = Integer.valueOf (TpLocation[1]) ;
				int DestX = Integer.valueOf (TpLocation[2]) ;
				int DestY = Integer.valueOf (TpLocation[3]) ;
				
				
				String Msg = String.format ("Teleport to location(%d:%5d,%5d)\n", DestMapId, DestX, DestY) ;
				byte[] packet = new SystemMessage (Msg).getRaw () ;
				Handle.SendPacket (packet) ;
				
				Location Dest = new Location (DestMapId, DestX, DestY, Pc.location.Heading) ;
				new Teleport (Pc, Dest, true) ;
				
				return ;
			}
		}
		
		byte[] ChatPacket = new NodeTalks (Pc, Content, ServerOpcodes.NORMAL_TALKS, TalkType).getRaw () ; 
		
		if (TalkType == 0x00) { //普通對話
			Handle.SendPacket (ChatPacket) ;
			Pc.BoardcastPcInsight (ChatPacket) ;
		} else if (TalkType == 0x02) {//大喊
			//
		} else if (TalkType == 0x03) {//隊伍頻道
			//
		} else if (TalkType == 0x04) {//血盟頻道
			//
		}
		
		if (Configurations.DISPLAY_CHAT) {
			String TalkTypeS;
			switch (TalkType) {
				case 0 : TalkTypeS = "一般"; break;
				case 2 : TalkTypeS = "大喊"; break;
				case 3 : TalkTypeS = "隊伍"; break;
				case 4 : TalkTypeS = "血盟"; break;
				default : TalkTypeS = "不明"; break;
			}
			
			System.out.printf ("[%s]%s: %s ->", TalkTypeS, Pc.Name, Content) ;
			for (byte b : Content.getBytes () ) {
				System.out.printf ("0x%02X ", b) ;
			}
			System.out.println();
		}
	}
}
