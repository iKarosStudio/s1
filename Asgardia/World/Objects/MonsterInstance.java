package Asgardia.World.Objects;

import Asgardia.World.Objects.Dynamic.*;
import Asgardia.World.Objects.Template.*;

/*
 * 怪物實體
 */
public class MonsterInstance extends DynamicObject
{
	//
	//
	//
	public int ActionStatus = 0; /* Idle, Searching, Dead */
	
	public int GroupId;
	
	public int RandomX;
	public int RandomY;
	public int MovementDistance;
	public int Rest; /* 復活次數 */
	
	
	/*
	 * 怪物特有
	 */
	//持有道具
	//持有經驗值
	//仇恨清單 (玩家, 寵物)
	public MonsterInstance (NpcTemplate n) {
		//
	}
	
	
}
