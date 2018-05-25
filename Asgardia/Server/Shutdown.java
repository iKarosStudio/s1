package Asgardia.Server;

public class Shutdown extends Thread implements Runnable
{
	public void run () {
		System.out.println ("Initiating shutdown process") ;
	}
	
	public Shutdown () {
		//
	}
}
