package Asgardia.World.Objects.Template;

import Asgardia.Server.PacketBuilder;
import static Asgardia.World.Objects.Items.ItemTypeTable.*;

public class ArmorTemplate
{	
	public int MajorType = 2;
	public int MinorType = 0;
	public int Material = 0;
	
	public int ItemId;
	public String Name;
	public String NameId;
	public String TypeName;
	public String MaterialName;
	public int Weight;
	public int InvGfx;
	public int GndGfx;
	public int ItemDescId;
	public int Ac;
	public int SafeEnchant;
	public boolean UseRoyal;
	public boolean UseKnight;
	public boolean UseMage;
	public boolean UseElf;
	public boolean UseDarkElf;
	public int AddStr;
	public int AddCon;
	public int AddDex;
	public int AddInt;
	public int AddWis;
	public int AddCha;
	public int AddHp;
	public int AddMp;
	public int AddHpr;
	public int AddMpr;
	public int AddSp;
	public int MinLevel;
	public int MaxLevel;
	public int Mdef;
	public boolean HasteItem;
	public int DamegeReduction;
	public int WeightReduction;
	public int BowHitRate;
	public int Bless;
	public boolean Trade;
	public int DefenseWater;
	public int DefenseWind;
	public int DefenseFire;
	public int DefenseEarth;
	public int RegistStan;
	public int RegistStone;
	public int RegistSleep;
	public int RegistFreeze;
	
	public ArmorTemplate (
		int item_id,
		String name,
		String name_id,
		String type,
		String material,
		int weight,
		int inv_gfx,
		int gnd_gfx,
		int itemdesc_id,
		int ac,
		int safe_enchant,
		boolean use_royal,
		boolean use_knight,
		boolean use_mage,
		boolean use_elf,
		boolean use_darkelf,
		int add_str,
		int add_con,
		int add_dex,
		int add_int,
		int add_wis,
		int add_cha,
		int add_hp,
		int add_mp,
		int add_hpr,
		int add_mpr,
		int add_sp,
		int min_lvl,
		int max_lvl,
		int m_def,
		boolean haste_item,
		int damege_reduction,
		int weight_reduction,
		int bow_hit_rate,
		int bless,
		boolean trade,
		int defense_water,
		int defense_wind,
		int defense_fire,
		int defense_earth,
		int regist_stan,
		int regist_stone,
		int regist_sleep,
		int regist_freeze) {
		
		ItemId = item_id;
		Name = name;
		NameId = name_id;
		TypeName = type;
		MaterialName = material;
		Weight = weight;
		InvGfx = inv_gfx;
		GndGfx = gnd_gfx;
		ItemDescId = itemdesc_id;
		Ac = ac;
		SafeEnchant = safe_enchant;
		UseRoyal = use_royal;
		UseKnight = use_knight;
		UseMage = use_mage;
		UseElf = use_elf;
		UseDarkElf = use_darkelf;
		AddStr = add_str;
		AddCon = add_con;
		AddDex = add_dex;
		AddInt = add_int;
		AddWis = add_wis;
		AddCha = add_cha;
		AddHp = add_hp;
		AddMp = add_mp;
		AddHpr = add_hpr;
		AddMpr = add_mpr;
		AddSp = add_sp;
		MinLevel = min_lvl;
		MaxLevel = max_lvl;
		Mdef = m_def;
		HasteItem = haste_item;
		DamegeReduction = damege_reduction;
		WeightReduction = weight_reduction;
		BowHitRate = bow_hit_rate;
		Bless = bless;
		Trade = trade;
		DefenseWater = defense_water;
		DefenseWind = defense_wind;
		DefenseFire = defense_fire;
		DefenseEarth = defense_earth;
		RegistStan = regist_stan;
		RegistStone = regist_stone;
		RegistSleep = regist_sleep;
		RegistFreeze = regist_freeze;
		
		switch (TypeName) {
		case "none" : MinorType = ARMOR_TYPE_NONE; break;
		case "helm" : MinorType = ARMOR_TYPE_HELM; break;
		case "armor" : MinorType = ARMOR_TYPE_ARMOR; break;
		case "T" : MinorType = ARMOR_TYPE_T; break;
		case "cloak" : MinorType = ARMOR_TYPE_CLOAK; break;
		case "glove" : MinorType = ARMOR_TYPE_GLOVE; break;
		case "boots" : MinorType = ARMOR_TYPE_BOOTS; break;
		case "shield" : MinorType = ARMOR_TYPE_SHIELD; break;
		case "amulet" : MinorType = ARMOR_TYPE_AMULET; break;
		case "ring" : MinorType = ARMOR_TYPE_RING; break;
		case "belt" : MinorType = ARMOR_TYPE_BELT; break;
		case "ring2" : MinorType = ARMOR_TYPE_RING2; break;
		case "earring" : MinorType = ARMOR_TYPE_EARRING; break;
		default : MinorType = 0xFF; break;
		}
		
		switch (MaterialName) {
		case "none" : Material = MATERIAL_NONE; break;
		case "liquid" : Material = MATERIAL_LIQUID; break;
		case "web" : Material = MATERIAL_WEB; break;
		case "vegetation" : Material = MATERIAL_VEGETATION; break;
		case "animalmetter" : Material = MATERIAL_ANIMALMATTER; break;
		case "paper" : Material = MATERIAL_PAPER; break;
		case "cloth" : Material = MATERIAL_CLOTH; break;
		case "leather" : Material = MATERIAL_LEATHER; break;
		case "wood" : Material = MATERIAL_WOOD; break;
		case "bone" : Material = MATERIAL_BONE; break;
		case "dragonscale" : Material = MATERIAL_DRAGONSCALE; break;
		case "iron" : Material = MATERIAL_IRON; break;
		case "steel" : Material = MATERIAL_STEEL; break;
		case "copper" : Material = MATERIAL_COPPER; break;
		case "silver" : Material = MATERIAL_SILVER; break;
		case "gold" : Material = MATERIAL_GOLD; break;
		case "platinum" : Material = MATERIAL_PLATINUM; break;
		case "mithril" : Material = MATERIAL_MITHRIL; break;
		case "blackmithril" : Material = MATERIAL_BLACKMITHRIL; break;
		case "glass" : Material = MATERIAL_GLASS; break;
		case "mineral" : Material = MATERIAL_MINERAL; break;
		case "oriharukon" : Material = MATERIAL_ORIHARUKON; break;
		default: Material = 0xFF;
		}
	}
	
