package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;

/*
 * 指定uuid執行一個action code
 */
public class NodeAction
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public NodeAction (int action_code, int uuid, int heading) {
		builder.writeByte (ServerOpcodes.NODE_ACTION) ;
		builder.writeByte (action_code) ;
		builder.WriteDoubleWord (uuid) ;
		builder.WriteDoubleWord (0) ;
		builder.writeByte (0x4E) ;
		builder.writeByte (heading) ; //目標物件的heading
		builder.WriteWord (0x00) ;
		builder.WriteWord (0x00) ;
		builder.writeByte (0x00) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
