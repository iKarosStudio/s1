package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;
import Asgardia.World.Objects.*;

public class NewCharacterPack
{
	PacketBuilder pb = new PacketBuilder () ;
	
	public NewCharacterPack (PcInstance Pc) {
		pb.WriteByte (ServerOpcodes.NEW_CHARACTER_PACK) ;
		pb.WriteString (Pc.Name) ;
		pb.WriteString ("") ;
		pb.WriteByte (Pc.Type) ;
		pb.WriteByte (Pc.Sex) ;
		pb.WriteWord (Pc.Lawful) ;
		pb.WriteWord (Pc.BasicParameter.MaxHp) ;
		pb.WriteWord (Pc.BasicParameter.MaxMp) ;
		pb.WriteByte (Pc.BasicParameter.Ac) ;
		pb.WriteByte (Pc.Level) ;
		pb.WriteByte (Pc.BasicParameter.Str) ;
		pb.WriteByte (Pc.BasicParameter.Dex) ;
		pb.WriteByte (Pc.BasicParameter.Con) ;
		pb.WriteByte (Pc.BasicParameter.Wis) ;
		pb.WriteByte (Pc.BasicParameter.Cha) ;
		pb.WriteByte (Pc.BasicParameter.Intel) ;
		pb.WriteByte (0) ;	
		
	}
	
	public byte[] getRaw () {
		return pb.GetPacket () ;
	}
}
