package Asgardia.World.Objects;

import java.util.*;

import Asgardia.Types.*;
import Asgardia.World.*;
import Asgardia.World.Map.*;
import Asgardia.World.Objects.Static.*;

public class DoorInstance extends StaticObject
{
	/*
	 * Direction用location.heading標示
	 */
	public static final int STATUS_DOOR_CLOSE = 29;
	public static final int STATUS_DOOR_OPEN = 28;
	
	public int StatusActionCode = STATUS_DOOR_CLOSE;
	public boolean isOpened = false;
	public boolean isKeeper = false;
	public Location Entracne = new Location () ;
	public int Hp;
	public int Size;
	public int KeyId; /* 需要特定道具才可以打開 */
	public int Castle;
	public int Order;
	public String Note;
	
	public DoorInstance (int uuid, String note, int gfx, int x, int y, int mapid, int direction, int entrance_x, int entrance_y, int hp, boolean keeper, int key, int size, int castle, int order) {
		Uuid = uuid;
		Note = note;
		Gfx = gfx;
		location.x = x;
		location.y = y;
		location.MapId = mapid;
		location.Heading = direction;
		Entracne.x = entrance_x;
		Entracne.y = entrance_y;
		Hp = hp;
		isKeeper = keeper;
		KeyId = key;
		Size = size;
		Castle = castle;
		Order = order;
	}
	
	public void BoardcastPcInsight (byte[] Data) {
		AsgardiaMap map = Asgardia.getInstance ().getMap (location.MapId) ;
		List<PcInstance> pcs = map.getPcInstance (location.x, location.y) ;
		for (PcInstance p : pcs) {
			p.getHandler ().SendPacket (Data) ;
		}
		pcs = null;
	}
	
	public void open () {
		isOpened = true;
		StatusActionCode = STATUS_DOOR_OPEN;
	}
	
	public void close () {
		isOpened = false;
		StatusActionCode = STATUS_DOOR_CLOSE;
	}
}
