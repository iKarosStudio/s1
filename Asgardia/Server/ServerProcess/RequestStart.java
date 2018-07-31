package Asgardia.Server.ServerProcess;

import Asgardia.Server.SessionHandler;
import Asgardia.Server.PacketBuilder;
import Asgardia.Server.Opcodes.ServerOpcodes;

public class RequestStart
{
	public RequestStart (SessionHandler Handle) 
	{
		PacketBuilder Builder = new PacketBuilder () ;
		Builder.writeByte (ServerOpcodes.LOGIN_START) ;
		Builder.writeByte (0x14) ;
		Builder.writeByte (0x69) ;
		/*
		Builder.WriteByte (0x50) ;
		Builder.WriteByte (0x40) ;
		Builder.WriteByte (0x30) ;
		Builder.WriteByte (0x25) ;
		Builder.WriteByte (0x55) ;
		*/
		Handle.SendPacket (Builder.GetPacket () ) ;
	}
}
