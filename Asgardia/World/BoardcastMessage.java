package Asgardia.World;

import Asgardia.World.*;

import Asgardia.Server.ServerProcess.*;

/*
 * 遊戲內的定時廣播訊息
 */
public class BoardcastMessage extends Thread implements Runnable
{
	private static BoardcastMessage instance;
	private static Asgardia world;
	
	public void run () {
		String BoardcastMessage = String.format ("循環廣播訊息測試") ;
		//System.out.println ("[伺服器廣播]" + BoardcastMessage) ;
		world.BoardcastToAll (new SystemMessage (BoardcastMessage).getRaw () ) ;
	}
	
	public static BoardcastMessage getInstance () {
		if (instance == null) {
			instance = new BoardcastMessage () ;
		}
		return instance;
	}
	
	public BoardcastMessage () {
		world = Asgardia.getInstance () ;
	}
}
