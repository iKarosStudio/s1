package Asgardia.Server.ClientProcess;

import Asgardia.Server.SessionHandler;
import Asgardia.Server.ServerProcess.*;

public class ClientVersion 
{
	public ClientVersion (SessionHandler Handle, byte[] Data) {
		/*
		 * 回應伺服器版本
		 */
		new ServerVersion (Handle) ;
	}
}
