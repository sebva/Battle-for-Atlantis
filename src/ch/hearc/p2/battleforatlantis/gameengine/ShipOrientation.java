package ch.hearc.p2.battleforatlantis.gameengine;

public enum ShipOrientation
{
	NORTH, EAST, SOUTH, WEST;
	
	public static ShipOrientation opposite(ShipOrientation value)
	{
		switch (value)
		{
			case EAST:
				return WEST;
			case NORTH:
				return SOUTH;
			case SOUTH:
				return NORTH;
			case WEST:
				return EAST;
			default:
				return null;
		}
	}
}