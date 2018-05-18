package Asgardia.Server;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class ServiceThreadPool
{
	private static ServiceThreadPool instance; 
	
	private Executor Pool;
	private ScheduledExecutorService ServicePool;
	
	public static ServiceThreadPool getInstance () {
		if (instance == null) {
			instance = new ServiceThreadPool () ;
		}
		return instance;
	}
	
	private ServiceThreadPool () {
		System.out.print ("Service thread pool initializing...") ;
		
		Pool = Executors.newCachedThreadPool (
				/*new ThreadFactory() {
		            public Thread newThread(Runnable r) {
		                Thread t = Executors.defaultThreadFactory().newThread(r);
		                t.setDaemon(true);
		                return t;
		            }
		        }*/
		) ;
		
		/*
		 * 數量建議值=max_users / 20
		 */
		ServicePool = Executors.newScheduledThreadPool (
				50, //Size
				new PriorityThreadFactory ("UserService", Thread.NORM_PRIORITY) //ThreadFactory
			) ;
		
		System.out.println ("success") ;
	}
	
	public void execute (Runnable Foo) {
		if (Pool != null) {
			Pool.execute (Foo) ;
			
		} else {
			Thread thread = new Thread (Foo) ;
			thread.start () ;
			//thread.setDaemon ();
		}
	}
	
	public ScheduledFuture<?> ScheduleAtFixedRate (Runnable Foo, long InitDelay, long Period) {
		return ServicePool.scheduleAtFixedRate (Foo, InitDelay, Period, TimeUnit.MILLISECONDS) ;
	}
	
	/*
public ScheduledFuture<?> pcSchedule(L1PcMonitor r, long delay) {
		try {
			if (delay <= 0) {
				_executor.execute(r);
				return null;
			}
			return _pcScheduler.schedule(r, delay, TimeUnit.MILLISECONDS);
		} catch (RejectedExecutionException e) {
			return null;
		}
	}

	public ScheduledFuture<?> pcScheduleAtFixedRate(L1PcMonitor r,
			long initialDelay, long period) {
		return _pcScheduler.scheduleAtFixedRate(r, initialDelay, period,
				TimeUnit.MILLISECONDS);
	}
	 */
	
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
			thread.setName (_group_name + "-" + _thread_number.getAndIncrement () ) ;
			thread.setPriority (_priority) ;
			return thread;
		}
		/*
		public ThreadGroup getGroup ()  {
			return _group;
		}*/
	}
}
