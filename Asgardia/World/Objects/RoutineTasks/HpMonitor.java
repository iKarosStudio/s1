package Asgardia.World.Objects.RoutineTasks;

import java.util.*;


import Asgardia.Server.SessionHandler;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.Objects.*;

public class HpMonitor extends TimerTask implements Runnable
{
	private final Timer t = new Timer () ;
	private int TaskInterval = 0;
	private PcInstance Pc;
	private SessionHandler Handle;
	
	private int Status = 0;
	
	public HpMonitor (PcInstance Pc) {
		this.Pc = Pc;
		Handle = Pc.getHandler () ;
		TaskInterval = 1000;
	}
	
	public void run () {
		
		int div10 = 0;
		try {
			//if (Pc.getHp < Pc.getMaxHp () ) {
				//Pc.Hp++;
				//Handle.SendPacket (new NodeStatus (Pc).getRaw () ) ;
				Handle.SendPacket (new HpUpdate (Pc.Hp, Pc.getMaxHp () ).getRaw () ) ;
			//}
		} catch (Exception e) {
			Stop () ;
			//e.printStackTrace () ;
			//System.out.printf ("HP Monitor %s\n", e.toString () ) ;
		}
	}
	
	public void Start () {
		t.scheduleAtFixedRate (this, 1000, TaskInterval) ;
	}
	
	public void Stop () {
		t.cancel () ;
	}
}
