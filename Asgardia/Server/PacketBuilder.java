package Asgardia.Server;

import java.io.ByteArrayOutputStream;

public class PacketBuilder
{
	ByteArrayOutputStream Packet = new ByteArrayOutputStream () ;
	private int offset = 1;
	
	public PacketBuilder () {
		//Packet = new ByteArrayOutputStream () ;
		//Packet.reset () ; 
	}
	
	public void WriteByte (int Value) {
		writeByte (Value);
	}

	public void writeByte (int Value) {
		Packet.write (Value & 0xFF) ;
	}
	
	public void WriteByte (byte[] Value) {
		for (byte b : Value) {
			Packet.write (b) ;
		}
	}
	
	public void WriteByte (boolean Value) {
		if (Value) {
			Packet.write (1) ;
		} else {
			Packet.write (0) ;
		}
	}
	
	public void WriteWord (int Value) {
		Packet.write (Value & 0xFF) ;
		Packet.write ((Value >> 8) & 0xFF) ;
	}
	
	public void WriteDoubleWord (int Value) {
		Packet.write (Value & 0xFF) ;
		Packet.write ((Value >>  8) & 0xFF) ;
		Packet.write ((Value >> 16) & 0xFF) ;
		Packet.write ((Value >> 24) & 0xFF) ;
	}
	
	public void WriteFloat (Float Value) {
		//
	}
	
	public void WriteString (String Str) {
		if (Str == null) {
			//
		} else {
			try {
				Packet.write (Str.getBytes ("BIG5") ) ;
			} catch (Exception e) {
				System.out.println (e.toString () ) ;
			}
		}
		Packet.write (0) ;//結束字元
	}
	
	public void Reset () {
		Packet.reset () ;
	}
	
	public byte[] GetPacket () {
		int Padding = Packet.size () % 4; //封包4byte資料對齊
		
		if (Padding != 0) {
			for (int i = Padding; i < 4; i++) {
				Packet.write (0x00) ;
			}
		}
		
		return Packet.toByteArray () ;
	}
	
	public byte[] GetPacketNoPadding () {
		return Packet.toByteArray () ;
	}
}
