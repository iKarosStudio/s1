package Asgardia.Types;

import java.util.*;

public class TimerPool
{
	private Timer[] _timer;
	private int PoolSize;
	private int TimerIndex = 0;
	
	public TimerPool (int pool_size) {
		_timer = new Timer[pool_size];
		for (int p = 0; p < pool_size; p++) {
			_timer[p] = new Timer () ;
		}
		
		PoolSize = pool_size;
	}
	
	public synchronized Timer getTimer () {
		if (TimerIndex >= PoolSize) {
			TimerIndex = 0;
		}
		
		return _timer[TimerIndex++];
	}
	
	public synchronized Timer getTimer (int t) {
		return _timer[t];
	}
}
