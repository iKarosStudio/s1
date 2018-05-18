package Asgardia.World.Skills.CommonSkill;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;

public class SkillBrave
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public SkillBrave (int uuid, int move_speed, int time) {
		builder.WriteByte (ServerOpcodes.SKILL_BRAVE) ; 
		builder.WriteDoubleWord (uuid) ;
		builder.WriteByte (move_speed) ;
		
		/*
		 * Remain Time  0xFFFF -> 永久效果
		 */
		builder.WriteWord (time) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
