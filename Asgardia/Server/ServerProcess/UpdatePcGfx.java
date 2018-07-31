package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;

public class UpdatePcGfx
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public UpdatePcGfx (int uuid, int gfx) {
		builder.writeByte (ServerOpcodes.UPDATE_PC_GFX) ;
		builder.WriteDoubleWord (uuid) ;
		builder.writeByte (gfx) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
