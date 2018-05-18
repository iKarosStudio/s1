package Asgardia.Types;

public class Location
{
	public int MapId = 0;
	public int x = 0;
	public int y = 0;
	public int Heading = 0;
	
	public Location () {
	}
	
	public Location (int map_id, int X, int Y, int heading) {
		MapId = map_id;
		x = X;
		y = Y;
		Heading = heading;
	}
}
