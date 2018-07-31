package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.ServerOpcodes;

public class ServerMessage
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public ServerMessage (int msg_code) {
		this (msg_code, null) ;
	}
	
	public ServerMessage (int msg_code, String[] argv) {
		builder.writeByte (ServerOpcodes.SERVER_MSG) ;
		builder.WriteWord (msg_code) ;
		
		if (argv == null) {
			builder.writeByte (0) ;
			
		} else {
			builder.writeByte (argv.length) ;
			for (int index = 0; index < argv.length; index++) {
				builder.WriteString (argv[index]) ;
			}
			
		}
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
