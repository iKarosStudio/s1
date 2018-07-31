package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;

public class ObjAction
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public ObjAction (int uuid, int action_code) {
		builder.writeByte (ServerOpcodes.OBJ_ACTION) ;
		builder.WriteDoubleWord (uuid) ;
		builder.writeByte (action_code) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
