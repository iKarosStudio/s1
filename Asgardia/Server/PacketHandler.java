package Asgardia.Server;

import Asgardia.Server.Opcodes.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.Server.ClientProcess.*;

public class PacketHandler
{	
	SessionHandler Handle;
	
	public PacketHandler (SessionHandler Session) {
		Handle = Session;
	}
	
	public void Process (byte[] Data)
	{
		int Opcode = Data[0] & 0x0FF;
		
		/*
		System.out.printf ("[ IN OP:%2d]:", Opcode) ;
		for (byte b : Data) {
			System.out.printf ("0x%02X ", b) ;
		}
		System.out.print ("\n") ;
		*/
		//System.out.print ("\n--------\n") ;
		
		switch (Opcode) { //最常用到的放前面
		
		case ClientOpcodes.ITEM_USE : 
			new ItemUse (Handle, Data) ;
			break;
			
		case ClientOpcodes.ATTACK:
		case ClientOpcodes.REMOTE_ATTACK:
			new Attack (Handle, Data) ;
			break;	
			
		case ClientOpcodes.MOVE :
			new Move (Handle, Data) ;
			break;
			
		case ClientOpcodes.CHANGE_HEADING : 
			new ChangeHeading (Handle, Data) ;
			break;
			
		//case ClientOpcodes.ATTR : 
		//	break;
				
		//case useskill;
		
		case ClientOpcodes.ITEM_DROP :
			new ItemDrop (Handle, Data) ;
			break;
		
		case ClientOpcodes.ITEM_PICK :
			new ItemPick (Handle, Data) ;
			break;
		
		case ClientOpcodes.ITEM_DELETE :
			new ItemDelete (Handle, Data) ;
			break;
			
		case ClientOpcodes.TALK:
			new Talk (Handle, Data) ;
			break;
			
		case ClientOpcodes.ACCESS_NPC:
			new NpcAccess (Handle, Data) ;
			break;
		
		case ClientOpcodes.ACTION_NPC:
			new NpcAction (Handle, Data) ;
			break;
		
		case ClientOpcodes.REQUEST_NPC:
			new NpcRequest (Handle, Data) ;
			break;
		
		case ClientOpcodes.DOOR_TOUCH:
			new DoorTouch(Handle, Data) ;
			
		case ClientOpcodes.CLIENT_BEAT : //keep alive
			new GameTime (Handle) ;
			break;
		
		case ClientOpcodes.CLIENT_VERSION :
			new ServerVersion (Handle) ;
			break;
		
		case ClientOpcodes.CLIENT_CONFIG:
			new ClientConfig (Handle, Data) ;
			break;
		
		case ClientOpcodes.LOGIN_PACKET : //帳號密碼
			new AccountOperation ().Login (Handle, Data) ;
			break;
			
		case ClientOpcodes.LIST_CHARACTER : //common click in l1j
			new AccountOperation ().ReportCharactersData (Handle, Data) ; 
			break;
			
		case ClientOpcodes.CREATE_CHARACTER :
			new CharacterOperation ().Create (Handle, Data) ;
			break;
			
		case ClientOpcodes.LOGIN_TO_SERVER : //login char
			new CharacterOperation ().Login (Handle, Data) ;
			break;
			
		case ClientOpcodes.DELETE_CHARACTER :
			new CharacterOperation ().Delete (Handle, Data);
			break;
			
		case ClientOpcodes.LOGIN_TO_SERVER_DONE :
			break;
			
		case ClientOpcodes.RESTART:
			Handle.Account.ActivePc.Offline () ;
			break;
		
		case ClientOpcodes.WHO :
			Handle.SendPacket (new SystemMessage ("WHO").getRaw () ) ;
			break;
			
		case ClientOpcodes.TS : 
			new TS (Handle, Data) ;
			break;
			
		case ClientOpcodes.RST :
			if (Handle.Account.ActivePc != null) {
				Handle.Account.ActivePc.Offline () ;
			}
			break;
			
		case ClientOpcodes.EXIT_GAME :
			new ExitGame (Handle, Data) ;
			break;
			
		default:
			System.out.println ("UNKNOWN OPCODE : " + Opcode + " Length : " + Data.length) ;
			for (byte b : Data) {
				System.out.printf ("0x%02X, ", b) ;
			}
			System.out.println () ;
			/*
			try {
				Handle.Disconnect () ;
			} catch (Exception e) {
				//
			}
			*/
			break;
		}
	}
}
