package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.ServerOpcodes;
import Asgardia.World.Npc.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Items.*;
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
		
		Builder.WriteByte (ServerOpcodes.NODE_PACK);
		Builder.WriteWord (Node.location.x) ;
		Builder.WriteWord (Node.location.y) ;
		Builder.WriteDoubleWord (Node.Uuid) ;
		
		Builder.WriteWord (Node.TempGfx) ; //get gfx
		//if (isDead) {
		//showDead
		//} else {
			Builder.WriteByte (Node.getWeaponGfx () ) ; //武器外型修正
		//}
		Builder.WriteByte (Node.location.Heading) ;
		Builder.WriteByte (10) ; //light
		Builder.WriteByte (Node.MoveSpeed) ; //move speed;
		
		Builder.WriteDoubleWord (Node.Exp) ;
		Builder.WriteWord (Node.Lawful) ;
		Builder.WriteString (Node.Name) ;
		Builder.WriteString (Node.Title);
		
		Node.Status = status;
		Builder.WriteByte (Node.Status) ; //status
		Builder.WriteDoubleWord (Node.ClanId) ;
		Builder.WriteString (Node.ClanName) ;
		Builder.WriteString (null) ;
		Builder.WriteByte (0x00) ;
		
		Builder.WriteByte (0xFF) ; //血條百分比
		
		Builder.WriteByte (0x00) ;
		Builder.WriteByte (0x00) ;
		Builder.WriteByte (0x00) ;
		Builder.WriteByte (0xFF) ;
		Builder.WriteByte (0xFF) ;
	}
	
	/*
	 * 報告NPC物件
	 */
	public NodePacket (NpcInstance Npc) {
		Builder.WriteByte (ServerOpcodes.NODE_PACK);
		Builder.WriteWord (Npc.location.x) ;
		Builder.WriteWord (Npc.location.y) ;
		Builder.WriteDoubleWord (Npc.Uuid) ;
		
		Builder.WriteWord (Npc.Gfx) ; //get gfx
		//if (isDead) {
		//showDead
		//} else {
			Builder.WriteByte (0) ;//weapon 0:hand 4:sword
		//}
		Builder.WriteByte (Npc.location.Heading) ;
		Builder.WriteByte (10) ; //light
		Builder.WriteByte (Npc.MoveSpeed) ; //move speed;
		
		Builder.WriteDoubleWord (0) ; //EXP
		Builder.WriteWord (0) ; //Lawful
		Builder.WriteString (Npc.NameId) ;
		Builder.WriteString (null) ;//title
		Builder.WriteByte (Npc.Status) ; //status
		Builder.WriteDoubleWord (0) ;
		Builder.WriteString (null) ;
		Builder.WriteString (null) ;
		Builder.WriteByte (0x00) ;
		
		Builder.WriteByte (0xFF) ; //血條百分比
		
		Builder.WriteByte (0x00) ;
		Builder.WriteByte (Npc.Level) ;
		Builder.WriteByte (0x00) ;
		Builder.WriteByte (0xFF) ;
		Builder.WriteByte (0xFF) ;
	}
	
	/*
	 * 報告地面物件
	 */
	public NodePacket (ItemInstance i) {
		Builder.WriteByte (ServerOpcodes.NODE_PACK) ;
		Builder.WriteWord (i.location.x) ;
		Builder.WriteWord (i.location.y) ;
		Builder.WriteDoubleWord (i.Uuid) ;
		Builder.WriteWord (i.GndGfx) ;
		Builder.WriteByte (0); //*
		Builder.WriteByte (0); //heading
		Builder.WriteByte (1) ; //light
		Builder.WriteByte (0) ; //speed
		Builder.WriteDoubleWord (i.Count) ;
		Builder.WriteByte (0) ;
		Builder.WriteByte (0) ;
		if (i.Count > 1) {
			Builder.WriteString (i.getName () ) ;
		} else {
			Builder.WriteString (i.Name) ;
		}
		Builder.WriteByte (0) ;
		Builder.WriteDoubleWord (0) ;
		Builder.WriteDoubleWord (0) ;
		Builder.WriteByte (0xFF) ;
		Builder.WriteByte (0) ;
		Builder.WriteByte (0) ;
		Builder.WriteByte (0) ;
		Builder.WriteWord (0xFFFF) ; 
		Builder.WriteDoubleWord (0) ;
		Builder.WriteByte (0) ;
		Builder.WriteByte (0) ;
	}
	
	/*
	 * 報告門物件
	 */
	public NodePacket (DoorInstance d) {
		Builder.WriteByte (ServerOpcodes.NODE_PACK) ;
		Builder.WriteWord (d.location.x) ;
		Builder.WriteWord (d.location.y) ;
		Builder.WriteDoubleWord (d.Uuid) ;
		Builder.WriteWord (d.Gfx) ;
		Builder.WriteByte (d.StatusActionCode) ;
		Builder.WriteByte (0) ;
		Builder.WriteByte (0) ;
		Builder.WriteByte (0) ;
		Builder.WriteDoubleWord (0x01) ;
		Builder.WriteWord (0) ;
		Builder.WriteString (null) ;
		Builder.WriteString (null) ;
		if (d.isVisible) {
			Builder.WriteByte (0) ;
		} else {
			Builder.WriteByte (STATUS_INVISIBLE) ;
		}
		Builder.WriteDoubleWord (0);
		Builder.WriteString (null) ;
		Builder.WriteString (null) ;
		Builder.WriteByte (0) ;
		Builder.WriteByte (0xFF) ;
		Builder.WriteByte (0) ;
		Builder.WriteByte (0) ;
		Builder.WriteByte (0) ;
		Builder.WriteByte (0xFF) ;
		Builder.WriteByte (0xFF) ;
	}	
	
	/*
	 * 報告怪物物件
	 */
	public NodePacket (MonsterInstance Npc) {
		Builder.WriteByte (ServerOpcodes.NODE_PACK);
		Builder.WriteWord (Npc.location.x) ;
		Builder.WriteWord (Npc.location.y) ;
		Builder.WriteDoubleWord (Npc.Uuid) ;
		
		Builder.WriteWord (Npc.Gfx) ; //get gfx
		if (Npc.isDead) {
		//showDead
		} else {
			Builder.WriteByte (0) ;//weapon 0:hand 4:sword
		}
		Builder.WriteByte (Npc.location.Heading) ;
		Builder.WriteByte (10) ; //light
		Builder.WriteByte (Npc.MoveSpeed) ; //move speed;
		
		Builder.WriteDoubleWord (0) ; //EXP
		Builder.WriteWord (0) ; //Lawful
		Builder.WriteString (Npc.NameId) ;
		Builder.WriteString (null) ;//title
		Builder.WriteByte (Npc.Status) ; //status
		Builder.WriteDoubleWord (0) ;
		Builder.WriteString (null) ;
		Builder.WriteString (null) ;
		Builder.WriteByte (0x00) ;
		
		Builder.WriteByte (0xFF) ; //血條百分比
		
		Builder.WriteByte (0x00) ;
		Builder.WriteByte (Npc.Level) ;
		Builder.WriteByte (0x00) ;
		Builder.WriteByte (0xFF) ;
		Builder.WriteByte (0xFF) ;
	}
	
	public byte[] getRaw () {
		return Builder.GetPacketNoPadding () ;
	}
}
