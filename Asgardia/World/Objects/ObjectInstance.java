package Asgardia.World.Objects;

import java.lang.*;
import java.math.*;

import Asgardia.Types.*;
import Asgardia.Config.*;
import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.*;
import Asgardia.World.Map.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Items.*;

public class ObjectInstance
{
	/* 只要是物件一定會有uuid */
	public int Uuid = 0; 
	
	public Location location = new Location () ;
	public AsgardiaMap Map = null;
	
	public boolean isVisible = true;
		
	public int UpdateCurrentMap () {
		Map = Asgardia.getInstance ().getMap (location.MapId) ;
		return location.MapId;
	}
	
	public boolean isInsight (ObjectInstance Node) {
				
		try {
			//node map編號不同
			if (location.MapId != Node.location.MapId) {
				return false;
			}
			
			return getDistance (Node.location.x, Node.location.y) < Configurations.SIGHT_RAGNE;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isInsight (ItemInstance i) {
		//node不存在地圖時		
		try {
			if (location.MapId != i.location.MapId) {
				return false;
			}
			return getDistance (i.location.x, i.location.y) < Configurations.SIGHT_RAGNE;
		} catch (Exception e) {
			return false;
		}
	}
	
	/*
	 * 自身對p(x, y)的距離
	 */
	public int getDistance (int x, int y) {
		int dx = Math.abs (x - location.x) ;
		int dy = Math.abs (y - location.y) ;
		
		return (int) Math.sqrt (Math.pow (dx, 2) + Math.pow (dy, 2) );		
	}
	
	/*
	public int getDistance (ObjectInstance p2) {
		int dx = Math.abs (p2.PosX - PosX) ;
		int dy = Math.abs (p2.PosY - PosY) ;
		return (int) Math.sqrt ( (dx*dx) + (dy*dy) ) ;
	}
	
	public int getDistance (int x1, int y1, int x2, int y2) {
		int dx = Math.abs (x2 - x1) ;
		int dy = Math.abs (y2 - y1) ;
		return (int) Math.sqrt ( (dx*dx) + (dy*dy) ) ;
	}
	*/
}
