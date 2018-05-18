package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;

public class SkillGfx
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public SkillGfx (int uuid, int gfx_id) {
		builder.WriteByte (ServerOpcodes.SKILL_GFX) ;
		builder.WriteDoubleWord (uuid) ;
		builder.WriteWord (gfx_id) ;
		builder.WriteWord (0) ;
		builder.WriteDoubleWord (0) ;
 	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}	
}
