package Asgardia.Server.ServerProcess;

import Asgardia.World.*;
import Asgardia.World.Map.*;
import Asgardia.World.Objects.*;
import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;

/*
 * 由Server端告知物件移動使用的封包
 */
public class NodeMove
{
	PacketBuilder builder = null;
	public NodeMove (int uuid, int x, int y, int heading) {
		builder = new PacketBuilder () ;

		builder.WriteByte (ServerOpcodes.MOVE_NODE) ;
		builder.WriteDoubleWord (uuid) ;
		builder.WriteWord (x) ;
		builder.WriteWord (y) ;
		builder.WriteByte (heading) ;
		builder.WriteByte (129) ;
		builder.WriteDoubleWord (0) ;
	}
	
	public NodeMove (PcInstance Node) {
		builder = new PacketBuilder () ;

		builder.WriteByte (ServerOpcodes.MOVE_NODE) ;
		builder.WriteDoubleWord (Node.Uuid) ;
		builder.WriteWord (Node.location.x) ;
		builder.WriteWord (Node.location.y) ;
		builder.WriteByte (Node.location.Heading) ;
		builder.WriteByte (129) ;
		builder.WriteDoubleWord (0) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
