package Asgardia.Server.ClientProcess;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.Objects.*;

public class Attack
{
	public Attack (SessionHandler Handle, byte[] Data) {
		PacketReader reader = new PacketReader (Data) ;
		PcInstance Pc = Handle.getPc () ;
		
		int TargetUuid = reader.ReadDoubleWord () ;
		int TargetX = reader.ReadWord () ;
		int TargetY = reader.ReadWord () ;
		
		if (Pc.getWeightInScale30 () > 24) {
			Handle.SendPacket (new ServerMessage (110).getRaw () ) ;
			return;
		}
		
		Pc.location.Heading = Pc.getDirection (TargetX, TargetY) ;
		Pc.Attack (TargetUuid) ;	
	}
}
