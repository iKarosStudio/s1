package Asgardia.Server.ClientProcess;

import Asgardia.Server.*;

import Asgardia.World.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Template.*;

public class SkillUse
{
	PacketReader reader;
	PcInstance pc;
	
	
	public SkillUse (SessionHandler handle, byte[] Data) {
		reader = new PacketReader (Data) ;
		pc = handle.getPc () ;
		try {
			for (byte d : Data) {
				System.out.printf ("0x%2X ", d) ;
			}
			System.out.println () ;
			
			int row = reader.ReadByte () ;
			int col = reader.ReadByte () ;
			int skill_id = (row << 3) + col + 1;
			SkillTemplate Skill = CacheData.SkillCache.get (skill_id) ;
			
			int uuid = 0;
			if (Skill.Target != 0) {
				uuid = reader.ReadDoubleWord () ;
			}
			
			System.out.printf ("use skill id:%d target:%d\n", skill_id, uuid) ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
}
