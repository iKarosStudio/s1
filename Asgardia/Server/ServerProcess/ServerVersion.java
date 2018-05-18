package Asgardia.Server.ServerProcess;

import Asgardia.Server.SessionHandler;
import Asgardia.Server.ServerTime;
import Asgardia.Server.PacketBuilder;
import Asgardia.Server.Opcodes.ServerOpcodes;

public class ServerVersion
{
	public ServerVersion (SessionHandler Handle) {
		PacketBuilder Builder = new PacketBuilder () ;
		ServerTime t = ServerTime.getInstance () ;
		
		Builder.WriteByte (ServerOpcodes.SERVER_VERSION) ;
		Builder.WriteByte (0x00) ;
		Builder.WriteByte (0x02) ;
		Builder.WriteDoubleWord (0x00009D7C) ;
		Builder.WriteDoubleWord (0x0000791A) ;
		Builder.WriteDoubleWord (0x0000791A) ;
		Builder.WriteDoubleWord (0x00009DD1) ;
		Builder.WriteDoubleWord (t.getTime () ) ; //time
		Builder.WriteByte (0x00) ;
		Builder.WriteByte (0x00) ;
		Builder.WriteByte (0x03) ; //3:繁體中文
		
		Handle.SendPacket (Builder.GetPacket () ) ;
	}
}
