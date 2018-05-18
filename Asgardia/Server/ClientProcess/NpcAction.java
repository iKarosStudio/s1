package Asgardia.Server.ClientProcess;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.*;
import Asgardia.World.Npc.*;
import Asgardia.World.Objects.*;

public class NpcAction
{
	public NpcAction (SessionHandler Handle, byte[] Data) {
		PcInstance pc = Handle.Account.ActivePc;
		PacketReader reader = new PacketReader (Data) ;
		
		int NpcId = reader.ReadDoubleWord () ;
		String ActionCode = reader.ReadString () ;
		
		NpcActionCodeHandler CodeHandler = new NpcActionCodeHandler (Handle, NpcId, ActionCode) ;
		
	}
}
