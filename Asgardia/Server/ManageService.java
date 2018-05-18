package Asgardia.Server;

import java.net.*;
import java.lang.Thread;
import java.lang.management.*;
import com.sun.management.OperatingSystemMXBean;

import Asgardia.Config.*;
import Asgardia.World.*;

public class ManageService extends Thread
{
	private static ManageService instance;
	private ServerSocket ServiceSocket;
	
	Asgardia World;
	
	ThreadMXBean mana = ManagementFactory.getThreadMXBean();
	OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean () ;
	
	/*
	 * 建立連接用session
	 */
	public void run () {		
		while (true) {
			/*
			String t = String.format ("Cpu:%2.4f%% 執行緒:%d 使用記憶體:%1.1f kB",
				osmb.getProcessCpuLoad (),
				mana.getThreadCount (),
				(float) World.UsedMemory / (1024)
			) ;*/
			try {
				Socket Sock = ServiceSocket.accept () ;
				//String ClientIp = Sock.getInetAddress ().getHostAddress ();
				
				/*
				 * 開始管理服務程式
				 */
				
			} catch (SocketTimeoutException e) {
				//it's ok
				//System.out.println (t) ;
			} catch (Exception e) {e.printStackTrace () ;}
		}
	}
	
	public static ManageService getInstance () {
		if (instance == null) {
			instance = new ManageService () ;
		}
		return instance;
	}
	
	public ManageService () {
		World = Asgardia.getInstance () ;
		
		/*
		 * 初始化端口監聽
		 */
		System.out.printf ("Management Port %d binding...", 2001) ;
		try {
			ServiceSocket = new ServerSocket (2001) ;
			ServiceSocket.setSoTimeout (1000) ; //10s accept timeout
			System.out.printf ("success\n") ;
			
			this.setName ("MANAGEMENT SERVICE");
			
		} catch (Exception e) {
			System.out.printf ("fail\n") ;
			System.out.printf ("[!] TCP/IP Listening fault->%s\n", e.getMessage () ) ;
			e.printStackTrace () ;
			System.exit (666) ;
			
		}
	}
}
