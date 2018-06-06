package Asgardia.Server.Utility;

import java.math.*;

import Asgardia.Types.*;

/*
 * 雜項計算功能
 */
public class Utility
{
	public static void Test () {
		System.out.println ("utility test") ;
	}
	
	/*
	 * 給定起點座標及方向
	 * 回傳該方向得座標
	 */
	public static Location getNextLocation (int x, int y, int heading) {
		int px = x;
		int py = y;
		
		switch (heading) {
		case 0: py--; break;
		case 1: px++; py--; break;
		case 2: px++; break;
		case 3: px++; py++; break;
		case 4: py++; break;
		case 5: px--; py++; break;
		case 6: px--; break;
		case 7: px--; py--; break;
		default: break;
		}
		
		Location nextLocation = new Location (0, px, py, heading) ;
		
		return nextLocation;
	}
}
