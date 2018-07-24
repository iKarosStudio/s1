package Asgardia.Server;

import Asgardia.World.*;

public class MemoryMonitor extends Thread implements Runnable
{
	private static MemoryMonitor instance; 
	private static Asgardia world;
	
	public MemoryMonitor () {
		world = Asgardia.getInstance () ;
		this.setName ("System Monitor") ;
	}
	
	public void run () {
		Runtime r = Runtime.getRuntime () ;
		long memory_usage = r.totalMemory () - r.freeMemory () ;

		//System.out.printf ("mem : %2.2f MB\n", (float) mmm / (1024 * 1024) ) ;
		world.MemoryUsage = memory_usage;
	}
	
	public static MemoryMonitor getInstance () {
		if (instance == null) {
			instance = new MemoryMonitor () ;
		}
		
		return instance;
	}
}
