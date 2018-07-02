package Asgardia.Server.Utility;


import java.util.Random;

import Asgardia.Config.*;
import Asgardia.Types.*;
import Asgardia.World.*;

/*
 * 雜項計算功能
 */
public class Utility
{
	private static Random random = new Random (System.currentTimeMillis () ) ;
	
	
	public static String getItemNameById (int item_id) {
		if (CacheData.ItemCache.containsKey (item_id) ) {
			return CacheData.ItemCache.get (item_id).Name;
		} else if (CacheData.WeaponCache.containsKey (item_id) ) {
			return CacheData.WeaponCache.get (item_id).Name;
		} else if (CacheData.ArmorCache.containsKey (item_id) ) {
			return CacheData.ArmorCache.get (item_id).Name;
		}
		return null;
	}
	
	/*
	 * 給定起點座標及方向
	 * 回傳該方向得座標
	 */
	public static Location getNextLocation (int x, int y, int heading) {
		int px = x;
		int py = y;
		
		switch (heading) {
		case 0: py--; break;
		case 1: px++; py--; break;
		case 2: px++; break;
		case 3: px++; py++; break;
		case 4: py++; break;
		case 5: px--; py++; break;
		case 6: px--; break;
		case 7: px--; py--; break;
		default: break;
		}
		
		Location nextLocation = new Location (0, px, py, heading) ;
		
		return nextLocation;
	}
	
	/*
	 * 計算升級增加血量
	 */
	public static int calcIncreaseHp (int type, int hp, int max_hp, int con) {
		short random_hp = 0;
		if (con > 15) {
			random_hp = (short) (con - 15) ;
		}
		
		if (type == 0) {
			random_hp += (short) (5 + random.nextInt (6) ) ;
			if (hp + max_hp > Configurations.MAX_HP_ROYAL) {
				random_hp = (short) (Configurations.MAX_HP_ROYAL - max_hp) ;
			}
			
		} else if (type == 1) {
			random_hp += (short) (6 + random.nextInt (7) ) ;
			if (hp + max_hp > Configurations.MAX_HP_KNIGHT) {
				random_hp = (short) (Configurations.MAX_HP_KNIGHT - max_hp) ;
			}
			
		} else if (type == 2) {
			random_hp += (short) (5 + random.nextInt (6) ) ;
			if (hp + max_hp > Configurations.MAX_HP_ELF) {
				random_hp = (short) (Configurations.MAX_HP_ELF - max_hp) ;
			}
			
		} else if (type == 3) {
			random_hp += (short) (3 + random.nextInt (4) ) ;
			if (hp + max_hp > Configurations.MAX_HP_MAGE) {
				random_hp = (short) (Configurations.MAX_HP_MAGE - max_hp) ;
			}
			
		} else if (type == 4) {
			random_hp += (short) (5 + random.nextInt (6) ) ;
			if (hp + max_hp > Configurations.MAX_HP_DARKELF) {
				random_hp = (short) (Configurations.MAX_HP_DARKELF - max_hp) ;
			}
			
		} else {
			return 0;
		}
		return random_hp;
	}
	
	/*
	 * 計算升級增加魔量
	 */
	static final int SEED[] = {
			-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, //0-9
			-1, -1,  0,  0,  0,  2,  2,  2,  3,  3, //10-19
			 4,  5,  5,  5,  6,  7,  9 //20-26
	} ;
	public static int calcIncreaseMp (int type, int mp, int max_mp, int wis) {
		int random_mp = 0;
		int seed = 0;
		
		if (wis > 26) {
			seed = SEED[26];
		} else {
			seed = SEED[wis];
		}
		
		/*
		randommp = 2 + rnd.nextInt(3 + seed % 2 + (seed / 6) * 2) + seed / 2
				- seed / 6;
		*/
		//幹破你娘在算殺小
		//這組沒改掉我跟你姓
		random_mp = 2 + random.nextInt (3 + seed % 2 + (seed / 6) * 2) + seed / 2 - seed / 6;
		
		if (type == 0) {
			if (max_mp + random_mp > Configurations.MAX_MP_ROYAL) {
				random_mp = Configurations.MAX_MP_ROYAL - max_mp;
			}
			
		} else if (type == 1) {
			if (wis == 9) {
				random_mp --;
			} else {
				random_mp = (int) (1.0 * random_mp / 2 + 0.5) ;
			}
			
			if (max_mp + random_mp > Configurations.MAX_MP_KNIGHT) {
				random_mp = Configurations.MAX_MP_KNIGHT - max_mp;
			}
		} else if (type == 2) {
			random_mp = (int) (random_mp * 1.5) ;
			if (max_mp + random_mp > Configurations.MAX_MP_ELF) {
				random_mp = Configurations.MAX_MP_ELF - max_mp;
			}
			
		} else if (type == 3) {
			random_mp = (int) (random_mp * 2.0) ;
			if (max_mp + random_mp > Configurations.MAX_MP_MAGE) {
				random_mp = Configurations.MAX_MP_MAGE - max_mp;
			}
			
		} else if (type == 4) {
			random_mp = (int) (random_mp * 1.5) ;
			if (max_mp + random_mp > Configurations.MAX_MP_DARKELF) {
				random_mp = Configurations.MAX_MP_DARKELF - max_mp;
			}
			
		} else {
			return 0;
		}
		return random_mp;
	}
	
	private static final int MR_K[] = {
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //0-9
			0, 0, 0, 0, 0, 3, 3, 6,10,15, //10-19
			21,28,37,47,50 //20-24
	} ;
	public static int calcMr (int type, int level, int wis) {
		int Mr = 0;
		int k= 0;
		if (type == 0) {
			Mr = 10;
		} else if (type == 1) {
			Mr = 0;
		} else if (type == 2) {
			Mr = 25;
		} else if (type == 3) {
			Mr = 10;
		} else if (type == 4) {
			Mr = 10;
		}
		
		if (wis > 24) {
			k = MR_K[24];
		} else {
			k = MR_K[wis];
		}
		
		Mr += k + (level >> 1) ;
		
		return Mr;
	}
	
	private static final int SP_K[] = {
			-1, -1, -1, -1, -1, -1, -1, -1, -1, 0, //0-9
			 0,  0,  1,  1,  1,  2,  2,  2 //10-17
	} ;
	public static int calcSp (int type, int level, int intel) {
		int sp = 0;
		int k = 0;
		
		if (type == 0) {
			sp = level / 10;
		} else if (type == 1) {
			sp = level / 50;
		} else if (type == 2) {
			sp = level / 8;
		} else if (type == 3) {
			sp = level / 4;
		} else if (type == 4) {
			sp = level / 12;
		}
		
		if (intel > 17) {
			k = intel - 15;
		} else {
			k = SP_K[intel];
		}
		
		return sp + k;
	}
	
	private static final int DEX_K[] = {
			8, 8, 8, 8, 8, 8, 8, 8, 8, 8, //0~9 DEX
			7, 7, 7, 6, 6, 6, 5, 5, 4} ;  //10~18 DEX
	public static int calcAcBonusFromDex (int level, int dex) {
		int Bonus = 10;
		int k = 0;
		if (dex > 18) {
			k = DEX_K[18];
		} else {
			k = DEX_K[dex];
		}
		
		Bonus -= level / k;
		return Bonus;
	}
}
