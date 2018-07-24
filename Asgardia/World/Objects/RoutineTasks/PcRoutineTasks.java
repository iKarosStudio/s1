package Asgardia.World.Objects.RoutineTasks;

import java.util.*;
import java.util.concurrent.*;

import Asgardia.Server.*;
import Asgardia.World.Objects.*;

public class PcRoutineTasks extends Thread implements Runnable
{
	//private final Timer t = new Timer () ;
	ScheduledFuture s;
	private int TaskInterval = 0;
	private SessionHandler Handle;
	private PcInstance Pc;
	
	
	public PcRoutineTasks (PcInstance Pc) {
		Handle = Pc.getHandler () ;
		this.Pc = Pc;
		TaskInterval = 500;
	}
	
	public void run () {
		try {
			//System.out.println ("pc routine tasks..") ;
		} catch (Exception e) {
			System.out.printf ("%s Routine tasks : %s\n", Pc.Name, e.toString () ) ;
		}
	}
	
	public void Start () {
		//t.scheduleAtFixedRate (this, 0, TaskInterval) ;
		s = KernelThreadPool.getInstance ().ScheduleAtFixedRate (this, 0, TaskInterval) ;
	}
	
	public void Stop () {
		//t.cancel () ;
		s.cancel (true) ;
	}
}
