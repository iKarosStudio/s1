package Asgardia.Server.ClientProcess;

import static Asgardia.World.Objects.Items.ItemTypeTable.TYPE_POTION;
import static Asgardia.World.Objects.Items.ItemTypeTable.TYPE_SCROLL;

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
			//廢棄機制
			//new ItemHandler (Pc, i) ;
			
			/*
			 * 使用道具
			 */
			if (i.MajorType == 0) {
				if (i.MinorType == TYPE_POTION) {
					new UsePotion (Pc, i) ;
					//Pc.removeItem (i.Uuid, 1) ;
				} else if (i.MinorType == TYPE_SCROLL) {
					int target_uuid = reader.ReadDoubleWord () ;
					new UseScroll (Pc, i, target_uuid) ;
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
				System.out.printf ("%s 使用不明道具%d(Major type:%d)\n", Pc.Name, i.Uuid, i.MajorType) ;
			}
		}
	}
}
