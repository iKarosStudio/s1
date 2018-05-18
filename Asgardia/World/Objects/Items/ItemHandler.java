package Asgardia.World.Objects.Items;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Skills.SkillId;
import Asgardia.World.Skills.CommonSkill.*;

import static Asgardia.World.Objects.Items.ItemTypeTable.*;

import Asgardia.Server.ServerProcess.SkillGfx;

public class ItemHandler
{	
	PcInstance Pc;
	SessionHandler Handle;
	
	public ItemHandler (PcInstance p, ItemInstance i) {
		Pc = p;
		Handle = Pc.getHandler () ;
		
		if (i.MajorType == 0) {
			if (i.MinorType == TYPE_POTION) {
				UsePotion (i) ;
			} else { //未知道具 無法使用
				
			}
			
		} else if (i.MajorType == 1) {
			Pc.EquipWeapon (i.Uuid) ;
			byte[] Packet = new UpdatePcGfx (Pc.Uuid, Pc.getWeaponGfx () ).getRaw () ;
			Handle.SendPacket (Packet) ;
			Pc.BoardcastPcInsight (Packet) ;
			
		} else if (i.MajorType == 2) {
			Pc.EquipArmor (i.Uuid) ;		
		} else {
			System.out.printf ("%s 使用不明道具 %d\n", p.Name, i.Uuid) ;
		}
	}
	
	public void UsePotion (ItemInstance i) {
		byte[] gfx_packet = null;
		
		if (i.ItemId == 40013) { //綠水
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
			
		}
		
		Pc.BoardcastPcInsight (gfx_packet) ;
	}
	
	public void UseBravePotion (ItemInstance i) {
		//C_ItemUse.java : 4006
	}
	
	public void UseHastePotion () {
		//
	}
	
	public void UseHealPotion () {
		//
	}
}
