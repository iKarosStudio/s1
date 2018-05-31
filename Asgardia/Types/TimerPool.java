package Asgardia.Types;

import java.util.*;

public class TimerPool
{
	private Timer[] _timer;
	private int PoolSize;
	private int TimerIndex;
	
	public TimerPool (int pool_size) {
		
		for (int p = 0; p < pool_size; p++) {
			_timer[p] = new Timer () ;
		}
		PoolSize = pool_size;
		TimerIndex = PoolSize - 1;
	}
	
	public synchronized Timer getTimer () {
		if (TimerIndex >= PoolSize) {
			TimerIndex = 0;
		}
		
		return _timer[TimerIndex++];
	}
}
