package Asgardia.World.Skills;

import java.util.*;

import Asgardia.Server.ServerProcess.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Monster.*;
import Asgardia.World.Objects.Items.*;

import static Asgardia.World.Objects.Items.ItemTypeTable.*;

/* 
 * 一般攻擊視為一個技能實作
 * src : 攻擊發動方
 * dest : 被攻擊方
 */
public class CommonAttack
{	
	Random rnd = new Random () ;
	
	/*
	 * 玩家對怪物
	 */
	public CommonAttack (PcInstance src, MonsterInstance dest) {
		int dmg = 0;
		
		if (dest.isDead () ) {
			return;
		}
		
		
		/*
		if (src.equipment.getWeapon () == null) {
			System.out.printf ("%s 使用%s對 %s(%d) 攻擊", src.Name, "空手", dest.Name, dest.Uuid) ;
			
		} else {
			System.out.printf ("%s 使用%s對 %s(%d) 攻擊", src.Name, src.equipment.getWeapon ().getName (), dest.Name, dest.Uuid) ;
		}
		*/
		
		/*
		 * 命中檢查
		 */
		if (isPc2NpcHit (src, dest) ) {
			/*
			 * 傷害計算
			 */
			dmg = CalcPc2NpcDmg (src, dest) ;
			dest.ToggleHateList (src, dmg) ;
			
			System.out.printf ("命中! 造成%3d傷害\n", dmg) ;
			dest.TakeDamage (dmg) ;
			
			/*
			 * 設定反擊
			 */
			if (dest.TargetPc == null) {
				dest.Aikernel.cancel () ;
				dest.ActionStatus = 2;
				dest.TargetPc = src;
			}
			
			/*
			 * 挨打動作
			 */
			dest.BoardcastPcInsight (new NodeAction (2, dest.Uuid, dest.location.Heading).getRaw () ) ;
			
			
			if (dest.isDead () ) {
				byte[] die = new NodeAction (8, dest.Uuid, dest.location.Heading).getRaw () ;
				
				//轉移經驗值與道具
				dest.TransferExp (dest.TargetPc) ;
				dest.TransferItems () ;
				
				dest.BoardcastPcInsight (die) ;
				dest.setDead (true) ;
				dest.ActionStatus = 3;
			}
				
			
		} else {
			System.out.printf ("未命中!\n") ;
		}
	}
	
	/*
	 * 怪物對玩家
	 */
	public CommonAttack (MonsterInstance src, PcInstance dest) {
		int dmg = 0;
		
		if (dest.isDead () ) {
			return;
		}
		
		if (isNpc2PcHit (src, dest) ) {
			dmg = CalcNpc2PcDmg (src, dest) ;
			System.out.printf ("命中! 造成%d傷害\n", dmg) ;
			
			dest.TakeDamage (dmg) ;
			byte[] action_code = new NodeAction (2, dest.Uuid, dest.location.Heading).getRaw () ;
			dest.getHandler ().SendPacket (action_code) ;
			dest.BoardcastPcInsight (action_code) ;
		} else {
			System.out.printf ("未命中!\n") ;
		}
	}
	
	/*
	 * 怪物(寵物, 怪物)對怪物
	 */
	public CommonAttack (PetInstance src, MonsterInstance dest) {
	}
	
