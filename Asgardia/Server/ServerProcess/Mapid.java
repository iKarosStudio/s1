package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;

public class Mapid
{
	PacketBuilder b = new PacketBuilder () ;
	
	public Mapid (int map_id) {
		b.writeByte (ServerOpcodes.MAP_ID) ;
		b.WriteWord (map_id) ;
		b.writeByte (0x00) ; //1->underwater
		b.writeByte (0x00) ;
		b.WriteWord (0x0000) ;
		b.writeByte (0x00);
		b.WriteDoubleWord (0x00000000) ;
	}
	
	public byte[] getRaw () {
		return b.GetPacket () ;
	}
}
