package Asgardia.World.Objects.Items;

import Asgardia.Server.PacketBuilder;
import Asgardia.World.*;
import Asgardia.World.Objects.Static.*;
import Asgardia.World.Objects.Template.*;

/*
 * 實體道具物件, 給上座標之後可以被丟在地上
 */
public class ItemInstance extends StaticObject
{
	public int OwnerId = 0;
	
	public int ItemId = 0;
	public String Name;
	public String NameId;
	
	public String TypeName; 
	public String MaterialName;
	
	/* 0:道具 1:武器 2:防具 */
	public int MajorType = 0;
	
	
	public int MinorType = 0;
	public int UseType = 0;
	public int Material = 0;
	
	public int Count = 0;
	public int Weight = 0;
	public int InvGfx = 0;
	public int GndGfx = 0;
	
	/* 腳色持有武器的外觀編號 */
	public int PcGfx = 0;
	
	public int ItemDescId = 0; /* 鑑定說明編號 */
	
	public int SafeEnchant = 0;
	public int Enchant = 0;
	
	/* 損壞度 */
	public int Durability = 0;
	
	public int ChargeCount = 0;
	public boolean IsEquipped = false;
	public boolean IsIdentified = false;
	public int Bless = 0; /* 0:Normal, 1:Blessed, 2:Cursed */
	public boolean Trade = false;
	
	public int FoodValue = 0;
	public int MaxCharge = 0;
	public int DelayId;
	public int DelayTime;
	public boolean SaveAtOnce = false;
	public boolean Stackable = false;
	public boolean HasteItem = false;
	public boolean ManaItem = false;
	
	public int MinLevel = 0; //要求使用等級
	public int MaxLevel = 0; //最大使用等級
	public boolean UseRoyal;
	public boolean UseKnight;
	public boolean UseMage;
	public boolean UseElf;
	public boolean UseDarkElf;
	public int AddStr, AddDex, AddCon, AddInt, AddWis, AddCha; //增加素質
	public int AddHp, AddMp, AddHpr, AddMpr, AddSp, Mdef;
	
	public int DmgSmall = 0; //小怪傷害
	public int DmgLarge = 0; //大怪傷害
	public int HitModifier = 0; //命中加成
	public int DmgModifier = 0; //額外攻擊點數
	public int MagicDmgModifier = 0; //魔法攻擊點數
	public boolean CanBeDmg = true; //會壞刀
	public boolean isTwoHanded = false; //雙手武器
	
	public int Ac = 0; //防禦數值
	public int BowHitRate = 0; //弓箭命中修正
	public int DamegeReduction = 0; //傷害減免
	public int WeightReduction = 0; //負重減免
	public int DefenseWater = 0, DefenseWind = 0, DefenseFire = 0, DefenseEarth = 0;
	public int ResistStan = 0, ResistStone = 0, ResistSleep = 0, ResistFreeze = 0;
	
	private byte[] Detail = null;
	
	public ItemInstance () {
		//
	}
	
	public ItemInstance (
		int item_uuid,
		int item_id,
		int char_uuid,
		int count,
		int enchant_level,
		int durability,
		int charge_count,
		boolean is_equipped,
		boolean is_identified
	) {
		//load cached template data
		Uuid = item_uuid;
		ItemId = item_id;
		OwnerId = char_uuid;
		Count = count;
		Enchant = enchant_level;
		Durability = durability;
		ChargeCount = charge_count;
		IsEquipped = is_equipped;
		IsIdentified = is_identified;
		
		if (CacheData.ItemCache.containsKey (ItemId) ) {
			ItemTemplate i = CacheData.ItemCache.get (ItemId) ;
			
			Name = i.Name; NameId = i.NameId;
			TypeName = i.TypeName;
			Material = i.Material; MaterialName = i.MaterialName;
			MajorType = i.MajorType; MinorType = i.MinorType; UseType = i.UseType;
			Weight = i.Weight;
			InvGfx = i.InvGfx; GndGfx = i.GrdGfx;
			Bless = i.Bless; Trade = i.Trade;
			FoodValue = i.FoodValue;
			MaxCharge = i.MaxCharge;
			DmgSmall = i.DmgSmall; DmgLarge = i.DmgLarge;
			DelayId = i.DelayId; DelayTime = i.DelayTime;
			SaveAtOnce = i.SaveAtOnce; 
			Stackable = i.Stackable;
			Detail = ParseItemDetail (1) ;
			
		} else if (CacheData.WeaponCache.containsKey (ItemId) ) {
			WeaponTemplate w = CacheData.WeaponCache.get (ItemId) ;
			
			Name = w.Name; NameId = w.NameId;
			TypeName = w.TypeName;
			Material = w.Material; MaterialName = w.MaterialName;
			MajorType = w.MajorType; MinorType = w.MinorType; UseType = ItemTypeTable.TYPE_USE_WEAPON;
			Weight = w.Weight;
			InvGfx = w.InvGfx; GndGfx = w.GndGfx; PcGfx = w.PcGfx;
			Bless = w.Bless; Trade = w.Trade;
			HasteItem = w.HasteItem; ManaItem = w.ManaItem;
			MinLevel = w.MinLevel; MaxLevel = w.MaxLevel;
			UseRoyal = w.UseRoyal; UseKnight = w.UseKnight; UseMage = w.UseMage; UseElf = w.UseElf; UseDarkElf = w.UseDarkElf;
			AddStr = w.AddStr; AddDex = w.AddDex; AddCon = w.AddCon;
			AddInt = w.AddInt; AddWis = w.AddWis; AddCha = w.AddCha;
			AddHp = w.AddHp; AddMp = w.AddMp; AddHpr = w.AddHpr; AddMpr = w.AddMpr;
			AddSp = w.AddSp; Mdef = w.Mdef;
			DmgSmall = w.DmgSmall; DmgLarge = w.DmgLarge;
			HitModifier = w.HitModifier; DmgModifier = w.DmgModifier; MagicDmgModifier = w.MagicDmgModifier;
			CanBeDmg = w.CanBeDmg;
			isTwoHanded = w.isTwohandedWeapon () ;
			Stackable = false;
			Detail = ParseWeaponDetail () ;
			
		} else if (CacheData.ArmorCache.containsKey (ItemId) ) {
			ArmorTemplate a = CacheData.ArmorCache.get (ItemId) ;
			
			Name = a.Name; NameId = a.NameId;
			TypeName = a.TypeName;
			Material = a.Material; MaterialName = a.MaterialName;
			MajorType = a.MajorType; MinorType = a.MinorType;
			//UseType = ItemTypeTable.TYPE_USE_ARMOR;
			UseType = ItemTypeTable.ArmorMinorType2UseType (a.MinorType) ;
			Weight = a.Weight;
			InvGfx = a.InvGfx; GndGfx = a.GndGfx;
			Bless = a.Bless; Trade = a.Trade;
			HasteItem = a.HasteItem;
			Ac = a.Ac;
			MinLevel = a.MinLevel; MaxLevel = a.MaxLevel;
			UseRoyal = a.UseRoyal; UseKnight = a.UseKnight; UseMage = a.UseMage; UseElf = a.UseElf; UseDarkElf = a.UseDarkElf;
			AddStr = a.AddStr; AddDex = a.AddDex; AddCon = a.AddCon;
			AddInt = a.AddInt; AddWis = a.AddWis; AddCha = a.AddCha;
			AddHp = a.AddHp; AddMp = a.AddMp; AddHpr = a.AddHpr; AddMpr = a.AddMpr;
			BowHitRate = a.BowHitRate;
			DamegeReduction = a.DamegeReduction; WeightReduction = a.WeightReduction;
			DefenseWater = a.DefenseWater;
			DefenseWind = a.DefenseWind;
			DefenseFire = a.DefenseFire;
			DefenseEarth = a.DefenseEarth;
			ResistStan = a.RegistStan;
			ResistStone = a.RegistStone;
			ResistSleep = a.RegistSleep;
			ResistFreeze = a.RegistFreeze;
			Stackable = false;
			Detail = ParseArmorDetail () ;
			
		} else {
			//
		}
	}
	
