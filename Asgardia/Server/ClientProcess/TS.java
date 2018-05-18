package Asgardia.Server.ClientProcess;

import Asgardia.Server.*;

public class TS
{
	public TS (SessionHandler Handle, byte[] Data) {
		PacketReader reader = new PacketReader (Data) ;
		
		int unknown = reader.ReadByte () ;
		String Content = reader.ReadString () ;
		String Subject = reader.ReadString () ;
		
		System.out.printf ("TS標題:%s\nTS內容:%s\n", Subject, Content) ;
	}
}
