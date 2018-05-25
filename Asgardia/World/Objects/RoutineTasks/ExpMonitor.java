package Asgardia.World.Objects.RoutineTasks;

import java.util.*;

import Asgardia.Server.SessionHandler;
import Asgardia.World.Objects.PcInstance;

public class ExpMonitor extends TimerTask implements Runnable
{
	private final Timer t = new Timer () ;
	private PcInstance Pc;
	private SessionHandler Handle;

	public ExpMonitor (PcInstance Pc) {
		this.Pc = Pc;
		Handle = Pc.getHandler () ;
	}
	
	public void run () {
		try {
			System.out.printf ("pc exp monitor\n") ;
		} catch (Exception e) {
			System.out.printf ("%s ExpMonitor:%s\n", Pc.Name, e.toString () ) ;
		}
	}
	
	public void Start () {
		t.scheduleAtFixedRate (this, 0, 200) ;
	}
	
	public void Stop () {
		t.cancel () ;
	}
}
