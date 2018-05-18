package Asgardia.World.Objects.Template;

import Asgardia.Server.*;
import Asgardia.World.Objects.Dynamic.DynamicObject;
import Asgardia.World.Objects.Template.*;

/*
 * 基本非玩家控制實體
 */
public class NpcTemplate extends DynamicObject
{
	public int Gfx = 0;
	public String Name;
	public String NameId;
	public String Note;
	public String NpcType;
	
	public NpcTemplate () {
		//System.out.println ("警告 不該被呼叫") ;
	}
	
	public NpcTemplate (
			int npc_id, //Template id
			String name,
			String name_id,
			String note,
			String impl, //NPC Type
			int gfxid, //shape
			int level, int hp, int mp, int ac,
			int str, int con, int dex, int wis, int intel,
			int mr, int exp, int lawful,
			String size,
			int weak_water, int weak_wind, int weak_fire, int weak_earth,
			int ranged, boolean tamable,
			int passispeed, int atkspeed, int atk_magic_speed, int sub_magic_speed,
			int undead, int poison_atk, int paralysis_atk,
			int agro, //主動被動設定
			int agrososc, //看穿隱身
			int agrocoi, //看穿變身
			String family,
			int argofamily, int pickupitem, int digestitem, int bravespeed,
			int hprinterval, int hpr, int mprinterval, int mpr, 
			int teleport,
			int random_level, int random_hp, int random_mp, int random_ac, int random_exp, int random_lawful,
			int damage_reduction, int hard, int doppel, int is_tu, int is_erase,
			int bow_act_id, int karma, int transform_id, int light_size, int amount_fixed, int atkexspeed,
			int att_status, int bow_use_id, int has_castle, int broad) 
	{
		Uuid = npc_id;
		Name = name;
		NameId = name_id;
		Note = note;
		NpcType = impl ;
		Gfx = gfxid;
	}
}
