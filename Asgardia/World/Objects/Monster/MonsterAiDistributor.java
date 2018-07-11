package Asgardia.World.Objects.Monster;

import java.util.concurrent.*;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.Map.*;

/*
 * 抓地圖中的怪物AI執行需求塞到Queue
 */
public class MonsterAiDistributor implements Runnable
{
	private AsgardiaMap Map;
	private ScheduledFuture<?> t;
	private static java.util.Queue<Runnable> queue;
	
	public void run () {
		Map.Monsters.forEach ((Integer u, MonsterInstance m)->{
			//主動模式
			/*
			if (!queue.contains (m.Aikernel) ) {
				queue.offer (m.Aikernel) ;
			}*/
			
			//被動模式
			if (m.Aikernel != null) {
				/*
				 * 太久沒有被玩家觸發, 主動停止並清除AI核心節省系統資源
				 */
				if (m.Aikernel.TimeoutCounter < 60) { //500ms * 60 = 30s
					m.Aikernel.TimeoutCounter++;
				} else {
					m.Aikernel.cancel () ;
					m.Aikernel = null;
				}
				
			}
			
			if (m.isDead () ) {
				if (m.Aikernel.DeadTimeCounter < 10) { //500ms * 20 = 10s
					m.Aikernel.DeadTimeCounter++;
				} else {
					//System.out.printf ("清除%s(%d)屍體\n", m.Name, m.Uuid) ;
					m.BoardcastPcInsight (new RemoveObject (m.Uuid).getRaw () ) ;
					
					m.Aikernel.cancel () ;
					m.Aikernel = null;
					
					Map.removeMonster (m) ;
					Map.MobGenerator.removeMonster (m) ;
				}
			}
		});
	}
	
	public MonsterAiDistributor (AsgardiaMap map) {
		Map = map;
		queue = MonsterAiQueue.getInstance ().getQueue () ;
	}
	
	public void Start () {
		t = KernelThreadPool.getInstance ().ScheduleAtFixedRate (this, 100, 500) ;
	}
	
	public void Stop () {
		t.cancel (false) ;
	}
}
