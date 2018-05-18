package Asgardia.Server.ClientProcess;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.Objects.*;

public class ChangeHeading
{
	public ChangeHeading (SessionHandler Handle, byte[] Data) {
		PacketReader Reader = new PacketReader (Data) ;
		PcInstance Pc = Handle.Account.ActivePc;
		int Heading = Reader.ReadByte () ;
		Pc.location.Heading = Heading;
		
		//Pc.BoardcastPcInsightExceptSelf (new NodeHeading (Pc.Uuid, Pc.location.Heading).getRaw () ) ;
		Pc.BoardcastPcInsight (new NodeHeading (Pc.Uuid, Pc.location.Heading).getRaw () ) ;
	}
}
