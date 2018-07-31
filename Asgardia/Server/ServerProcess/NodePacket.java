package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.ServerOpcodes;
import Asgardia.World.Npc.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Items.*;
import Asgardia.World.Objects.Monster.MonsterInstance;
import Asgardia.World.Objects.Template.NpcTemplate;

/*
 * S_OwnCharPack對比
 */
public class NodePacket
{
	PacketBuilder Builder = new PacketBuilder () ;
	
	public static int STATUS_POISON = 0x01; //中毒
	public static int STATUS_INVISIBLE = 0x02;//隱身
	public static int STATUS_PC = 0x04; //一般玩家
	public static int STATUS_FROZEN = 0x08; //冷凍
	public static int STATUS_BRAVE = 0x10; //勇水
	public static int STATUS_ELF_BRAVE = 0x20; //精餅
	public static int STATUS_FASTMOVE = 0x40; //高速移動用
	public static int STATUS_GHOST = 0x80; //幽靈模式
	
	/*
	 * 報告角色物件
	 */
	public NodePacket (PcInstance Node) {
		int status = STATUS_PC;	
		
		if (Node.BraveSpeed == 1) {
			status |= STATUS_BRAVE;
		}
		
		//Check Invisible
		//Check FastMove
		//Check Ghost
		
		Builder.writeByte (ServerOpcodes.NODE_PACK);
		Builder.WriteWord (Node.location.x) ;
		Builder.WriteWord (Node.location.y) ;
		Builder.WriteDoubleWord (Node.Uuid) ;
		
		Builder.WriteWord (Node.TempGfx) ; //get gfx
		if (Node.isDead () ) {
			Builder.writeByte (8) ;
		} else {
			Builder.writeByte (Node.getWeaponGfx () ) ; //武器外型修正
		}
		Builder.writeByte (Node.location.Heading) ;
		Builder.writeByte (10) ; //light
		
		Builder.writeByte (Node.MoveSpeed) ; //move speed;
		
		Builder.WriteDoubleWord (Node.Exp) ;
		Builder.WriteWord (Node.Lawful) ;
		Builder.WriteString (Node.Name) ;
		Builder.WriteString (Node.Title);
		
		Node.Status = status;
		Builder.writeByte (Node.Status) ; //status
		Builder.WriteDoubleWord (Node.ClanId) ;
		Builder.WriteString (Node.ClanName) ;
		Builder.WriteString (null) ;
		Builder.writeByte (0x00) ;
		
		Builder.writeByte (0xFF) ; //血條百分比
		
		Builder.writeByte (0x00) ;
		Builder.writeByte (0x00) ;
		Builder.writeByte (0x00) ;
		Builder.writeByte (0xFF) ;
		Builder.writeByte (0xFF) ;
	}
	
	/*
	 * 報告NPC物件
	 */
	public NodePacket (NpcInstance Npc) {
		Builder.writeByte (ServerOpcodes.NODE_PACK);
		Builder.WriteWord (Npc.location.x) ;
		Builder.WriteWord (Npc.location.y) ;
		Builder.WriteDoubleWord (Npc.Uuid) ;
		
		Builder.WriteWord (Npc.Gfx) ; //get gfx
		if (Npc.isDead () ) {
			Builder.writeByte (8) ;
		} else {
			Builder.writeByte (0) ;//weapon 0:hand 4:sword
		}
		Builder.writeByte (Npc.location.Heading) ;
		Builder.writeByte (10) ; //light
		Builder.writeByte (Npc.MoveSpeed) ; //move speed;
		
		Builder.WriteDoubleWord (0) ; //EXP
		Builder.WriteWord (0) ; //Lawful
		Builder.WriteString (Npc.NameId) ;
		Builder.WriteString (null) ;//title
		Builder.writeByte (Npc.Status) ; //status
		Builder.WriteDoubleWord (0) ;
		Builder.WriteString (null) ;
		Builder.WriteString (null) ;
		Builder.writeByte (0x00) ;
		
		Builder.writeByte (0xFF) ; //血條百分比
		
		Builder.writeByte (0x00) ;
		Builder.writeByte (Npc.Level) ;
		Builder.writeByte (0x00) ;
		Builder.writeByte (0xFF) ;
		Builder.writeByte (0xFF) ;
	}
	
