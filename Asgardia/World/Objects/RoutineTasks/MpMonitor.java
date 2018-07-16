package Asgardia.World.Objects.RoutineTasks;

import java.util.*;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.Objects.*;

public class MpMonitor extends TimerTask implements Runnable
{
	private final Timer t = new Timer () ;
	private int TaskInterval = 0;
	private PcInstance Pc;
	private SessionHandler Handle;
	
	private int Status = 0;
	
	public MpMonitor (PcInstance Pc) {
		this.Pc = Pc;
		Handle = Pc.getHandler () ;
		TaskInterval = 1000;
	}
	
	public void run () {
		try {
			Handle.SendPacket (new MpUpdate (Pc.Mp, Pc.getMaxMp () ).getRaw () ) ;
		} catch (Exception e) {
			System.out.printf ("MP Monitor %s\n", e.toString () ) ;
		}
	}
	
	public void Start () {
		t.scheduleAtFixedRate (this, 1000, TaskInterval) ;
	}
	
	public void Stop () {
		t.cancel () ;
	}
}
