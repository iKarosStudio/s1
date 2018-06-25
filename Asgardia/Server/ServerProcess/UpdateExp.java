package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.ServerOpcodes;
import Asgardia.World.Objects.*;

public class UpdateExp
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public UpdateExp (PcInstance p) {
		builder.WriteByte (ServerOpcodes.UPDATE_EXP) ;
		builder.WriteByte (p.Level) ;
		builder.WriteDoubleWord (p.Exp) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