	/*
	 * 玩家對玩家
	 */
	public CommonAttack (PcInstance src, PcInstance dest) {
	}
	
	
	
	
	/*
	 * 算PC2NPC命中率
	 */
	public boolean isPc2NpcHit (PcInstance src, MonsterInstance dest) {
		
		int SrcStr = src.getStr () ;
		int SrcDex = src.getDex () ;
		int HitRate = src.Level;
		
		int WeaponEnchant = 0;
		int WeaponType = 0;
		ItemInstance Weapon = src.equipment.getWeapon () ;
		if (Weapon != null) {
			WeaponType = src.equipment.getWeapon ().MinorType;
			WeaponEnchant = src.equipment.getWeapon ().Enchant;
		}
		
		if (SrcStr > 39) {
			HitRate += STR_HIT_OFFSET[39];
		} else {
			HitRate += STR_HIT_OFFSET[SrcStr];
		}
		
		if (SrcDex > 39) {
			HitRate += DEX_HIT_OFFSET[39];
		} else {
			HitRate += DEX_HIT_OFFSET[SrcDex];
		}
		
		if (Weapon != null) {
			HitRate += Weapon.HitModifier;
			HitRate += (WeaponEnchant >>> 1) ;
		}
		
		if ((WeaponType != WEAPON_TYPE_BOW) && (WeaponType != WEAPON_TYPE_GAUNTLET)) {
			HitRate += src.getHitModify () ;
		} else {
			HitRate += src.getBowHitModify () ;
		}
		
		HitRate *= 5;
		HitRate += (dest.BasicParameter.Ac * 5) ;
		
		if (HitRate > 95) {
			HitRate = 95;
		}
		
		if (HitRate < 5) {
			HitRate = 5;
		}
		
		int Rate = rnd.nextInt (100) + 1;
		return Rate < HitRate;
	}
	
	/*
	 * 算NPC2PC命中率
	 */
	public boolean isNpc2PcHit (MonsterInstance src, PcInstance dest) {
		int HitRate = 0;
		
		HitRate = src.Level * 10; // * 2 * 5
		HitRate -= dest.getAc () * 5;
		
		/*
		 * 寵物命中修正*2
		 */
		
		/*
		 * Npc Hit rate修正
		 */
		
		/*
		 * 有暗影閃避(Uncanny dodge)命中-20
		 */
		
		if (HitRate < src.Level) {
			HitRate = src.Level;
		}
		
		if (HitRate < 5) {
			HitRate = 5;
		}
		
		if (HitRate > 95) {
			HitRate = 95;
		}
		
		int p = rnd.nextInt (100) + 1;
		System.out.printf ("Hit: %d/%d\n", HitRate, p) ;
		return p < HitRate;
	}
	
	public int CalcPc2NpcDmg (PcInstance src, MonsterInstance dest) {
		ItemInstance Weapon = src.equipment.getWeapon () ;
		int WeaponMaxDmg = 0;
		int WeaponDmg = 0;
		int Str = src.getStr () ;
		int Dex = src.getDex () ;
		
		/*
		 * 有傷害無效化技能先行處理 return 0;
		 */
		
		
		if (Weapon != null) {
			if (dest.Size == 0) { //小型怪
				WeaponMaxDmg = Weapon.DmgSmall;
			} else { //大型怪
				WeaponMaxDmg = Weapon.DmgLarge;
			}
			
			/*
			 * 套用力量/敏捷加乘效果
			 */
			if ((Weapon.MinorType == WEAPON_TYPE_ARROW) || (Weapon.MinorType == WEAPON_TYPE_GAUNTLET) ) {
				//遠程武器
				if (Dex > 35) {
					WeaponMaxDmg += DEX_DMG_OFFSET[35];
				} else {
					WeaponMaxDmg += DEX_DMG_OFFSET[Dex];
				}
				
				if (src.isElf () ) {
					WeaponMaxDmg += src.Level / 10;
				}
				
				if (src.isDarkElf () ) {
					//
				}
				
			} else {
				//進戰武器
				if (Str > 50) {
					WeaponMaxDmg += STR_DMG_OFFSET[50];
				} else {
					WeaponMaxDmg += STR_DMG_OFFSET[Str];
				}
				
				/*
				 * 套用職業加乘效果
				 */
				if (src.isKnight () ) {
					WeaponMaxDmg += src.Level / 10;
				}
				
				if (src.isDarkElf () ) {
					//
				}
			}
					
			/*
			 * 套用材質加乘效果
			 */
			
			/*
			 * 雙刀1/3機率打最大傷害 特效#3671
			 */
			
			/*
			 * 雙刀1/4機率打出雙倍傷害 特效#3398
			 */
			
			/*
			 * 雙刀/雙爪 有雙重破壞(Double Brake)時1/3機率打出雙倍傷害
			 */
			
			/*
			 * 有擬似魔法武器+2
			 */
			
			/*
			 * 有烈焰之魂(Soul of Flame)近戰武器 取最高傷害
			 */
			
			WeaponDmg = 1 + rnd.nextInt (WeaponMaxDmg) + Weapon.Enchant;
			
		} else {
			WeaponDmg = 1;
		}
		
		
		return WeaponDmg;
	}
	
