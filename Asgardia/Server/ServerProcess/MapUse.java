package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;

public class MapUse
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public MapUse (int uuid, int item_id) {
		int map_value = 0;
		
		builder.WriteByte (ServerOpcodes.MAP_USE) ;
		builder.WriteDoubleWord (uuid) ;
		
		switch (item_id) {
		case 40373: //大陸全圖
			map_value = 16; break;
		case 40374: //說話之島
			map_value = 1; break;
		case 40375: //古魯丁
			map_value = 2; break;
		case 40376: //肯特
			map_value = 3; break;
		case 40377: //燃柳
			map_value = 4; break;
		case 40378: //妖精森林
			map_value = 5; break;
		case 40379: //風木
			map_value = 6; break;
		case 40380: //銀騎士村莊
			map_value = 7; break;
		case 40381: //龍之谷
			map_value = 8; break;
		case 40382: //奇岩
			map_value = 9; break;
		case 40383: //歌唱之島
			map_value = 10; break;
		case 40384: //隱藏之谷
			map_value = 11; break;
		case 40385: //海音
			map_value = 12; break;
		case 40386: //威頓
			map_value = 13; break;
		case 40387: //歐瑞
			map_value = 14; break;
		case 40388: //亞丁
			map_value = 15; break;
		case 40389: //沉默洞穴
			map_value = 17; break;
		case 40390: //海賊島
			map_value = 18; break;
		default :
			map_value = 1; break;
		}
		
		builder.WriteDoubleWord (map_value) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
