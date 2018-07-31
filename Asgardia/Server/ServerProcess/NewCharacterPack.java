package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;
import Asgardia.World.Objects.*;

public class NewCharacterPack
{
	PacketBuilder pb = new PacketBuilder () ;
	
	public NewCharacterPack (PcInstance Pc) {
		pb.writeByte (ServerOpcodes.NEW_CHARACTER_PACK) ;
		pb.WriteString (Pc.Name) ;
		pb.WriteString ("") ;
		pb.writeByte (Pc.Type) ;
		pb.writeByte (Pc.Sex) ;
		pb.WriteWord (Pc.Lawful) ;
		pb.WriteWord (Pc.BasicParameter.MaxHp) ;
		pb.WriteWord (Pc.BasicParameter.MaxMp) ;
		pb.writeByte (Pc.BasicParameter.Ac) ;
		pb.writeByte (Pc.Level) ;
		pb.writeByte (Pc.BasicParameter.Str) ;
		pb.writeByte (Pc.BasicParameter.Dex) ;
		pb.writeByte (Pc.BasicParameter.Con) ;
		pb.writeByte (Pc.BasicParameter.Wis) ;
		pb.writeByte (Pc.BasicParameter.Cha) ;
		pb.writeByte (Pc.BasicParameter.Intel) ;
		pb.writeByte (0) ;	
		
	}
	
	public byte[] getRaw () {
		return pb.GetPacket () ;
	}
}
