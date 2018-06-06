package Asgardia.Server.ClientProcess;

import Asgardia.Server.*;

public class ClientConfig
{
	public ClientConfig (SessionHandler Handle, byte[] Data) {
		PacketReader reader = new PacketReader (Data) ;
		int Length = reader.ReadDoubleWord () - 3;
		byte[] Conf = reader.ReadRaw () ;
		
		/*
		System.out.println ("Client config:") ;
		for (int i = 0; i < Conf.length; i++) {
			if ((i % 16) == 0 && (i > 0) ) {
				System.out.println () ;
			}
			System.out.printf ("%02X ", Conf[i]) ;
			
		}
		System.out.println () ;
		*/
	}
}
