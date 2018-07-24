package Asgardia.World.Objects.RoutineTasks;

import java.util.*;
import java.util.concurrent.*;

import Asgardia.Server.KernelThreadPool;
import Asgardia.Server.SessionHandler;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.Objects.*;

public class HpMonitor extends Thread implements Runnable
{
	//private final Timer t = new Timer () ;
	ScheduledFuture s;
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
		
		//System.out.printf ("%s hp\n", Pc.Name) ;
		try {
			//if (Pc.getHp < Pc.getMaxHp () ) {
				//Pc.Hp++;
				//Handle.SendPacket (new NodeStatus (Pc).getRaw () ) ;
				Handle.SendPacket (new HpUpdate (Pc.Hp, Pc.getMaxHp () ).getRaw () ) ;
			//}
		} catch (Exception e) {
			//s.cancel (true) ;
			//e.printStackTrace () ;
			//e.printStackTrace () ;
			//System.out.printf ("HP Monitor %s\n", e.toString () ) ;
		}
	}
	
	public void Start () {
		s = KernelThreadPool.getInstance ().ScheduleAtFixedRate (this, 1000, TaskInterval) ;
		//t.scheduleAtFixedRate (this, 1000, TaskInterval) ;
	}
	
	public void Stop () {
		s.cancel (true) ;
	}
}
