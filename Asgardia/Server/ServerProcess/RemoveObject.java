package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;

public class RemoveObject
{
	private PacketBuilder builder = new PacketBuilder () ;
	
	public RemoveObject (int uuid) {
		builder.WriteByte (ServerOpcodes.REMOVE_OBJECT) ;
		builder.WriteDoubleWord (uuid) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
