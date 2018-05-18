package Asgardia.World.Objects.Template;

import Asgardia.Server.PacketBuilder;
import static Asgardia.World.Objects.Items.ItemTypeTable.*;

public class ItemTemplate
{	
	public int MajorType = 0; //0:etcitem 1:weapon 2:armor
	public int MinorType;
	public int UseType;
	public int Material;
	
	public int ItemId;
	public String Name;
	public String NameId;
	public String TypeName;
	public String UseTypeName;
	public String MaterialName;
	public int Weight;
	public int InvGfx;
	public int GrdGfx;
	public int DecsId;
	public boolean Stackable = false;
	public int MaxCharge = 0;
	public int DmgSmall = 0;
	public int DmgLarge = 0;
	public int MinLevel = 0;
	public int MaxLevel = 0;
	public int Bless = 0;
	public boolean Trade = true;
	public int DelayId = 0;
	public int DelayTime = 0;
	public int FoodValue = 0;
	public boolean SaveAtOnce = false;
	
	public ItemTemplate (
			int item_id, 
			String name, 
			String name_id, 
			String item_type, 
			String use_type,
			String material,
			int weight,
			int inv_gfx,
			int grd_gfx,
			int decs_id,
			boolean stackable,
			int max_charge,
			int dmg_small,
			int dmg_large,
			int min_level,
			int max_level,
			int bless,
			boolean trade,
			int delay_id,
			int delay_time,
			int food_value,
			boolean save_at_once) {
		ItemId = item_id;
		Name = name;
		NameId = name_id;
		TypeName = item_type;
		UseTypeName = use_type;
		MaterialName = material;
		Weight = weight;
		InvGfx = inv_gfx;
		GrdGfx = grd_gfx;
		DecsId = decs_id;
		Stackable = stackable;
		MaxCharge = max_charge;
		DmgSmall = dmg_small;
		DmgLarge = dmg_large;
		MinLevel = min_level;
		MaxLevel = max_level;
		Bless = bless;
		Trade = trade;
		DelayId = delay_id;
		DelayTime = delay_time;
		FoodValue = food_value;
		SaveAtOnce = save_at_once;
		
		
		switch (TypeName) {
		case "arrow" : MinorType = TYPE_ARROW; break;
		case "wand" : MinorType = TYPE_WAND; break;
		case "light" : MinorType = TYPE_LIGHT; break;
		case "gem" : MinorType = TYPE_GEM; break;
		case "totem" : MinorType = TYPE_TOTEM; break;
		case "firecracker" : MinorType = TYPE_FIRECRACKER; break;
		case "potion" : MinorType = TYPE_POTION; break;
		case "food" : MinorType = TYPE_FOOD; break;
		case "scroll" : MinorType = TYPE_SCROLL; break;
		case "questitem" : MinorType = TYPE_QUEST_ITEM; break;
		case "spellbook" : MinorType = TYPE_SPELL_BOOK; break;
		case "petitem" : MinorType = TYPE_PET_ITEM; break;
		case "other" : MinorType = TYPE_OTHER; break;
		case "material" : MinorType = TYPE_MATERIAL; break;
		case "event" : MinorType = TYPE_EVENT; break;
		case "sting" : MinorType = TYPE_STING; break;
		default: MinorType = 0xFF; break;
		}
		
		switch (UseTypeName) {
		case "none" : UseType = -1; break;
		case "normal" : UseType = 0; break;
		case "weapon" : UseType = 1; break;
		case "armor" : UseType = 2; break;
		case "spell_long" : UseType = 5; break;
		case "ntele" : UseType = 6; break;
		case "identify" : UseType = 7; break;
		case "res" : UseType = 8; break;
		case "letter" : UseType = 12; break;
		case "letter_w" : UseType = 13; break;
		case "choice" : UseType = 14; break;
		case "instrument" : UseType = 15; break;
		case "sosc" : UseType = 16; break;
		case "spell_short" : UseType = 17; break;
		case "T" : UseType = 18; break;
		case "cloak" : UseType = 19; break;
		case "glove" : UseType = 20; break;
		case "boots" : UseType = 21; break;
		case "helm" : UseType = 22; break;
		case "ring" : UseType = 23; break;
		case "amulet" : UseType = 24; break;
		case "shield" : UseType = 25; break;
		case "dai" : UseType = 26; break;
		case "zel" : UseType = 27; break;
		case "blank" : UseType = 28; break;
		case "btele" : UseType = 29; break;
		case "spell_buff" : UseType = 30; break;
		case "ccard" : UseType = 31; break;
		case "ccard_w" : UseType = 32; break;
		case "vcard" : UseType = 33; break;
		case "vcard_w" : UseType = 34; break;
		case "wcard" : UseType = 35; break;
		case "wcard_w" : UseType = 36; break;
		case "belt" : UseType = 37; break;
		case "earring" : UseType = 40; break;
		case "fishing_rod" : UseType = 42; break;
		default : UseType = 0xFF; break;
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
	
	public byte[] ParseItemDetail () {
		return ParseItemDetail (1) ;
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
			
		default :
			builder.WriteByte (23) ;
			break;
		}
		
		builder.WriteByte (Material) ;
		builder.WriteDoubleWord ((Weight * count) / 1000);
		
		return builder.GetPacketNoPadding () ;
	}
}
