package Asgardia.World.Objects;

import java.util.concurrent.*;

import Asgardia.Types.*;
import Asgardia.World.Objects.Items.*;
import Asgardia.World.Objects.Dynamic.*;
import Asgardia.World.Objects.Template.*;

/*
 * 怪物實體
 */
public class MonsterInstance extends DynamicObject
{
	public String NameId; 
	
	public int ActionStatus = 0; /* 0:Idle 1:Searching 2:Dead */
	
	public int GroupId;
	
	public Location location;
	public int RandomX;
	public int RandomY;
	public int MovementDistance;
	public int Rest; /* 復活次數 */
	
	
	/*
	 * 怪物道具
	 */
	public ConcurrentHashMap<Integer, ItemInstance> Items = null;
	
	/*
	 * 怪物經驗值
	 */
	public int Exp;
	
	/*
	 * 仇恨清單
	 *  <K, V> = <仇恨對象UUID, 打擊次數>
	 */
	public ConcurrentHashMap<Integer, Integer> HateList;
	
	public MonsterInstance (NpcTemplate n, Location loc) {
		Uuid = n.Uuid;
		Gfx = n.Gfx;
		Name = n.Name;
		NameId = n.NameId;
		
		Level = n.Level;
		Exp = n.Exp;
		
		location = loc;
		
		BasicParameter = new QualityParameters () ;
		BasicParameter.MaxHp = n.BasicParameter.MaxHp;
		BasicParameter.MaxMp = n.BasicParameter.MaxMp;
		BasicParameter.Ac = n.BasicParameter.Ac;
		
		Items = new ConcurrentHashMap<Integer, ItemInstance> () ;
	}
		
}
