package Asgardia.Server.ClientProcess;

import Asgardia.Server.*;
import Asgardia.World.Objects.*;

public class ExitGame
{
	public ExitGame (SessionHandler Handle, byte[] Data) {
		if (Handle.Account != null && 
			Handle.Account.ActivePc != null) {
			PcInstance Pc = Handle.Account.ActivePc;
			try {
				if (Handle.Account != null && Handle.Account.ActivePc != null) {
					Pc.exit = true;
					Pc.Offline () ;
					/* 20180411
					 * 正常離開遊戲, 要多加一個旗標判斷
					 * 否則在sessionhandler exception捕捉斷線會kick()重複執行
					 */
				}
			} catch (Exception e) {
				e.printStackTrace () ;
			}
		}	
	}
}
