package Asgardia.World.Objects.RoutineTasks;

import java.util.*;
import java.util.concurrent.*;

import Asgardia.Server.*;
import Asgardia.Server.Utility.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.Objects.PcInstance;


/*
 * 經驗值需求量參考server/datatables/ExpTables.java
 */
public class ExpMonitor extends Thread implements Runnable
{
	private final Timer t = new Timer () ;
	ScheduledFuture s;
	private PcInstance Pc;
	private SessionHandler Handle;

	public ExpMonitor (PcInstance Pc) {
		this.Pc = Pc;
		Handle = Pc.getHandler () ;
	}
	
	public void run () {
		boolean ToggleSave = false;
		
		try {
			//System.out.printf ("%s Level:%d Exp:%d/%d\n", Pc.Name, Pc.Level, Pc.Exp, EXP_REQUEST_TABLE[Pc.Level]) ;
			
			while (Pc.Exp >= EXP_REQUEST_TABLE[Pc.Level] ) {
				Pc.Level ++;
				
				//升級事項
				Pc.BasicParameter.MaxHp += Utility.calcIncreaseHp (Pc.Type, Pc.Hp, Pc.getBaseMaxHp (), Pc.getBaseCon () ) ;
				Pc.BasicParameter.MaxMp += Utility.calcIncreaseMp (Pc.Type, Pc.Mp, Pc.getBaseMaxMp (), Pc.getBaseWis () ) ;
				Pc.BasicParameter.Ac += Utility.calcAcBonusFromDex (Pc.Level, Pc.getBaseDex () ) ;
				Pc.BasicParameter.Mr += Utility.calcMr (Pc.Type, Pc.Level, Pc.getBaseWis () ) ;
				Pc.BasicParameter.Sp += Utility.calcSp (Pc.Type, Pc.Level, Pc.getBaseIntel () ) ;
				
				//重新計算SP/MR
				
				System.out.printf ("%s升到了%d等級\n", Pc.Name, Pc.Level) ;
				ToggleSave = true;
			}
			
			if (ToggleSave) {
				Handle.SendPacket (new NodeStatus(Pc).getRaw () ) ;
				Handle.SendPacket (new UpdateExp(Pc).getRaw () ) ;
				DatabaseCmds.SavePc (Pc);
				ToggleSave = false;
			}
			
		} catch (Exception e) {
			System.out.printf ("%s ExpMonitor:%s\n", Pc.Name, e.toString () ) ;
			e.printStackTrace ();
		}
	}
	
	public void Start () {
		//t.scheduleAtFixedRate (this, 0, 300) ;
		s = KernelThreadPool.getInstance ().ScheduleAtFixedRate (this, 0, 300) ;
	}
	
	public void Stop () {
		t.cancel ();
		s.cancel (true) ;
	}
	
	/* 
	 * 0-99等級需求經驗值表 
	 * 經驗值的量為到當前等級所累積的總和, 並不是所需要的量
	 * */
	private static final int EXP_REQUEST_TABLE[] = {
			0, 125, 300, 500, 750, //0-4
			1296, 2401, 4096, 6581, 10000, //5-9
			14661, 20756, 28581, 38436, 50645, 0x10014,
			0x14655, 0x19a24, 0x1fd25, 0x27114, 0x2f7c5, 0x39324, 0x44535,
			0x51010, 0x5f5f1, 0x6f920, 0x81c01, 0x96110, 0xacae1, 0xc5c20,
			0xe1791, 0x100010, 0x121891, 0x146420, 0x16e5e1, 0x19a110,
			0x1c9901, 0x1fd120, 0x234cf1, 0x271010, 0x2b1e31, 0x2f7b21,
			0x342ac2, 0x393111, 0x3e9222, 0x49b332, 0x60b772, 0x960cd1,
			0x12d4c4e, 0x3539b92, 0x579ead6, 0x7a03a1a, 0x9c6895e, 0xbecd8a2,
			0xe1327e6, 0x1039772a, 0x125fc66e, 0x148615b2, 0x16ac64f6,
			0x18d2b43a, 0x1af9037e, 0x1d1f52c2, 0x1f45a206, 0x216bf14a,
			0x2392408e, 0x25b88fd2, 0x27dedf16, 0x2a052e5a, 0x2c2b7d9e,
			0x2e51cce2, 0x30781c26, 0x329e6b6a, 0x34c4baae, 0x36eb09f2,
			0x39115936, 0x3b37a87a, 0x3d5df7be, 0x3f844702, 0x41aa9646,
			0x43d0e58a, 0x45f734ce, 0x481d8412, 0x4a43d356, 0x4c6a229a,
			0x4e9071de, 0x50b6c122, 0x52dd1066, 0x55035faa, 0x5729aeee,
			0x594ffe32, 0x5b764d76, 0x5d9c9cba, 0x5fc2ebfe, 0x61e93b42,
			0x640f8a86, 0x6635d9ca, 0x685c290e, 0x6a827852, 0x6ca8c796,
			0x6ecf16da, };
}
