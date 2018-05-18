package Asgardia.Server;

public class PacketCodec
{
	public int[] DecodeKeyL = {0, 0} ;
	public int[] EncodeKeyL = {0, 0} ;
	private byte[] DecodeKey = new byte[8] ;
	private byte[] EncodeKey = new byte[8] ;
	
	public synchronized void UpdateDecodeKey (byte[] Data) {
		int mask = ((Data[3] & 0xFF) << 24) | ((Data[2] & 0xFF) << 16) | ((Data[1] & 0xFF) << 8) | (Data[0] & 0xFF) ;
		DecodeKeyL[0] ^= mask;
		DecodeKeyL[1] += 0x287EFFC3;
		
		for (int i = 0; i < 8; i++) {
			if (i < 4) {
				//DecodeKey[i] = (byte) ((DecodeKeyL[0] >> (i<<3)) & 0xFF) ;
				DecodeKey[i] = (byte) (DecodeKeyL[0] >>> (i<<3)) ;
			} else {
				//DecodeKey[i] = (byte) ((DecodeKeyL[1] >> ((i - 4) << 3) ) & 0xFF) ;
				DecodeKey[i] = (byte) (DecodeKeyL[1] >>> ((i - 4) << 3)) ;
			}
		}
	}
	
	public synchronized void UpdateEncodeKey (byte[] Data) {
		int mask = ((Data[3] & 0xFF) << 24) | ((Data[2] & 0xFF) << 16) | ((Data[1] & 0xFF) << 8) | (Data[0] & 0xFF) ;
		EncodeKeyL[0] ^= mask;
		EncodeKeyL[1] += 0x287EFFC3;
		
		for (int i = 0; i < 8; i++) {
			if (i < 4) {
				//EncodeKey[i] = (byte) ((EncodeKeyL[0] >> (i << 3) ) & 0xFF) ;
				EncodeKey[i] = (byte) (EncodeKeyL[0] >>> (i << 3)) ;
			} else {
				//EncodeKey[i] = (byte) ((EncodeKeyL[1] >> ((i - 4) << 3) ) & 0xFF) ;
				EncodeKey[i] = (byte) (EncodeKeyL[1] >>> ((i - 4) << 3));
			}
		}
	}
	
	public void InitKey () {
		DecodeKeyL[0] = 0x2EAE07B2;
		DecodeKeyL[1] = 0xC1D339C3;
		
		EncodeKeyL[0] = 0x2EAE07B2;
		EncodeKeyL[1] = 0xC1D339C3;
		
		//Init Key : 0xB2 0x07 0xAE 0x2E 0xC3 0x39 0xD3 0xC1
		for (int i = 0; i < 8; i++) {
			if (i < 4) {
				DecodeKey[i] = (byte) ((DecodeKeyL[0] >> (i<<3)) & 0xFF) ;
				EncodeKey[i] = DecodeKey[i];
			} else {
				DecodeKey[i] = (byte) ((DecodeKeyL[1] >> ((i - 4)<<3)) & 0xFF) ;
				EncodeKey[i] = DecodeKey[i];
			}
		}
	}
	
	public byte[] Encode (byte[] Data) {
		int Size = Data.length;
		byte[] Raw = Data.clone () ;
		
		Raw[0] ^= EncodeKey[0];
		
		for (int i = 1; i < Size; i++) {
			Raw[i] ^= (Raw[i - 1] ^ EncodeKey[i & 0x07]) ;
		}
		
		Raw[3] = (byte) (Raw[3] ^ EncodeKey[2]) ;
		Raw[2] = (byte) (Raw[2] ^ Raw[3] ^ EncodeKey[3]) ;
		Raw[1] = (byte) (Raw[1] ^ Raw[2] ^ EncodeKey[4]) ;
		Raw[0] = (byte) (Raw[0] ^ Raw[1] ^ EncodeKey[5]) ;
		
		int EncodedDataSize = Size + 2; 
		byte[] EncodedData = new byte[EncodedDataSize] ;
		
		EncodedData[0] = (byte) (EncodedDataSize & 0xFF) ;
		EncodedData[1] = (byte) ((EncodedDataSize >> 8) & 0xFF) ;
		
		System.arraycopy (Raw, 0, EncodedData, 2, Size);
		UpdateEncodeKey (Data);
		return EncodedData;
	}
	
	public void Decode (byte[] Data, int Size) {
		byte b3 = Data[3];
		Data[3] ^= DecodeKey[2];
		
		byte b2 = Data[2]; 
		Data[2] ^= (b3 ^ DecodeKey[3]) ;
		
		byte b1 = Data[1];
		Data[1] ^= (b2 ^ DecodeKey[4]) ;
		
		byte k = (byte) (Data[0] ^ b1 ^ DecodeKey[5]);
		Data[0] = (byte) (k ^ DecodeKey[0]) ;
		
		for (int Index = 1; Index < Size; Index++) {
			byte t = Data[Index];
			Data[Index] ^= (DecodeKey[Index & 0x07] ^ k) ;
			k = t;
		}
	}
}
