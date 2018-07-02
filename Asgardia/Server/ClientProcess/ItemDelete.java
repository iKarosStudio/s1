package Asgardia.Server.ClientProcess;

import Asgardia.Server.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Items.*;

public class ItemDelete
{
	public ItemDelete (SessionHandler Handle, byte[] Data) {
		PacketReader reader = new PacketReader (Data) ;
		PcInstance Pc = Handle.Account.ActivePc;
		
		int uuid = reader.ReadDoubleWord () ;
		
		System.out.printf ("delete item:%d\n", uuid) ;
		
		ItemInstance i = Pc.FindItemByUuid (uuid) ;
		if (i != null) {
			Pc.removeItem (i) ;
		}
	}
}
