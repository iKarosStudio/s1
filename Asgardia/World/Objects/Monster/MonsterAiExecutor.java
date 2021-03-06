package Asgardia.World.Objects.Monster;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import Asgardia.Server.*;

public class MonsterAiExecutor implements Runnable
{
	private static MonsterAiExecutor instance;
	private static MonsterAiQueue AiQueue;
	private ExecutorService Pool;
	
	private boolean isExecuting = true;
	
	public void run () {
		Queue<Runnable> q = AiQueue.getQueue () ;
		
		while (isExecuting) {
			try {
				if (!q.isEmpty () ) {
					/*
					 * ai queue not empty
					 */
					//System.out.printf ("queue size:%d\n", q.size () ) ;
					Pool.execute (q.poll () ) ;
					
				} else {
					/*
					 * ai queue empty
					 */
					Thread.sleep (16) ;
				}
				
			} catch (Exception e) {
				e.printStackTrace () ;
			}
		}
	}
	
	public static MonsterAiExecutor getInstance () {
		if (instance == null) {
			instance = new MonsterAiExecutor () ;
		}
		return instance;
	}
	
	public MonsterAiExecutor () {
		Pool = Executors.newCachedThreadPool (new PriorityThreadFactory ("Monster AI", Thread.NORM_PRIORITY) ) ;
		//Pool = Executors.newFixedThreadPool (2500) ;
		//Pool = Executors.newWorkStealingPool (2000) ;
		
		AiQueue = MonsterAiQueue.getInstance () ;
		
		KernelThreadPool.getInstance ().execute (this) ;
	}
	
	public boolean isRunning () {
		return isExecuting;
	}
	
	public void stopRunning () {
		isExecuting = false;
	}
	
	public void startRunning () {
		isExecuting = true;
		KernelThreadPool.getInstance ().execute (this) ;
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