	public String getName () {
		StringBuffer ItemViewName = new StringBuffer () ;
		//參考L1ItemInstance.java getNumberedViewName	
		
		/*
		 * 之後再處理燈籠蠟燭使用的問題
		 * 燈具使用($10), ($11)
		 */
		
		if (MajorType == 0) {
			ItemViewName.append (Name);
			if (Count > 1) {
				ItemViewName.append (" (" + Count + ")") ;
			}
		} else if (MajorType == 1) {
			if (IsIdentified) {
				ItemViewName.append ("+" + Enchant + " ") ;
			} 
			ItemViewName.append (Name) ;
			if (IsEquipped) {
				ItemViewName.append (" ($9)") ;
			}
			
		} else if (MajorType == 2) {
			if (IsIdentified) {
				ItemViewName.append ("+" + Enchant + " ") ;
			} 
			ItemViewName.append (Name) ;
			if (IsEquipped) {
				ItemViewName.append (" ($117)") ;
			}
			
		} else {
			ItemViewName.append ("unknown") ;
		}
		
		return ItemViewName.toString () ;
	}
	
	public byte[] getDetail () {
		if (MajorType == 0) {
			return ParseItemDetail (Count) ;
		} else if (MajorType == 1) {
			return ParseWeaponDetail () ;
		} else if (MajorType == 2) {
			return ParseArmorDetail () ;
		} else {
			return Detail;
		}
	}
	
	public byte[] ParseItemDetail (int count) {
		PacketBuilder builder = new PacketBuilder () ;
		
		switch (TypeName) {
		case "light" :
			builder.WriteByte (22) ;
			builder.WriteWord (10) ; //light range
			break;
			
		case "food" :
			builder.WriteByte (21) ;
			builder.WriteWord (FoodValue) ;
			break;
		
		case "arrow" :
		case "sting" :
			builder.WriteByte (1) ;
			builder.WriteByte (DmgSmall) ;
			builder.WriteByte (DmgLarge) ;
			break; 
			
		default :
			builder.WriteByte (23) ;
			break;
		}
		
		builder.WriteByte (Material) ;
		builder.WriteDoubleWord ((Weight * count) / 1000);
		
		return builder.GetPacketNoPadding () ;
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
		builder.WriteByte (Enchant) ;
		
		if (Durability > 0) {
			builder.WriteByte (3) ; //Durability
			builder.WriteByte (Durability) ;
		}
		
		if (isTwoHanded) {
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
	
	public byte[] ParseArmorDetail () {
		PacketBuilder builder = new PacketBuilder () ;
		
		builder.WriteByte (19) ;
		builder.WriteByte (Math.abs (Ac) ) ;
		builder.WriteByte (Material) ;
		builder.WriteDoubleWord (Weight / 1000) ;
		
		//
		builder.WriteByte (2) ; //Enchant level
		builder.WriteByte (Enchant) ;
		
		if (Durability > 0) {
			builder.WriteByte (3) ; //Durability
			builder.WriteByte (Durability) ;
		}
		
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
	
	public boolean isStackable () {
		return Stackable;
	}
}
