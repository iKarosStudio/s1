package Asgardia.Server.ServerProcess;

import Asgardia.Server.PacketBuilder;
import Asgardia.Server.SessionHandler;
import Asgardia.Server.Opcodes.ServerOpcodes;

/*
 * 登入公告訊息
 */
public class LoginAnnounce
{
	
	public LoginAnnounce (SessionHandler Handle) {
		PacketBuilder Builder = new PacketBuilder () ;
		String DefaultMsg = String.format ("帳號:%s\n密碼:%s\n登入IP:%s\n", Handle.Account.UserAccount, Handle.Account.UserPassword, Handle.getIP () ) ;
		
		Builder.WriteByte (ServerOpcodes.LOGIN_WELCOME_MSG) ;
		Builder.WriteString (DefaultMsg) ;
		Handle.SendPacket (Builder.GetPacket () ) ;
	}
	
	public LoginAnnounce (SessionHandler Handle, String Msg) {
		PacketBuilder Builder = new PacketBuilder () ;
		
		Builder.WriteByte (ServerOpcodes.LOGIN_WELCOME_MSG) ;
		Builder.WriteString (Msg) ;
		Handle.SendPacket (Builder.GetPacket () ) ;
	}
}