	public byte[] ParseArmorDetail () {
		PacketBuilder builder = new PacketBuilder () ;
		
		builder.WriteByte (19) ;
		builder.WriteByte (Math.abs (Ac) ) ;
		builder.WriteByte (Material) ;
		builder.WriteDoubleWord (Weight / 1000) ;
		
		//
		builder.WriteByte (2) ; //Enchant level
		builder.WriteByte (0) ;
		
		builder.WriteByte (3) ; //Durability
		builder.WriteByte (100) ;
		
		byte UseClass = 0;
		if (UseRoyal) UseClass   |= 0x01;
		if (UseKnight) UseClass  |= 0x02;
		if (UseElf) UseClass     |= 0x04;
		if (UseMage) UseClass    |= 0x08;
		if (UseDarkElf) UseClass |= 0x10;
		builder.WriteByte (7) ; //use class
		builder.WriteByte (UseClass);

		
		if (BowHitRate > 0) {
			builder.WriteByte (24) ;
			builder.WriteByte (BowHitRate) ;
		}
		
		if (AddStr > 0) {
			builder.WriteByte (8) ;
			builder.WriteByte (AddStr) ;
		}
		
		if (AddDex > 0) {
			builder.WriteByte (9) ;
			builder.WriteByte (AddDex) ;
		}
		
		if (AddCon > 0) {
			builder.WriteByte (10) ;
			builder.WriteByte (AddCon) ;
		}
		
		if (AddInt > 0) {
			builder.WriteByte (11) ;
			builder.WriteByte (AddInt) ;
		}
		
		if (AddWis > 0) {
			builder.WriteByte (12) ;
			builder.WriteByte (AddWis) ;
		}
		
		if (AddCha > 0) {
			builder.WriteByte (13) ;
			builder.WriteByte (AddCha) ;
		}
		
		if (AddHp > 0) {
			builder.WriteByte (31) ;
			builder.WriteByte (AddHp) ;
		}
		
		if (AddMp > 0) {
			builder.WriteByte (32) ;
			builder.WriteByte (AddMp) ;
		}
		
		if (Mdef > 0) {
			builder.WriteByte (15) ;
			builder.WriteWord (Mdef) ;
		}
		
		if (AddSp > 0) {
			builder.WriteByte (17) ;
			builder.WriteWord (AddSp) ;
		}
		
		if (HasteItem) {
			builder.WriteByte (18) ;
		}
		
		if (DefenseFire > 0) {
			builder.WriteByte (27) ;
			builder.WriteByte (DefenseFire) ;
		}
		
		if (DefenseWater > 0) {
			builder.WriteByte (28) ;
			builder.WriteByte (DefenseWater) ;
		}
		
		if (DefenseWind > 0) {
			builder.WriteByte (29) ;
			builder.WriteByte (DefenseWind) ;
		}
		
		if (DefenseEarth > 0) {
			builder.WriteByte (30) ;
			builder.WriteByte (DefenseEarth) ;
		}
		
		return builder.GetPacketNoPadding () ;
	}
}
