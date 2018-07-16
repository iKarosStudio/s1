package Asgardia.Server.ServerProcess;

import java.util.*;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;
import Asgardia.World.Objects.*;

public class SkillTable
{
	PacketBuilder builder = new PacketBuilder () ;
	

	public SkillTable (int pc_type, HashMap<Integer, Integer> skill_table) {		
		builder.WriteByte (ServerOpcodes.SKILL_TABLE) ;
		
		int check_5_8 = 0;
		int check_9_10 = 0;
		
		for (int i = 5; i <= 10; i++) {
			if (i < 9) {
				check_5_8 += skill_table.get (i) ;
			} else {
				check_9_10 += skill_table.get (i) ;
			}
		}
		
		if (check_5_8 > 0 && check_9_10 == 0) {
			builder.WriteByte (50) ;
		} else if (check_9_10 > 0) {
			builder.WriteByte (100) ;
		} else {
			builder.WriteByte (22);
		}
		
		for (int i = 1; i <= 24; i++) {
			builder.WriteByte (skill_table.get (i) ) ;
		}
		builder.WriteDoubleWord (0) ;
		builder.WriteDoubleWord (0) ;
		
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
