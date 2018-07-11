package Asgardia.World.Objects.Items;

import java.util.*;

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
		
		/*
		 * 使用道具
		 */
		if (i.MajorType == 0) {
			if (i.MinorType == TYPE_POTION) {
				//UsePotion (i) ;
				//Pc.removeItem (i.Uuid, 1) ;
			} else if (i.MinorType == TYPE_SCROLL) {
				//UseScroll (i) ;
			}
			else { //未知道具 無法使用
				System.out.printf ("%s 使用未知種類道具%d(Type:%d)\n", Pc.Name, i.Uuid, i.MinorType) ;
			}
			
		/*
		 * 使用武器
		 */
		} else if (i.MajorType == 1) {
			Pc.EquipWeapon (i.Uuid) ;
			
			/* 更新腳色武器外型 */
			byte[] Packet = new UpdatePcGfx (Pc.Uuid, Pc.getWeaponGfx () ).getRaw () ;
			Handle.SendPacket (Packet) ;
			Pc.BoardcastPcInsight (Packet) ;
		
		/*
		 * 使用防具	
		 */
		} else if (i.MajorType == 2) {
			Pc.EquipArmor (i.Uuid) ;
			
		} else {
			System.out.printf ("%s 使用不明道具%d(Major type:%d)\n", p.Name, i.Uuid, i.MajorType) ;
		}
	}
}
