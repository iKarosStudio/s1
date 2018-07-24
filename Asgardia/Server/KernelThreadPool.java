package Asgardia.Server;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class KernelThreadPool
{
	private static KernelThreadPool instance;
	
	private ExecutorService Pool;
	private ScheduledExecutorService KernelPool;
	
	public static KernelThreadPool getInstance () {
		if (instance == null) {
			instance = new KernelThreadPool () ;
		}
		return instance;
	}
	
	private KernelThreadPool () {
		System.out.print ("Kernel  thread pool initializing...") ;
		
		Pool = Executors.newCachedThreadPool () ;
		
		KernelPool = Executors.newScheduledThreadPool (
			8, //Size
			new PriorityThreadFactory ("KernelService", Thread.NORM_PRIORITY) //ThreadFactory
		) ;
		System.out.println ("success") ;
	}
	
	public void execute (Runnable Foo) {
		if (Pool != null) {
			Pool.execute (Foo) ;
			
		} else {
			System.out.println ("???") ;
			Thread thread = new Thread (Foo) ;
			thread.start () ;
		}
	}
	
	
	public ScheduledFuture<?> ScheduleAtFixedRate (Runnable Foo, long InitDelay, long Period) {
		return KernelPool.scheduleAtFixedRate (Foo, InitDelay, Period, TimeUnit.MILLISECONDS) ;
	}
	
	private class PriorityThreadFactory implements ThreadFactory {
		private final int _priority;
		private final String _group_name;
		private final AtomicInteger _thread_number = new AtomicInteger (1) ;
		private final ThreadGroup _group;
		
		public PriorityThreadFactory (String name, int priority) {
			_priority = priority;
			_group_name = name;
			_group = new ThreadGroup (_group_name) ;
		}
		
		public Thread newThread (Runnable Foo) {
			Thread thread = new Thread (_group, Foo) ;
			thread.setName (_group_name + "-" + _thread_number.getAndIncrement () + "->" + Foo.toString () ) ;
			thread.setPriority (_priority) ;
			return thread;
		}
		/*
		public ThreadGroup getGroup ()  {
			return _group;
		}*/
	}
}
