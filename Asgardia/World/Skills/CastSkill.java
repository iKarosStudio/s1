package Asgardia.World.Skills;

import Asgardia.Server.*;
import Asgardia.World.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Template.*;

public class CastSkill
{
	PcInstance pc;
	
	public CastSkill (SessionHandler handle, byte[] Data, int skill_id, int target_uuid, int target_x, int target_y, String clan_name, String clan_message) {
		SkillTemplate skill_template = CacheData.SkillCache.get (skill_id) ;
		pc = handle.getPc () ;
		
		System.out.printf ("CastSkill:%s使用%s(%d)\n", pc.Name, skill_template.Name, skill_id) ;
	}
}
