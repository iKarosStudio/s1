package Asgardia.World.Objects.RoutineTasks;

import java.util.*;

import Asgardia.Server.SessionHandler;
import Asgardia.World.Objects.*;

public class MpMonitor extends TimerTask implements Runnable
{
	private final Timer t = new Timer () ;
	private int TaskInterval = 0;
	private PcInstance Pc;
	private SessionHandler Handle;
	
	public MpMonitor (PcInstance Pc) {
		this.Pc = Pc;
		Handle = Pc.getHandler () ;
		TaskInterval = 100;
	}
	
	public void run () {
		try {
			//
		} catch (Exception e) {
			System.out.printf ("MP Monitor %s\n", e.toString () ) ;
		}
	}
	
	public void Start () {
		t.scheduleAtFixedRate (this, 0, TaskInterval) ;
	}
	
	public void Stop () {
		t.cancel () ;
	}
}
