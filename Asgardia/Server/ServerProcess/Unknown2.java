package Asgardia.Server.ServerProcess;

import Asgardia.Server.PacketBuilder;
import Asgardia.Server.SessionHandler;
import Asgardia.Server.Opcodes.ServerOpcodes;

public class Unknown2
{
	public Unknown2 (SessionHandler Handle) 
	{
		PacketBuilder Builder = new PacketBuilder () ;
		Builder.WriteByte (ServerOpcodes.UNKNOWN2) ;
		Builder.WriteByte (0xFF) ;
		Builder.WriteByte (0x7F) ;
		Builder.WriteByte (0x03) ;
		Handle.SendPacket (Builder.GetPacket () ) ;
	}
}
