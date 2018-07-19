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
		
		/* count應為表達個職業可在NPC學習的技能數量限制-1(id) */
		if (pc_type == 0) { //Royal
			count = 16;
		} else if (pc_type == 1) { //Knight
			count = 8;
		} else if (pc_type == 2) { //Elf
			count = 23;
		} else if (pc_type == 3) { //Mage
			count = 23;
		} else if (pc_type == 4) { //Darkelf
			count = 16;
		}
		
		builder.WriteWord (count) ;
		for (int i = 0; i < count; i++) {
			builder.WriteDoubleWord (i) ;
		}
		
		Handle.SendPacket (builder.GetPacket () ) ;
	}
}
