package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;
import Asgardia.World.*;

public class ReportWeather
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public ReportWeather (int weather) {
		builder.WriteByte (ServerOpcodes.WEATHER) ;
		builder.WriteByte (weather) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
