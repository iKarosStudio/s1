package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;

public class SkillBuyList
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public SkillBuyList (SessionHandler Handle, int pc_type) {
		int count = 0;
		
		builder.WriteByte (ServerOpcodes.SKILL_BUY_RESULT) ;
		builder.WriteDoubleWord (100) ;
		
		if (pc_type == 0) {
			count = 16;
		} else if (pc_type == 1) {
			count = 8;
		} else if (pc_type == 2) {
			count = 23;
		} else if (pc_type == 3) {
			count = 16;
		}
		
		builder.WriteWord (count) ;
		for (int i = 0; i < count; i++) {
			builder.WriteDoubleWord (i) ;
		}
		
		Handle.SendPacket (builder.GetPacket () ) ;
		System.out.printf ("skill buy result\n") ;
	}
}
