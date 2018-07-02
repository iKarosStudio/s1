package Asgardia.Blockchain;

public class BlockchainMain extends Thread implements Runnable
{
	private static BlockchainMain instance ;
	
	public void run () {
		try {
			//
			
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	public static BlockchainMain getInstance () {
		if (instance == null) {
			instance = new BlockchainMain () ;
		}
		
		return instance;
	}
	
	public BlockchainMain () {
		try {
			System.out.println ("Blockchain initializing...") ;
			
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
}
