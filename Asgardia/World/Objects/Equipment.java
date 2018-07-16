package Asgardia.World.Objects;

import java.util.*;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.Objects.Items.*;

public class Equipment
{
	/* Offset
	 * 0:Weapon
	 * 1:Arrow
	 * 2:Helmet
	 * 3:Armor
	 * 4:T_shirt
	 * 5:Cloak
	 * 6:Boots
	 * 7:Shield
	 * 8:Belt
	 * 9:Amulet
	 * 10:Ring1
	 * 11:Ring2
	 * 12:Earring
	 */
	private static final int WEAPON = 0;
	private static final int ARROW = 1;
	private static final int HELMET = 2;
	private static final int ARMOR = 3;
	private static final int TSHIRT = 4;
	private static final int CLOAK = 5;
	private static final int BOOTS = 6;
	private static final int SHIELD = 7;
	private static final int BELT = 8;
	private static final int AMULET = 9;
	private static final int RING1 = 10;
	private static final int RING2 = 11;
	private static final int EARRING = 12;
	
	private SessionHandler Handle;
	private ItemInstance[] _Equip = null;
	
	public ItemInstance[] getEquipmentList () {
		return _Equip;
	}
	
	public Equipment (SessionHandler handle) {
		Handle = handle;
		_Equip = new ItemInstance[12] ;
		for (ItemInstance i : _Equip) {
			i = null;
		}
	}
	
	public ItemInstance getWeapon () {
		return _Equip[WEAPON];
	}
	
	public void setWeapon (ItemInstance w) {
		
		if (_Equip[WEAPON] != null) {
			if (_Equip[WEAPON].Uuid == w.Uuid) {
				/*W武器->解除裝備*/
				_Equip[WEAPON].IsEquipped = false;
				Handle.SendPacket (new ItemUpdateName (_Equip[WEAPON]).getRaw () ) ;
				DatabaseCmds.UpdatePcItem (_Equip[WEAPON]) ;
				_Equip[WEAPON] = null;
				
			} else {
				/*X武器->W武器*/
				_Equip[WEAPON].IsEquipped = false;
				Handle.SendPacket (new ItemUpdateName (_Equip[WEAPON]).getRaw () ) ;
				DatabaseCmds.UpdatePcItem (_Equip[WEAPON]) ;
				
				_Equip[WEAPON] = w;
				_Equip[WEAPON].IsEquipped = true;
				Handle.SendPacket (new ItemUpdateName (_Equip[WEAPON]).getRaw () ) ;
				DatabaseCmds.UpdatePcItem (_Equip[WEAPON]) ;
			}
		} else {
			/*空手->裝備(W)武器*/
			_Equip[WEAPON] = w;
			_Equip[WEAPON].IsEquipped = true;
			Handle.SendPacket (new ItemUpdateName (_Equip[WEAPON]).getRaw () ) ;
			DatabaseCmds.UpdatePcItem (_Equip[WEAPON]) ;
		}
	}
	
	public ItemInstance getHelmet () {
		return _Equip[HELMET];
	}
	
	public ItemInstance getArmor () {
		return _Equip[ARMOR];
	}
	
	public ItemInstance getTShirt () {
		return _Equip[TSHIRT];
	}
	
	public ItemInstance getCloak () {
		return _Equip[CLOAK];
	}
	
	public ItemInstance getBoots () {
		return _Equip[BOOTS];
	}
	
	public ItemInstance getShield () {
		return _Equip[SHIELD];
	}
	
	public ItemInstance getAmulet () {
		return _Equip[AMULET];
	}
	
	public ItemInstance getRing1 () {
		return _Equip[RING1];
	}
	
	public ItemInstance getRing2 () {
		return _Equip[RING2];
	}
	
	public ItemInstance getBelt () {
		return _Equip[BELT];
	}
	
	public ItemInstance getEarring () {
		return _Equip[EARRING];
	}
	
	/*
	 * c_itemuse.java : 3498 (UseArmor)->4185
	 */
	public void setArmor (ItemInstance a) {
		int TypeIndex = a.MinorType + 1;
		
		if (_Equip[TypeIndex] != null) {
			if (_Equip[TypeIndex].Uuid == a.Uuid) {
				/*P裝備->解除裝備*/
				_Equip[TypeIndex].IsEquipped = false;
				Handle.SendPacket (new ItemUpdateName (_Equip[TypeIndex]).getRaw () ) ;
				DatabaseCmds.UpdatePcItem (_Equip[TypeIndex]) ;
				_Equip[TypeIndex] = null;
				
				//System.out.printf ("解除裝備 %s\n", a.getName () ) ;
			} else {
				/*P裝備->A裝備*/
				_Equip[TypeIndex].IsEquipped = false;
				Handle.SendPacket (new ItemUpdateName (_Equip[TypeIndex]).getRaw () ) ;
				DatabaseCmds.UpdatePcItem (_Equip[TypeIndex]) ;
				
				_Equip[TypeIndex] = a;
				_Equip[TypeIndex].IsEquipped = true;
				Handle.SendPacket (new ItemUpdateName (_Equip[TypeIndex]).getRaw () ) ;
				DatabaseCmds.UpdatePcItem (_Equip[TypeIndex]) ;
				
				//System.out.printf ("裝備 %s\n", a.getName () ) ;
			}
		} else {
			/*空裝->裝備(A)道具*/
			_Equip[TypeIndex] = a;
			_Equip[TypeIndex].IsEquipped = true;
			Handle.SendPacket (new ItemUpdateName (_Equip[TypeIndex]).getRaw () ) ;
			DatabaseCmds.UpdatePcItem (_Equip[TypeIndex]) ;
			
			//System.out.printf ("裝備 %s\n", a.getName () ) ;
		}
	}
}
