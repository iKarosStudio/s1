package Asgardia.Server;

public class MonsterGenerator extends Thread implements Runnable
{
	public int MapId;
	
	public void run () {
		System.out.printf ("Map[%d] MobGen running...\n", MapId) ;
	}
	
	public MonsterGenerator (int map_id) {
		MapId = map_id;
		System.out.printf ("Monster Generator Initialize for Map:%d\n", MapId) ;
	}
}
