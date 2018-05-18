package Asgardia.Server.ClientProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;

public class GameTime
{
	public GameTime (SessionHandler Handle) {
		PacketBuilder Builder = new PacketBuilder () ;
		ServerTime St = ServerTime.getInstance ();
		
		Builder.WriteByte (ServerOpcodes.SYS_TICK) ;
		Builder.WriteDoubleWord (St.getTime () ) ; //get sys tick time
		Handle.SendPacket (Builder.GetPacket () ) ;
	}
}
