package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.Server.Opcodes.ServerOpcodes;
import Asgardia.World.Objects.*;

public class NodeEquipmentAc
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public NodeEquipmentAc (PcInstance pc) {
		builder.WriteByte (ServerOpcodes.NODE_DEF) ;
		builder.WriteByte (pc.getAc () ) ; //Ac
		builder.WriteByte (0) ; //fire
		builder.WriteByte (0) ; //water
		builder.WriteByte (0) ; //wind
		builder.WriteByte (0) ; //earth
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
