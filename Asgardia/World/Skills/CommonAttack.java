package Asgardia.World.Skills;

import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Monster.*;

/* 
 * 一般攻擊視為一個技能實作
 * src : 攻擊發動方
 * dest : 被攻擊方
 */
public class CommonAttack
{	
	/*
	 * 玩家對怪物
	 */
	public CommonAttack (PcInstance src, MonsterInstance dest) {
		if (src.equipment.getWeapon () == null) {
			System.out.printf ("%s 使用%s對 %s(%d) 攻擊",
					src.Name,
					"空手",
					dest.Name,
					dest.Uuid
				) ;
		} else {
			System.out.printf ("%s 使用%s對 %s(%d) 攻擊",
				src.Name,
				src.equipment.getWeapon ().getName (),
				dest.Name,
				dest.Uuid
			) ;
		}
			
		
		System.out.println () ;
	}
	
	/*
	 * 怪物對玩家
	 */
	public CommonAttack (MonsterInstance src, PcInstance dest) {
	}
	
	/*
	 * 怪物(寵物, 怪物)對怪物
	 */
	
	/*
	 * 玩家對玩家
	 */
	public CommonAttack (PcInstance src, PcInstance dest) {
	}
}
