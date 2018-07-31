package Asgardia.Server.ServerProcess;

import Asgardia.Server.PacketBuilder;
import Asgardia.Server.SessionHandler;
import Asgardia.Server.Opcodes.ServerOpcodes;

public class Unknown2
{
	public Unknown2 (SessionHandler Handle) 
	{
		PacketBuilder Builder = new PacketBuilder () ;
		Builder.writeByte (ServerOpcodes.UNKNOWN2) ;
		Builder.writeByte (0xFF) ;
		Builder.writeByte (0x7F) ;
		Builder.writeByte (0x03) ;
		Handle.SendPacket (Builder.GetPacket () ) ;
	}
}
