package Asgardia.World.Objects.Items.Potion;

import java.util.Random;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Items.*;
import Asgardia.World.Skills.*;
import Asgardia.World.Skills.CommonSkill.*;

public class UsePotion
{
	PcInstance Pc;
	SessionHandler Handle;
	public UsePotion (PcInstance pc, ItemInstance i) {
		Pc = pc;
		Handle = Pc.getHandler () ;
		
		byte[] gfx_packet = null;
		
		if (i.ItemId == 40013 || i.ItemId == 40030) { //綠水, 象牙塔綠色藥水
			Pc.MoveSpeed = 1;
			gfx_packet = new SkillGfx (Pc.Uuid, 191).getRaw () ;
			Handle.SendPacket (gfx_packet) ; //Virtual effect
			Handle.SendPacket (new SkillHaste (Pc.Uuid, 1, 300).getRaw () ) ;
			Pc.addSkillEffect (SkillId.STATUS_HASTE, 300);
			
		} else if (i.ItemId == 40018) { //強綠
			Pc.MoveSpeed = 1;
			gfx_packet = new SkillGfx (Pc.Uuid, 191).getRaw () ;
			Handle.SendPacket (gfx_packet) ; //Virtual effect
			Handle.SendPacket (new SkillHaste (Pc.Uuid, 1, 1800).getRaw () ) ;
			Pc.addSkillEffect (SkillId.STATUS_HASTE, 1800);
			
		} else if (i.ItemId == 40014) { //勇敢藥水
			Pc.BraveSpeed = 1;
			gfx_packet = new SkillGfx (Pc.Uuid, 751).getRaw () ;
			Handle.SendPacket (gfx_packet) ; //Virtual effect
			Handle.SendPacket (new SkillBrave (Pc.Uuid, 1, 300).getRaw () ) ;
			Pc.addSkillEffect (SkillId.STATUS_BRAVE, 300) ;
			
		} else if (i.ItemId == 40068) { //精靈餅乾
			Pc.BraveSpeed = 1;
			gfx_packet = new SkillGfx (Pc.Uuid, 751).getRaw () ;
			Handle.SendPacket (gfx_packet) ; //Virtual effect
			Handle.SendPacket (new SkillBrave (Pc.Uuid, 1, 300).getRaw () ) ;
			Pc.addSkillEffect (SkillId.STATUS_BRAVE, 300) ;
			
		} else if (i.ItemId == 40010 || i.ItemId == 40019 || i.ItemId == 40029) { //紅色藥水, 濃縮紅色藥水,象牙塔紅色藥水
			if (checkPotionDelay (i) ) {
				if (UseHealPotion (15, i.DelayTime) ) { //成功使用藥水
					gfx_packet = new SkillGfx (Pc.Uuid, 189).getRaw () ;
					Handle.SendPacket (gfx_packet) ; //Virtual effect	
				}
			}
		} else if (i.ItemId == 40011 || i.ItemId == 40020) { //橙色藥水, 濃縮橙色藥水
			if (checkPotionDelay (i) ) {
				if (UseHealPotion (45, i.DelayTime) ) { //成功使用藥水
					gfx_packet = new SkillGfx (Pc.Uuid, 194).getRaw () ;
					Handle.SendPacket (gfx_packet) ; //Virtual effect	
				}
			}
			
		} else if (i.ItemId == 40012 || i.ItemId == 40021) { //白色藥水, 濃縮白色藥水
			if (checkPotionDelay (i) ) {
				if (UseHealPotion (75, i.DelayTime) ) { //成功使用藥水
					gfx_packet = new SkillGfx (Pc.Uuid, 197).getRaw () ;
					Handle.SendPacket (gfx_packet) ; //Virtual effect	
				}
			}
			
		} else if (i.ItemId == 40022) { //古代紅色藥水
			if (checkPotionDelay (i) ) {
				if (UseHealPotion (20, i.DelayTime) ) { //成功使用藥水
					gfx_packet = new SkillGfx (Pc.Uuid, 189).getRaw () ;
					Handle.SendPacket (gfx_packet) ; //Virtual effect	
				}
			}
			
		} else if (i.ItemId == 40023) { //古代澄色藥水
			if (checkPotionDelay (i) ) {
				if (UseHealPotion (30, i.DelayTime) ) { //成功使用藥水
					gfx_packet = new SkillGfx (Pc.Uuid, 194).getRaw () ;
					Handle.SendPacket (gfx_packet) ; //Virtual effect	
				}
			}
			
		} else if (i.ItemId == 40024) { //古代白色藥水
			if (checkPotionDelay (i) ) {
				if (UseHealPotion (55, i.DelayTime) ) { //成功使用藥水
					gfx_packet = new SkillGfx (Pc.Uuid, 197).getRaw () ;
					Handle.SendPacket (gfx_packet) ; //Virtual effect	
				}
			}
			
		} else if (i.ItemId == 40506) { //安特的水果
			if (checkPotionDelay (i) ) {
				if (UseHealPotion (70, i.DelayTime) ) { //成功使用藥水
					gfx_packet = new SkillGfx (Pc.Uuid, 197).getRaw () ;
					Handle.SendPacket (gfx_packet) ; //Virtual effect	
				}
			}
		} else if (i.ItemId == 40043) { //兔肝
			//
		} else {
			System.out.printf ("%s使用不明的藥水ItemId:%d\n", Pc.Name, i.ItemId) ;
		}
		
		Pc.BoardcastPcInsight (gfx_packet) ;
	}
	
	public boolean checkPotionDelay (ItemInstance i) {
		boolean res;
		long now_time = System.currentTimeMillis () ;
		
		if (Pc.getItemDelay (i.ItemId, now_time) > i.DelayTime) {
			Pc.setItemDelay (i.ItemId, now_time) ;
			res = true;
		} else {
			res = false;
		}
		
		return res;
	}
	
	public void UseBravePotion (ItemInstance i) {
		//C_ItemUse.java : 4006
	}
	
	public void UseHastePotion () {
		//
	}
	
	public boolean UseHealPotion (int heal_hp, int delay) {
		Random r = new Random () ;
		//檢查藥水霜化
		//解除絕對屏障
		//return false
		
		//heal_hp *= ((r.nextGaussian () / 5.0) + 1.0) ;
		heal_hp += heal_hp * (1 + r.nextInt (20) ) / 100;
		
		//System.out.printf ("回復%d HP\n", heal_hp) ;
		if (Pc.Hp + heal_hp > Pc.getMaxHp () ) {
			Pc.Hp = Pc.getMaxHp () ;
		} else {
			Pc.Hp += heal_hp;
		}
		
		Handle.SendPacket (new ServerMessage (77).getRaw () ) ; //覺得舒服多了
		return true;
	}
}
