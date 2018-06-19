package Asgardia.World.Objects.Dynamic;

import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Template.*;

/*
 * 玩家腳色, NPC, 怪物等等
 * 應該有角色素質的物件
 */
public class DynamicObject extends ObjectInstance
{
	public int Level = 1;
	public int Status = 0; //每bit表示腳色debuff狀態
	
	/* 加速狀態: 0:一般 1:加速 2:緩速 */
	public int MoveSpeed = 0;
	
	/* 勇水/精餅狀態: 0:一般 1:加速 */
	public int BraveSpeed = 0; 
	
	public int Gfx; /* 使用外型編號 */
	public int TempGfx;
	
	public String Name = null;
	public String Title = null;
	public CombatStatus BasicParameter;
	public CombatStatus SkillParameter;
	
	public int Lawful;
	
	private boolean isDead = false;
	public int Hp = 1;
	public int Mp = 1;
	
	public boolean isDead () {
		return isDead;
	}
	
	public synchronized void setDead (boolean d) {
		isDead = d;
	}
}
