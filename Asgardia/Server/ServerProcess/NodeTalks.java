package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.World.Objects.*;

public class NodeTalks
{
	PacketBuilder Builder = new PacketBuilder () ;
	
	public NodeTalks (PcInstance Node, String Text, int Opcode, int TalksType) {		
		Builder.writeByte (Opcode) ;
		Builder.writeByte (TalksType) ;
		Builder.WriteDoubleWord (Node.Uuid) ;
		Builder.WriteString (Node.Name + ": " + Text) ;
	}
	
	public byte[] getRaw () {
		return Builder.GetPacket () ;
	}
}