	public int CalcNpc2PcDmg (MonsterInstance src, PcInstance dest) {
		int dmg = 5 * (src.Level / 10) ;
		dmg += rnd.nextInt (src.Level - dmg + 5) ;
		dmg += src.BasicParameter.Str;
		dmg = dmg >>> 1;
		dmg++;
		
		/*
		 * 職業傷害減免
		 */
		int ac = Math.min (0, dest.getAc () ) ;
		if (dest.isRoyal () ) {
			dmg += rnd.nextInt (ac / 3 + 1) ;
		} else if (dest.isKnight () ) {
			dmg += rnd.nextInt (ac / 2 + 1) ;
		} else if (dest.isElf () ) {
			dmg += rnd.nextInt (ac / 4 + 1) ;
		} else if (dest.isMage () ) {
			dmg += rnd.nextInt (ac / 5 + 1) ;
		} else if (dest.isDarkElf () ) {
			dmg += rnd.nextInt (ac / 4 + 1) ;
		}
		
		/*
		 * 壞物 傷害/2
		 */
		
		/*
		 * 反擊屏障
		 */
		
		return dmg;
	}
	
	public int CalcPc2PcDmg (PcInstance src, PcInstance dest) {
		return 0;
	}
	
	/*
	 * 算迴避率(Evation Rate)
	 */
	public int CalcER (int t, int level, int dex) {
		int d = 0;
		
		/*
		 * 不同職業的等級修正係數
		 */
		switch (t) {
		case 0: d = 8; break; //Royal
		case 1: d = 4; break; //knight
		case 2: d = 6; break; //elf
		case 3: d = 10;break; //mage
		case 4: d = 5; break; //darkelf
		}
		
		return (level / d) + (dex >>> 2) - 4;
	}
	
	
	private static final int[] STR_HIT_OFFSET = {
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, //0~9
			 0,  0,  1,  1,  2,  2,  3,  3,  4,  4, //10-19
			 4,  5,  5,  5,  6,  6,  6,  7,  7,  7, //20-29
			 8,  8,  8,  9,  9,  9, 10, 10, 10, 10, //30-39
			 10} ; //40

	private static final int[] DEX_HIT_OFFSET = {
			-1, -1, -1, -1, -1, -1, -1, -1, -1,  0, //0~9
			 0,  1,  1,  2,  2,  3,  3,  4,  4,  5, //10~19
			 6,  7,  8,  9, 10, 11, 12, 13, 14, 15, //20~29
			16, 17, 18, 19, 20, 21, 22, 23, 24, 25, //30~39
			26}; //40
	
	private static final int[] STR_DMG_OFFSET = {
			-2, -2, -2, -2, -2, -2, -2, -2, -2, -1, //0~9
			-1,  0,  0,  1,  1,  2,  2,  3,  3,  4, //10~19
			 4,  5,  5,  6,  6,  6,  7,  7,  7,  8, //20~29
			 8,  9,  9, 10, 11, 12, 12, 12, 12, 13, //30~39
			13, 13, 13, 14, 14, 14, 14, 15, 15, 16, //40~49
			17} ;
	
	private static final int[] DEX_DMG_OFFSET = {
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //0~9
			0, 0, 0, 0, 1, 2, 3, 4, 4, 4, //10~19
			4, 5, 5, 5, 6, 6, 6, 7, 7, 7, //20~29
			8, 8, 8, 9, 9, 10} ; //30~35
}
