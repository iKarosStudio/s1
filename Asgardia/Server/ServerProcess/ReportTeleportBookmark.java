package Asgardia.Server.ServerProcess;

import Asgardia.Server.PacketBuilder;
import Asgardia.Server.SessionHandler;
import Asgardia.Server.Opcodes.ServerOpcodes;

public class ReportTeleportBookmark
{
	public ReportTeleportBookmark (SessionHandler Handle) {
		PacketBuilder Builder = new PacketBuilder () ;
		Builder.writeByte (ServerOpcodes.BOOKMARK) ;
		Builder.WriteString ("dd"); //name
		Builder.WriteWord (0) ; //mapid
		Builder.WriteDoubleWord (Handle.Account.ActivePc.Uuid) ;
		Handle.SendPacket (Builder.GetPacket () ) ;
		
		System.out.println ("send bookmark") ;
	}
}
