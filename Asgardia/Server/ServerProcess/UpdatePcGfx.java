package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;

public class UpdatePcGfx
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public UpdatePcGfx (int uuid, int gfx) {
		builder.WriteByte (ServerOpcodes.UPDATE_PC_GFX) ;
		builder.WriteDoubleWord (uuid) ;
		builder.WriteByte (gfx) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
