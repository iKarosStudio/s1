package Asgardia.World.Objects.Monster;

import java.util.*;
import java.util.concurrent.*;

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
					//System.out.printf ("queue size:%d\n", q.size () ) ;
					Pool.execute (q.poll () ) ;
				} else {
					//System.out.printf ("ai queue empty") ;
					Thread.sleep (5) ;
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
		//Pool = Executors.newCachedThreadPool ();
		//Pool = Executors.newFixedThreadPool (2500) ;
		Pool = Executors.newWorkStealingPool (2000) ;
		
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
}
