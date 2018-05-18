package Asgardia.Server.ClientProcess;

import Asgardia.Server.*;
import Asgardia.World.Objects.*;

public class ItemPick
{
	public ItemPick (SessionHandler Handle, byte[] Data) {
		PacketReader reader = new PacketReader (Data) ;
		PcInstance Pc = Handle.Account.ActivePc;
		
		int x = reader.ReadWord () ;
		int y = reader.ReadWord () ;
		int uuid = reader.ReadDoubleWord () ;
		int count = reader.ReadDoubleWord () ;
		
		Pc.pickItem (uuid, count, x, y) ;		
	}
}
