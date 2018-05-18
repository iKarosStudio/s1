/*
 * Wer mit Ungeheuern kämpft,
 * mag zusehn, dass er nicht dabei zum Ungeheuer wird.
 * Und wenn du lange in einen Abgrund blickst, 
 * blickt der Abgrund auch in dich hinein.
 */

package Asgardia;

import java.util.logging.*;
import java.util.Scanner;

import Asgardia.Gui.*;
import Asgardia.Config.*;
import Asgardia.Server.*;
import Asgardia.World.*;

public class AsgardiaMain {
	private static Logger Log = Logger.getLogger (AsgardiaMain.class.getSimpleName () ) ;
	
	static final String SLOG[] = {
		"---------------------------------------------------------------",
		"----  Wer mit Ungeheuern kämpft,                           ----",
		"----  mag zusehn, dass er nicht dabei zum Ungeheuer wird.  ----",
		"----  Und wenn du lange in einen Abgrund blickst,          ----",
		"----  blickt der Abgrund auch in dich hinein.              ----",
		"----                          <Jenseits von Gut und Böse>  ----",
		"----                                 Nietzsche, Friedrich  ----",
		"---------------------------------------------------------------",
	} ;
	
	public static void ShowSlog () {
		for (String s : SLOG) {
			System.out.println (s) ;
		}
	}
	
	/*
	 * 主進入點
	 */
	public static void main (String[] args) {
		ShowSlog () ;
		System.out.printf ("Kernel Author:%s\n", Configurations.AUTHOR) ;
		System.out.printf ("OS:%s-%s\n", System.getProperty ("os.name"), System.getProperty ("os.arch" ) ) ;
		
		/*
		 * 載入參數設定
		 */
		//ConfigurationLoader config_loader = ConfigurationLoader.getInstance () ;
		ConfigurationLoader.getInstance () ;
		KernelThreadPool.getInstance () ;
		ServiceThreadPool.getInstance () ;
		
		/*
		 * 建構資料庫連結
		 */
		HikariCP.getInstance () ;
		
		/*
		 * 建構遊戲世界
		 */
		Asgardia world = Asgardia.getInstance () ;
		world.Initialize () ;
		
		/*
		 * 建構網路接口
		 */
		MainService game_service = MainService.getInstance () ;
		game_service.start () ;
		
		/*
		 * 建構遠端管理介面
		 */
		ManageService manage_service = ManageService.getInstance () ;
		manage_service.start () ;
		
		/*
		 * GUI管理介面
		 */
		if (Configurations.USE_GUI) {
			System.out.println ("使用本地管理介面") ;
			GuiMain gui = new GuiMain (world) ;
		}
		//
		
		
		/*
		 * 賭博系統
		 */
		if (Configurations.CASINO) {
			System.out.println ("實作Casino系統") ;
		}
		
		/*
		 * 本地端控制台
		 */
		
		Scanner KeyboardReader = new Scanner (System.in) ;
		do {
			//System.out.print ("#") ;
			String Cmd = KeyboardReader.nextLine () ;
			if (Cmd.equalsIgnoreCase ("exit") ) {
				Log.info ("收到停止命令") ;
				KeyboardReader.close () ;
				System.exit (0) ;
			} else {
				try {
					//PcInstance pc = world.getPc (Cmd) ;
					//System.out.println (pc.Uuid) ;
				} catch (Exception e) {
					e.printStackTrace () ;
				}
			}
		} while (true) ;
	}
}
