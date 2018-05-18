package Asgardia.World.Skills;

public class SkillEffect
{
	public int SkillId;
	public int RemainTime; /* Sesond */
	public int PolyId = 0;
	
	public SkillEffect (int skill_id, int remain_time) {
		SkillId = skill_id;
		RemainTime = remain_time;
	}
	
	public SkillEffect (int skill_id, int remain_time, int poly_id) {
		SkillId = skill_id;
		RemainTime = remain_time;
		PolyId = poly_id;
	}
}
