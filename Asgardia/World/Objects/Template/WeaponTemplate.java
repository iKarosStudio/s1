package Asgardia.World.Objects.Template;

import Asgardia.Server.PacketBuilder;
import static Asgardia.World.Objects.Items.ItemTypeTable.*;

public class WeaponTemplate
{	
	public int MajorType = 1;
	public int MinorType = 0; //Weapon type
	public int Material;
	
	public int PcGfx = 0;
	
	public int ItemId;
	public String Name;
	public String NameId;
	public String TypeName;
	public String MaterialName;
	public int Weight;
	public int InvGfx;
	public int GndGfx;
	public int ItemDescId;
	public int DmgSmall;
	public int DmgLarge;
	public int SafeEnchant;
	public boolean UseRoyal;
	public boolean UseKnight;
	public boolean UseMage;
	public boolean UseElf;
	public boolean UseDarkElf;
	public int HitModifier;
	public int DmgModifier;
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
	public int Mdef;
	public boolean HasteItem;
	public int MagicDmgModifier;
	public boolean CanBeDmg;
	public int MinLevel;
	public int MaxLevel;
	public int Bless;
	public boolean Trade;
	public boolean ManaItem;
	
	public WeaponTemplate (
		int item_id,
		String name,
		String name_id,
		String type,
		String material,
		int weight,
		int invgfx,
		int gndgfx,
		int itemdesc_id,
		int dmg_small,
		int dmg_large,
		int safe_enchant,
		boolean use_royal,
		boolean use_kighit,
		boolean use_mage,
		boolean use_elf,
		boolean use_darkelf,
		int hit_modifier,
		int dmg_modifier,
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
		int add_mr,
		int m_def,
		boolean haste_item,
		int magic_dmg_modifier,
		boolean can_be_dmg,
		int min_lvl,
		int max_lvl,
		int bless,
		boolean trade,
		boolean mana_item) {
		
		ItemId = item_id;
		Name = name;
		NameId = name_id;
		TypeName = type;
		MaterialName = material;
		Weight = weight;
		InvGfx = invgfx;
		GndGfx = gndgfx;
		ItemDescId = itemdesc_id;
		DmgSmall = dmg_small;
		DmgLarge = dmg_large;
		SafeEnchant = safe_enchant;
		UseRoyal = use_royal;
		UseKnight = use_kighit;
		UseMage = use_mage;
		UseElf = use_elf;
		UseDarkElf = use_darkelf;
		HitModifier = hit_modifier;
		DmgModifier = dmg_modifier;
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
		Mdef = m_def;
		HasteItem = haste_item;
		MagicDmgModifier = magic_dmg_modifier;
		CanBeDmg = can_be_dmg;
		MinLevel = min_lvl;
		MaxLevel = max_lvl;
		Bless = bless;
		Trade = trade;
		ManaItem = mana_item;
		
		switch (TypeName) {
		case "sword" :
			MinorType = WEAPON_TYPE_SWORD; 
			PcGfx = WEAPON_GFX_SWORD;
			break;
		case "dagger" :
			MinorType = WEAPON_TYPE_DAGGER; 
			PcGfx = WEAPON_GFX_DAGGER;
			break;
		case "tohandsword" :
			MinorType = WEAPON_TYPE_TOHAND_SWORD;
			PcGfx = WEAPON_GFX_TOHAND_SWORD;
			break;
		case "bow" :
			MinorType = WEAPON_TYPE_BOW; 
			PcGfx = WEAPON_GFX_BOW;
			break;
		case "spear" :
			MinorType = WEAPON_TYPE_SPEAR;
			PcGfx = WEAPON_GFX_SPEAR;
			break;
		case "blunt" :
			MinorType = WEAPON_TYPE_BLUNT;
			PcGfx = WEAPON_GFX_BLUNT;
			break;
		case "staff" :
			MinorType = WEAPON_TYPE_STAFF;
			PcGfx = WEAPON_GFX_STAFF;
			break;
		case "throwingknife" :
			MinorType = WEAPON_TYPE_THROWING_KNIFE;
			PcGfx = WEAPON_GFX_THROWING_KNIFE;
			break;
		case "arrow" :
			MinorType = WEAPON_TYPE_ARROW;
			PcGfx = WEAPON_GFX_ARROW;
			break;
		case "gauntlet" :
			MinorType = WEAPON_TYPE_GAUNTLET;
			PcGfx = WEAPON_GFX_GAUNTLET;
			break;
		case "claw" :
			MinorType = WEAPON_TYPE_CLAW;
			PcGfx = WEAPON_GFX_CLAW;
			break;
		case "edoryu" :
			MinorType = WEAPON_TYPE_EDORYU;
			PcGfx = WEAPON_GFX_EDORYU;
			break;
		case "singlebow" :
			MinorType = WEAPON_TYPE_SINGLE_BOW;
			PcGfx = WEAPON_GFX_SINGLE_BOW;
			break;
		case "singlespear" :
			MinorType = WEAPON_TYPE_SINGLE_SPEAR;
			PcGfx = WEAPON_GFX_SINGLE_SPEAR;
			break;
		case "tohandblunt" :
			MinorType = WEAPON_TYPE_TOHAND_BLUNT;
			PcGfx = WEAPON_GFX_TOHAND_BLUNT;
			break;
		case "tohandstaff" :
			MinorType = WEAPON_TYPE_TOHAND_STAFF;
			PcGfx = WEAPON_GFX_TOHAND_STAFF;
			break;
		default :
			MinorType = 0xFF;
			PcGfx = 0;
			break;
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
	
	public byte[] ParseWeaponDetail () {
		PacketBuilder builder = new PacketBuilder () ;
		
		builder.WriteByte (1) ;
		builder.WriteByte (DmgSmall) ;
		builder.WriteByte (DmgLarge) ;
		builder.WriteByte (Material) ;
		builder.WriteDoubleWord (Weight / 1000) ;
		
		//
		builder.WriteByte (2) ; //Enchant level
		builder.WriteByte (0) ;
		
		//builder.WriteByte (3) ; //Durability
		//builder.WriteByte (100) ;
		
		if (isTwohandedWeapon () ) {
			builder.WriteByte (4) ;
		}
		
		if (HitModifier > 0) {
			builder.WriteByte (5) ; //hit modifier
			builder.WriteByte (HitModifier) ;
		}
		
		if (DmgModifier > 0) {
			builder.WriteByte (6) ; //dmg modifier
			builder.WriteByte (DmgModifier) ;
		}
		
		byte UseClass = 0;
		if (UseRoyal) UseClass   |= 0x01;
		if (UseKnight) UseClass  |= 0x02;
		if (UseElf) UseClass     |= 0x04;
		if (UseMage) UseClass    |= 0x08;
		if (UseDarkElf) UseClass |= 0x10;
		builder.WriteByte (7) ; //use class
		builder.WriteByte (UseClass);
		
		if (ManaItem || ItemId == 126 || ItemId == 127) {
			builder.WriteByte (16) ;
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
		
		return builder.GetPacketNoPadding () ;
	}
	
	public boolean isTwohandedWeapon () {
		return (MinorType == WEAPON_TYPE_TOHAND_SWORD) || (MinorType == WEAPON_TYPE_TOHAND_STAFF) ||
				(MinorType == WEAPON_TYPE_TOHAND_BLUNT) || (MinorType == WEAPON_TYPE_BOW) ||
				(MinorType == WEAPON_TYPE_CLAW) || (MinorType == WEAPON_TYPE_EDORYU) ;
	}
}
