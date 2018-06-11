package Asgardia.World.Objects.Monster;

import java.util.*;
import java.util.concurrent.*;

/*
 * AI工作要求Queue
 */
public class MonsterAiQueue
{
	private static MonsterAiQueue instance;
	private static Queue<Runnable> _queue;
	
	public static MonsterAiQueue getInstance () {
		if (instance == null) {
			instance = new MonsterAiQueue () ;
		}
		return instance;
	}
	
	public MonsterAiQueue () {
		System.out.printf ("Initializing Ai task queue...") ;
		_queue = new ConcurrentLinkedQueue<Runnable> () ;
		System.out.printf ("success\n") ;
	}
	
	public synchronized Queue<Runnable> getQueue () {
		return _queue;
	}
}
