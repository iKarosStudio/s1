package Asgardia.Server;

import java.net.*;
import java.lang.Thread;
import java.util.logging.*;
import javax.swing.*;

import Asgardia.Config.Configurations;

public class MainService extends Thread
{
	Logger Log = Logger.getLogger (MainService.class.getSimpleName () ) ;
	private static MainService instance;
	private ServerSocket ServiceSocket;
	
	/*
	 * 建立連接用session
	 */
	public void run () {		
		while (true) {
			try {
				Socket Sock = ServiceSocket.accept () ;
				
				String ClientIp = Sock.getInetAddress ().getHostAddress ();
				
				/* IP捕捉
				if (ClientIp.equalsIgnoreCase ("127.0.0.1") ) {
					System.out.printf ("捕捉目標IP:%s\n", ClientIp) ;
				} 
				*/
				
				SessionHandler ClientSession = new SessionHandler (Sock) ;
				ServiceThreadPool.getInstance ().execute (ClientSession) ;
				
			} catch (SocketTimeoutException e) {
				//it's ok
			} catch (Exception e) {
				e.printStackTrace () ;
			}
		}
	}
	
	public static MainService getInstance () {
		if (instance == null) {
			instance = new MainService () ;
		}
		return instance;
	}
	
	/*
	 * 建立伺服器socket
	 */
	public MainService () {
		/*
		 * 初始化端口監聽
		 */
		System.out.printf ("Socket Port %d binding...", Configurations.SERVER_PORT) ;
		try {
			ServiceSocket = new ServerSocket (Configurations.SERVER_PORT) ;
			ServiceSocket.setSoTimeout (10000) ; //10s accept timeout
			System.out.printf ("success\n") ;
			
			this.setName ("MAIN SERVICE") ;
			
			/*
			 * 清空所有人物上線狀態
			 */
			HikariCP Db = HikariCP.getInstance () ;
			String ClearOnlineStatus = String.format ("UPDATE characters SET OnlineStatus=\'0\' WHERE OnlineStatus=\'1\';") ;
			Db.Insert (ClearOnlineStatus) ;
			
		} catch (Exception e) {
			System.out.printf ("fail\n") ;
			System.out.printf ("[!] TCP/IP Listening fault->%s\n", e.getMessage () ) ;
			JOptionPane.showMessageDialog (null,"檢查"+Configurations.SERVER_PORT+"端口是否占用\n" + e.getMessage (), "綁定監聽端口失敗", JOptionPane.ERROR_MESSAGE) ;
			e.printStackTrace () ;
			System.exit (666) ;
			
		}
	}

}
