package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.ServerOpcodes;

public class SystemMessage
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public SystemMessage (String msg) {
		builder.writeByte (ServerOpcodes.SYSTEM_MSG) ;
		builder.writeByte (0x09) ;
		builder.WriteString (msg) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
