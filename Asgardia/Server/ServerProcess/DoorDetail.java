package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;
import Asgardia.World.Objects.*;

public class DoorDetail
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public DoorDetail (DoorInstance d) {
		builder.WriteByte (ServerOpcodes.DOOR_DETAIL) ;
		builder.WriteWord (d.Entracne.x) ;
		builder.WriteWord (d.Entracne.y) ;
		builder.WriteByte (d.location.Heading) ;
		builder.WriteByte (!d.isOpened) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
