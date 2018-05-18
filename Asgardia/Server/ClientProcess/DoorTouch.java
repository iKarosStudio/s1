package Asgardia.Server.ClientProcess;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.Objects.*;

public class DoorTouch
{
	PcInstance Pc;
	
	public DoorTouch (SessionHandler Handle, byte[] Data) {
		PacketReader reader = new PacketReader (Data) ;
		Pc = Handle.getPc () ;
		int x = reader.ReadWord () ;
		int y = reader.ReadWord () ;
		int uuid = reader.ReadDoubleWord () ;
		
		DoorInstance door = Pc.DoorInsight.get (uuid) ;
		System.out.printf ("door %d (%5d, %5d)\n", uuid, x, y) ;
		
		if (door.KeyId == 0) {
			if (door.isOpened) {
				door.close () ;
			} else {
				door.open () ;
			}
			byte[] detail = new DoorDetail (door).getRaw () ;
			byte[] action = new ObjAction (door.Uuid, door.StatusActionCode).getRaw () ;
			
			/* 以門為點做廣播 */
			door.BoardcastPcInsight (detail) ;
			door.BoardcastPcInsight (action) ;
		}
	}
}
