package Asgardia.Server.ClientProcess;

import Asgardia.Config.*;
import Asgardia.World.Map.*;
import Asgardia.World.Objects.*;
import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;

public class Move {
	public Move (SessionHandler Handle, byte[] Data) {
		PacketReader Reader = new PacketReader (Data) ;
		PcInstance Pc = Handle.Account.ActivePc;
		
		int tmpx = Reader.ReadWord () ; //pseudo
		int tmpy = Reader.ReadWord () ; //pseudo
		int Heading = Reader.ReadByte () ;
		
		AsgardiaMap m = Pc.Map;
		
		/*
		 * Server config 參數為3的情況下, 使用腳色原本的座標操作, 封包的座標無效
		 */
		int x = Pc.location.x;
		int y = Pc.location.y;
		
		m.setAccessible (Pc.location.x, Pc.location.y, true) ;
		
		switch (Heading) {
		case 0:
		case 73:
			Heading = 0; y--;
			break;
			
		case 1:
		case 72:
			Heading = 1; x++; y--;
			break;
			
		case 2:
		case 75:
			Heading = 2; x++;
			break;
			
		case 3:
		case 74:
			Heading = 3; x++; y++;
			break;
			
		case 4:
		case 77:
			Heading = 4; y++;
			break;	
			
		case 5:
		case 76:
			Heading = 5; x--; y++;
			break;
			
		case 6:
		case 79:
			Heading = 6; x--;
			break;
			
		case 7:
		case 78:
			Heading = 7; x--; y--;
			break;

		default : break;
		}
		
		/* 防穿檢查
		if (!Pc.CurrentMap.isPassable (x, y) ) {
			//System.out.printf ("next p(%5d, %5d) = 0x%02x is not passable\n", x, y, Pc.CurrentMap.getTile (x, y) ) ;
			//return ;
		}
		*/
		
		/*
		 * 廣播移動訊息
		 */
		//Pc.BoardcastPcInsightExceptSelf (new NodeMove (Pc.Uuid, Pc.location.x, Pc.location.y, Heading).getRaw () ) ;
		Pc.BoardcastPcInsight (new NodeMove (Pc.Uuid, Pc.location.x, Pc.location.y, Heading).getRaw () ) ;
		
		/*
		 * 檢查是否需要傳送位置
		 */
		if (Pc.Map.isInTpLocation (x, y) ) {
			new Teleport (Pc, Pc.Map.getTpDestination (x, y), false) ;
			return ;
		}
		
		/*
		 * 更新自身位置
		 */
		Pc.location.x = x;
		Pc.location.y = y;
		Pc.location.Heading = Heading;
		m.setAccessible (Pc.location.x, Pc.location.y, false) ;
	}
}
