package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.ServerOpcodes;

/*
 * 指定uuid物件面向Heading
 */
public class NodeHeading
{
	PacketBuilder Builder = new PacketBuilder () ;
	public NodeHeading (int Uuid, int Heading) {
		
		Builder.WriteByte (ServerOpcodes.SET_HEADING) ;
		Builder.WriteDoubleWord (Uuid) ;
		Builder.WriteByte (Heading) ;
	}
	
	public byte[] getRaw () {
		return Builder.GetPacket () ;
	}
}
