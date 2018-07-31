package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;

public class HpUpdate
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public HpUpdate (int hp, int max_hp) {
		builder.writeByte (ServerOpcodes.HP_UPDATE) ;
		builder.WriteWord (hp) ;
		builder.WriteWord (max_hp) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
