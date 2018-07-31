package Asgardia.Server.ServerProcess;

import Asgardia.Server.PacketBuilder;
import Asgardia.Server.SessionHandler;
import Asgardia.Server.Opcodes.ServerOpcodes;

public class ReportTitle
{
	PacketBuilder Builder = new PacketBuilder () ;
	
	public ReportTitle (SessionHandler Handle) {
		Builder.writeByte (ServerOpcodes.CHAR_TITLE) ;
		Builder.WriteDoubleWord (Handle.Account.ActivePc.Uuid) ;
		Builder.WriteString (Handle.Account.ActivePc.Title) ;
	}
	
	public byte[] getRaw () {
		return Builder.GetPacket () ;
	}
}