	/*
	 * 報告地面物件
	 */
	public NodePacket (ItemInstance i) {
		Builder.writeByte (ServerOpcodes.NODE_PACK) ;
		Builder.WriteWord (i.location.x) ;
		Builder.WriteWord (i.location.y) ;
		Builder.WriteDoubleWord (i.Uuid) ;
		Builder.WriteWord (i.GndGfx) ;
		Builder.writeByte (0); //*
		Builder.writeByte (0); //heading
		Builder.writeByte (1) ; //light
		Builder.writeByte (0) ; //speed
		Builder.WriteDoubleWord (i.Count) ;
		Builder.writeByte (0) ;
		Builder.writeByte (0) ;
		if (i.Count > 1) {
			Builder.WriteString (i.getName () ) ;
		} else {
			Builder.WriteString (i.Name) ;
		}
		Builder.writeByte (0) ;
		Builder.WriteDoubleWord (0) ;
		Builder.WriteDoubleWord (0) ;
		Builder.writeByte (0xFF) ;
		Builder.writeByte (0) ;
		Builder.writeByte (0) ;
		Builder.writeByte (0) ;
		Builder.WriteWord (0xFFFF) ; 
		Builder.WriteDoubleWord (0) ;
		Builder.writeByte (0) ;
		Builder.writeByte (0) ;
	}
	
	/*
	 * 報告門物件
	 */
	public NodePacket (DoorInstance d) {
		Builder.writeByte (ServerOpcodes.NODE_PACK) ;
		Builder.WriteWord (d.location.x) ;
		Builder.WriteWord (d.location.y) ;
		Builder.WriteDoubleWord (d.Uuid) ;
		Builder.WriteWord (d.Gfx) ;
		Builder.writeByte (d.StatusActionCode) ;
		Builder.writeByte (0) ;
		Builder.writeByte (0) ;
		Builder.writeByte (0) ;
		Builder.WriteDoubleWord (0x01) ;
		Builder.WriteWord (0) ;
		Builder.WriteString (null) ;
		Builder.WriteString (null) ;
		if (d.isVisible) {
			Builder.writeByte (0) ;
		} else {
			Builder.writeByte (STATUS_INVISIBLE) ;
		}
		Builder.WriteDoubleWord (0);
		Builder.WriteString (null) ;
		Builder.WriteString (null) ;
		Builder.writeByte (0) ;
		Builder.writeByte (0xFF) ;
		Builder.writeByte (0) ;
		Builder.writeByte (0) ;
		Builder.writeByte (0) ;
		Builder.writeByte (0xFF) ;
		Builder.writeByte (0xFF) ;
	}	
	
	/*
	 * 報告怪物物件
	 */
	public NodePacket (MonsterInstance Npc) {
		Builder.writeByte (ServerOpcodes.NODE_PACK);
		Builder.WriteWord (Npc.location.x) ;
		Builder.WriteWord (Npc.location.y) ;
		Builder.WriteDoubleWord (Npc.Uuid) ;
		
		Builder.WriteWord (Npc.Gfx) ; //get gfx
		if (Npc.isDead () ) {
			Builder.writeByte (8) ;
		} else {
			Builder.writeByte (0) ;
		}
		Builder.writeByte (Npc.location.Heading) ;
		Builder.writeByte (10) ; //light
		Builder.writeByte (Npc.MoveSpeed) ; //move speed;
		
		Builder.WriteDoubleWord (0) ; //EXP
		Builder.WriteWord (0) ; //Lawful
		Builder.WriteString (Npc.NameId) ;
		Builder.WriteString (null) ;//title
		Builder.writeByte (Npc.Status) ; //status
		Builder.WriteDoubleWord (0) ;
		Builder.WriteString (null) ;
		Builder.WriteString (null) ;
		Builder.writeByte (0x00) ;
		
		Builder.writeByte (0xFF) ; //血條百分比
		
		Builder.writeByte (0x00) ;
		Builder.writeByte (Npc.Level) ;
		Builder.writeByte (0x00) ;
		Builder.writeByte (0xFF) ;
		Builder.writeByte (0xFF) ;
	}
	
	public byte[] getRaw () {
		return Builder.GetPacketNoPadding () ;
	}
}
