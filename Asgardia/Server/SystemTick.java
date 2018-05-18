package Asgardia.Server;

import java.util.*;

import Asgardia.Server.ClientProcess.*;
import Asgardia.World.Objects.*;

public class SystemTick extends TimerTask implements Runnable
{
	private final Timer SystemTickTimer = new Timer () ;
	private PcInstance pc;
	private SessionHandler Handler;

	public SystemTick (PcInstance p) {
		pc = p;
		Handler = pc.getHandler () ;
	}
	
	public void run () {
		new GameTime (Handler) ;	
	}
	
	public void Start () {
		SystemTickTimer.scheduleAtFixedRate (this, 0, 1000) ;
	}
	
	public void Stop () {
		SystemTickTimer.cancel () ;
	}
}
