package Asgardia.World.Skills.CommonSkill;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;

public class SkillHaste
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public SkillHaste (int uuid, int move_speed, int time) {
		builder.writeByte (ServerOpcodes.SKILL_HASTE) ; 
		builder.WriteDoubleWord (uuid) ;
		builder.writeByte (move_speed) ;
		
		/*
		 * Remain Time  0xFFFF -> 永久效果
		 */
		builder.WriteWord (time) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
