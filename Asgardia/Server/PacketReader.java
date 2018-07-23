package Asgardia.Server;

public class PacketReader
{
	private int offset = 1;
	private byte[] Raw;
	
	public PacketReader (byte[] Data) {
		Raw = Data;
	}
	
	public byte[] ReadRaw () {
		byte[] result = new byte[Raw.length - offset] ;
		try {
			System.arraycopy (Raw, offset, result, 0, Raw.length - offset);
			offset = Raw.length;
		} catch (Exception e) {e.printStackTrace () ; }
		
		return result;
	}

	public byte ReadByte () {
		byte b = Raw[offset];
		offset++;
		return b;
	}
	
	public int ReadWord () {
		int w = ((Raw[offset+1] << 8) & 0xFF00) | (Raw[offset] & 0xFF) ;
		offset += 2;
		return w;
	}
	
	public int ReadDoubleWord () {
		int dw = ((Raw[offset+3] & 0xFF) << 24) | ((Raw[offset+2] & 0xFF) << 16) | ((Raw[offset+1] & 0xFF) << 8) | (Raw[offset] & 0xFF) ;
		offset += 4;
		return dw;
	}
	
	public String ReadString () {
		String Parse = null;
		try{
			Parse = new String (Raw, offset, Raw.length - offset, "BIG5");
			Parse = Parse.substring (0, Parse.indexOf ('\0') ) ;
			offset += Parse.getBytes ("BIG5").length + 1;
			
			return Parse;
		} catch (Exception e) {
			e.printStackTrace () ;
			return null;
		}
	}
	
	/*
	public String ReadTextString () {
		String Parse = null;
		try{
			Parse = new String (Raw, offset, Raw.length - offset, "BIG5");
			Parse = Parse.substring (0, Parse.indexOf ("\0\0") ) ;
			offset += Parse.getBytes ("MS950").length + 1;
			
			return Parse;
		} catch (Exception e) {
			e.printStackTrace () ;
			return null;
		}
	}
	*/
}
