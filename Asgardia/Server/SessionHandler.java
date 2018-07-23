package Asgardia.Server;

import java.io.*;
import java.net.*;
import java.lang.Thread;
import java.util.logging.*;

import Asgardia.World.Objects.*;

public class SessionHandler extends Thread implements Runnable
{
	Logger Log = Logger.getLogger (SessionHandler.class.getSimpleName () ) ;
	private String HostName;
	private String IP;
	private int Port;
	private Socket Sock;

	public Accounts Account;
	
	private InputStream In;
	private OutputStream Out;
	
	private PacketHandler Handle;
	private PacketCodec Codec;
	
	private HikariCP Db;
	
	private static final byte[] INIT_PACKET = {
			(byte) 0xB1, (byte) 0x3C, (byte) 0x2C, (byte) 0x28, (byte) 0xF6,
			(byte) 0x65, (byte) 0x1D, (byte) 0xDD, (byte) 0x56, (byte) 0xE3, 
			(byte) 0xEF
	} ;
		
	public void FirstPacket () {
		byte[] InitPacket = new byte[18];
		
		InitPacket[0] = (byte) (INIT_PACKET.length + 7) ;
		InitPacket[1] = (byte) 0;
		InitPacket[2] = (byte) 0x20;
		InitPacket[3] = (byte) 0xEC;
		InitPacket[4] = (byte) 0x90;
		InitPacket[5] = (byte) 0xC6;
		InitPacket[6] = (byte) 0x5C;
		System.arraycopy (INIT_PACKET, 0, InitPacket, 7, INIT_PACKET.length);
		
		try {
			Out.write (InitPacket) ;
			Out.flush () ;
		} catch (Exception e) {
			Log.warning (e.toString () );
		}
	}
	
	/*
	 * Recieve client side RAW data packet, return a Decoded packet
	 */
	public byte[] RecievePacket () throws IOException {
		try {
			int SizeHi = In.read () ;
			int SizeLo = In.read () ;
			int Size = ((SizeLo << 8) | SizeHi) - 2;
			
			byte[] Data = new byte[Size];	
			
			In.read (Data) ;
			Codec.Decode (Data, Size) ;
			Codec.UpdateDecodeKey (Data) ;
			
			return Data;
		} catch (IOException e) {
			throw e;
		}
	}
	
	public synchronized void SendPacket (byte[] Data)  {
		byte[] Raw = null;
		try {
			Raw = Codec.Encode (Data) ;
			//Codec.UpdateEncodeKey (Data) ;
			/*
			System.out.printf ("[OUT:0x%08x, 0x%08x]:", Codec.EncodeKeyL[0], Codec.EncodeKeyL[1]) ;
			for (byte b : Data) {
				System.out.printf ("0x%02X ", b) ;
			}
			System.out.print ("\n") ;
			*/
			Out.write (Raw) ;
			Out.flush () ;
			
		} catch (Exception e) {	
			try {
				Out.close () ;
				System.out.printf ("Send Packet Exception! Close ") ;
				System.out.print (Out) ;
				System.out.printf (" Output flow\n") ;
			} catch (Exception p) {
				//
			}
			//e.printStackTrace () ;
		}
	}
	
	/*
	 * 使用者Session主要工作迴圈
	 */
	public void run () {
		FirstPacket () ;
		Codec.InitKey () ;
		
		while (true) {
			try {
				byte[] Data = RecievePacket () ; //Get decoded data
				
				/* 處理客戶端封包  */
				Handle.Process (Data) ;
				
			} catch (SocketException s) {
				/* 連線中斷 */
				break;
				
			} catch (Exception e) {
				e.printStackTrace () ;
				break;
			}
		}
		
		/*
		 * 斷線後該做的事
		 */
		if (Account != null) {
			try {
				if (!Account.ActivePc.exit) {
					Account.ActivePc.Offline () ;
				}
				Account.ActivePc = null;
				
				Account.UpdateLastLoginTime () ;
				Sock.close () ;
				//Db.Disconnect () ;
				System.out.printf ("IP:%s [Host:%s] Disconnect\n", IP, HostName) ;
			} catch (Exception e) {
				e.printStackTrace () ;
			}
		}
	}
	
	public SessionHandler () {
		//
	}
	
	public SessionHandler (Socket sock) {
		try {
			Sock = sock;
			
			In = Sock.getInputStream () ;
			Out = new BufferedOutputStream(Sock.getOutputStream () ) ;
			
			IP = Sock.getInetAddress ().getHostAddress () ;
			HostName = Sock.getInetAddress ().getHostName () ;
			Port = Sock.getPort () ;
			
			Codec = new PacketCodec () ;
			Db = HikariCP.getInstance () ;
			Handle = new PacketHandler (this) ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	public String getIP () {
		return IP;
	}
	
	public String getHostName () {
		return HostName;
	}
	
	public int getPort () {
		return Port;
	}
	
	public PcInstance getPc () {
		PcInstance Pc = null;
		if (Account != null) {
			if (Account.ActivePc != null) {
				Pc = Account.ActivePc;
			}
		}
		
		return Pc;
	}
	
	public PacketCodec getCodec () {
		return Codec;
	}
	
	public HikariCP getDbHandle () {
		return Db;
	}
	
	public OutputStream getOutputStream () {
		return Out;
	}
	
	public InputStream getInputStream () {
		return In;
	}
	
	public boolean isClosed () {
		return Sock.isClosed () ;
	}
	
	public void Disconnect () {
		try {
			Sock.close () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
}
