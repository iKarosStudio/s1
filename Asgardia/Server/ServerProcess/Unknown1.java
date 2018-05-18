package Asgardia.Server.ServerProcess;

import Asgardia.Server.SessionHandler;
import Asgardia.Server.PacketBuilder;
import Asgardia.Server.Opcodes.ServerOpcodes;

public class Unknown1
{
	public Unknown1 (SessionHandler Handle) {
		PacketBuilder Builder = new PacketBuilder () ;
		Builder.WriteByte (ServerOpcodes.UNKNOWN1) ;
		Builder.WriteByte (0x03) ;
		Builder.WriteByte (0x00) ;
		Builder.WriteByte (0xF7) ;
		Builder.WriteByte (0xAD) ;
		Builder.WriteByte (0x74) ;
		Builder.WriteByte (0x00) ;
		Builder.WriteByte (0xE5) ;
		Handle.SendPacket (Builder.GetPacket () ) ;
	}
}
