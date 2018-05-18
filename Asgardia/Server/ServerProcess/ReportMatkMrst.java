package Asgardia.Server.ServerProcess;

import Asgardia.Server.SessionHandler;
import Asgardia.Server.PacketBuilder;
import Asgardia.Server.Opcodes.ServerOpcodes;
import Asgardia.World.Objects.*;

public class ReportMatkMrst
{
	PacketBuilder Builder = new PacketBuilder () ;
	
	public ReportMatkMrst (SessionHandler Handle) {
		PcInstance Pc = Handle.Account.ActivePc;
		
		Builder.WriteByte (ServerOpcodes.MATK_MRST) ;
		Builder.WriteByte (Pc.getSp () ) ; //sp
		Builder.WriteByte (Pc.getMr () ) ; //mr
	}
	
	public byte[] getRaw () {
		return Builder.GetPacket () ;
	}
}
