package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.Server.Opcodes.ServerOpcodes;
import Asgardia.World.Objects.*;

public class NodeEquipmentAc
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public NodeEquipmentAc (PcInstance pc) {
		builder.writeByte (ServerOpcodes.NODE_DEF) ;
		builder.writeByte (pc.getAc () ) ; //Ac
		builder.writeByte (0) ; //fire
		builder.writeByte (0) ; //water
		builder.writeByte (0) ; //wind
		builder.writeByte (0) ; //earth
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
