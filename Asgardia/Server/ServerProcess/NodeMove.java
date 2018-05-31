package Asgardia.Server.ServerProcess;

import Asgardia.World.*;
import Asgardia.World.Map.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Dynamic.*;
import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;

/*
 * 由Server端告知物件移動使用的封包
 * 令UUID物件由位置(x,y)往Heading方向移動一單位
 * 發送封包後記得更新原物件(x,y)
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
	
	public NodeMove (DynamicObject Node) {
	//public NodeMove (PcInstance Node) {
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
