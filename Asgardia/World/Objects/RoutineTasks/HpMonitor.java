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
	
	public HpMonitor (PcInstance Pc) {
		this.Pc = Pc;
		Handle = Pc.getHandler () ;
		TaskInterval = 100;
	}
	
	public void run () {
		int div10 = 0;
		try {
			if (div10 < 0) {
				div10++;
			} else {
				if (Pc.Hp < Pc.getMaxHp () ) {
					Pc.Hp++;
					Handle.SendPacket (new NodeStatus (Pc).getRaw () ) ;
					//todo:敘述生命值藥用NodeStatus
				}
				div10 = 0;
			}
		} catch (Exception e) {
			System.out.printf ("HP Monitor %s\n", e.toString () ) ;
		}
	}
	
	public void Start () {
		t.scheduleAtFixedRate (this, 0, TaskInterval) ;
	}
	
	public void Stop () {
		t.cancel () ;
	}
}