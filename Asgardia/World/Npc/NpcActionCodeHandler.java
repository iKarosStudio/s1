package Asgardia.World.Npc;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;

public class NpcActionCodeHandler
{
	public NpcActionCodeHandler (SessionHandler Handle, int NpcId, String ActionCode) {
		
		//System.out.printf ("npc code:%s\n", ActionCode) ;
		
		switch (ActionCode) {
		case "buy" :
			ReportNpcShop Res = new ReportNpcShop (NpcId) ;
			Res.BuyList () ;
			Handle.SendPacket (Res.getRaw () ) ;
			
			/*
			 * item id 取得方式參考IdFactory
			 */
			
			break;
		case "sell" :
			//
			break;
			
		case "retrieve" :
			//
			break;
			
		case "retrieve-pledge" :
			//
			break;
			
		default :
			System.out.printf ("unhandled action code:%s\n", ActionCode) ;
			break;
		}
		
	}
}
