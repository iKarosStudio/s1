package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;

public class MpUpdate
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public MpUpdate (int mp, int max_mp) {
		builder.WriteByte (ServerOpcodes.MP_UPDATE) ;
		builder.WriteWord (mp) ;
		builder.WriteWord (max_mp) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
