package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;

public class CharCreateResult
{
	PacketBuilder pb = new PacketBuilder () ;
	
	public static int OK = 0x02;
	public static int ALREADY_EXIST = 0x06;
	public static int INVALID_ID = 0x09;
	public static int WRONG_AMOUNT = 0x15;
	
	public CharCreateResult (int Result) {
		pb.writeByte (ServerOpcodes.CHAR_CREATE_RESULT) ;
		pb.writeByte (Result) ;
		pb.WriteDoubleWord (0) ;
		pb.WriteDoubleWord (0) ;
	}
	
	public byte[] getRaw () {
		return pb.GetPacket () ;
	}
}
