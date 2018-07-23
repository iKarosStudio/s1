package Asgardia.Server.ClientProcess;

import static Asgardia.World.Objects.Items.ItemTypeTable.*;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Items.*;
import Asgardia.World.Objects.Items.Potion.*;
import Asgardia.World.Objects.Items.Scroll.*;
import Asgardia.World.Skills.*;
import Asgardia.World.Skills.CommonSkill.SkillHaste;

public class ItemUse
{
	PcInstance Pc;
	SessionHandler Handle;
	
	public ItemUse (SessionHandler Handle, byte[] Data) {
		PacketReader reader = new PacketReader (Data) ;
		this.Handle = Handle;
		Pc = Handle.getPc () ;
		int ItemUuid = reader.ReadDoubleWord () ;
		
		ItemInstance i = Pc.FindItemByUuid (ItemUuid) ;
		if (i != null) {			
			/*
			 * 使用道具
			 */
			if (i.MajorType == 0) {
				if (i.MinorType == TYPE_ARROW) {
					//
				} else if (i.MinorType == TYPE_WAND) {
					//
				} else if (i.MinorType == TYPE_LIGHT) {
					//
				} else if (i.MinorType == TYPE_GEM) {
					//
				} else if (i.MinorType == TYPE_TOTEM) {
					//
				} else if (i.MinorType == TYPE_FIRECRACKER) {
					//
				} else if (i.MinorType == TYPE_POTION) {
					new UsePotion (Pc, i) ;
					//Pc.removeItem (i.Uuid, 1) ;
				} else if (i.MinorType == TYPE_FOOD) {
					//
				} else if (i.MinorType == TYPE_SCROLL) {
					int target_uuid = reader.ReadDoubleWord () ;
					new UseScroll (Pc, i, target_uuid) ;
				} else if (i.MinorType == TYPE_QUEST_ITEM) {
					//
				} else if (i.MinorType == TYPE_SPELL_BOOK) {
					//
				} else if (i.MinorType == TYPE_PET_ITEM) {
					//
				} else if (i.MinorType == TYPE_OTHER) {
					if (i.ItemId == 40310) { //一般信件
						int MailCode = reader.ReadWord () ;
						String Receiver = reader.ReadString () ;
						byte[] Text = reader.ReadRaw () ;
						
						System.out.printf ("Mail code:%d\n", MailCode) ;
						System.out.printf ("To:%s\n", Receiver) ;
						//
					}
					
					if (i.ItemId == 40311) { //血盟信件
					}
					
					if (i.ItemId >= 40373 && i.ItemId <= 40390) { //使用地圖
						Handle.SendPacket (new MapUse (i.Uuid, i.ItemId).getRaw () ) ;
					}
				} else if (i.MinorType == TYPE_MATERIAL) {
					//
				} else if (i.MinorType == TYPE_EVENT) {
					//
				} else if (i.MinorType == TYPE_STING) {
					//
				} else { //未知道具 無法使用
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
				System.out.printf ("%s 使用不明道具%d(Major type:%d)\n", Pc.Name, i.Uuid, i.MajorType) ;
			}
		}
	}
}
