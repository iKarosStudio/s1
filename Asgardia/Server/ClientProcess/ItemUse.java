package Asgardia.Server.ClientProcess;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Items.*;
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
			new ItemHandler (Pc, i) ;
		}
	}
}
