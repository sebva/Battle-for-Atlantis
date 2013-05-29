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

	public static ShipOrientation next(ShipOrientation value, boolean clockwise)
	{
		switch (value)
		{
			case EAST:
				return clockwise ? SOUTH : NORTH;
			case SOUTH:
				return clockwise ? WEST : EAST;
			case WEST:
				return clockwise ? NORTH : SOUTH;
			case NORTH:
				return clockwise ? EAST : WEST;
			default:
				return null;
		}
	}
}