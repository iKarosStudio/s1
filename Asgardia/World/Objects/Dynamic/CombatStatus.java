package Asgardia.World.Objects.Dynamic;

public class CombatStatus
{
	/*
	 * Str:力量
	 * Dex:敏捷
	 * Con:體質
	 * Wis:精神
	 * Cha:魅力
	 * Intel:智力
	 */
	public int Str, Dex, Con, Wis, Cha, Intel;
	public int Sp, Mr;
	public int Ac;
	public int MaxHp, MaxMp, Hpr, Mpr;
	public int DefFire, DefWater, DefWind, DefEarth;
	
	/*
	 * 額外攻擊點數修正
	 * 額外命中修正
	 * 魔法攻擊修正
	 * 弓箭命中修正
	 * 傷害減免修正
	 * 負重減免修正
	 */
	public int DmgModify;
	public int HitModify;
	public int SpModify;
	public int BowHitModify;
	
	public int DmgReduction;
	public int WeightReduction;
}
