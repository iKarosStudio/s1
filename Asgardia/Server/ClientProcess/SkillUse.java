package Asgardia.Server.ClientProcess;

import Asgardia.Server.*;

import Asgardia.World.Objects.*;
import Asgardia.World.Skills.*;
import static Asgardia.World.Skills.CommonSkill.SkillIdTable.*;

public class SkillUse
{
	PacketReader reader;
	PcInstance pc;
	
	
	public SkillUse (SessionHandler handle, byte[] Data) {
		reader = new PacketReader (Data) ;
		pc = handle.getPc () ;
		
		if (pc.isDead () ) {
			return;
		}
		
		try {
			/*
			for (byte d : Data) {
				System.out.printf ("0x%2X ", d) ;
			}
			System.out.println () ;
			*/
			
			int row = reader.ReadByte () ;
			int col = reader.ReadByte () ;
			int skill_id = (row << 3) + col + 1;
			int target_uuid = 0;
			int target_x = 0;
			int target_y = 0;
			String clan_name = null;
			String clan_message = null;
			
			if (Data.length > 4) {
				if (skill_id == CALL_CLAN || skill_id == RUN_CLAN) {
					clan_name = reader.ReadString () ;
					
				} else if (skill_id == TRUE_TARGET) {
					target_uuid = reader.ReadDoubleWord () ;
					target_x = reader.ReadWord () ;
					target_y = reader.ReadWord () ;
					clan_message = reader.ReadString () ;
					
				} else if (skill_id == SKILL_TELEPORT || skill_id == SKILL_MASS_TELEPORT) {
					reader.ReadWord () ;//mapid
					target_uuid = reader.ReadDoubleWord () ; //Bookmark id
					
				} else if (skill_id == SKILL_FIRE_WALL || skill_id == SKILL_LIFE_STREAM) {
					target_x = reader.ReadWord () ; 
					target_y = reader.ReadWord () ;
					
				} else {
					target_uuid = reader.ReadDoubleWord () ;
					if (Data.length > 11) {
						target_x = reader.ReadWord () ;
						target_y = reader.ReadWord () ;
					}
				}
			}
			
			new CastSkill (handle, Data, skill_id, target_uuid, target_x, target_y, clan_name, clan_message) ;
			
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
}
