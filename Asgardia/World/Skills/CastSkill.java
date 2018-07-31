package Asgardia.World.Skills;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Template.*;

public class CastSkill
{
	PcInstance pc;
	SessionHandler Handle;
	SkillTemplate SkillTemplate;
	int SkillID;
	int TargetUUID;
	int TargetX;
	int TargetY;
	String ClanName;
	String ClanMessage;
	
	public CastSkill (SessionHandler handle, byte[] Data, int skill_id, int target_uuid, int target_x, int target_y, String clan_name, String clan_message) {
		
		SkillTemplate = CacheData.SkillCache.get (skill_id) ;
		Handle = handle;
		pc = handle.getPc () ;
		SkillID = skill_id;
		if (target_uuid == 0) {
			TargetUUID = pc.Uuid;
		} else {
			TargetUUID = target_uuid;
		}
		TargetX = target_x;
		TargetY = target_y;
		ClanName = clan_name;
		ClanMessage = clan_message;
		
		/*
		 * 檢查角色能不能用這個技能
		 */
		
		/*
		 * 傷害效果計算
		 */
		
		/*
		 * 特效封包廣播
		 */
		dispSkillVirtualEffect () ;
		
		System.out.printf ("CastSkill:%s使用%s(%d)\n", pc.Name, SkillTemplate.Name, skill_id) ;
	}
	
	private void dispSkillVirtualEffect () {
		
		try {
			//技能效果
			byte[] virtual = new SkillGfx (TargetUUID, 2).getRaw ();
			Handle.SendPacket (virtual) ;
			
			//自身動作效果
			System.out.println ("skillid:"+SkillID) ;
			System.out.println ("GFX:"+SkillTemplate.GfxId) ;
			System.out.println ("TarUUID:"+TargetUUID) ;
			System.out.println ("TarX:"+TargetX) ;
			System.out.println ("TarY:"+TargetY) ;
			byte[] act = new CastTargetedSkill (Handle, SkillID, SkillTemplate.GfxId, TargetUUID, TargetX, TargetY).getRaw ();
			Handle.SendPacket (act);
			
			
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
}
