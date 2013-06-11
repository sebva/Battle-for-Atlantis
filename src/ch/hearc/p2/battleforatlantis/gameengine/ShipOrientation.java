package ch.hearc.p2.battleforatlantis.gameengine;

/**
 * Enumeration of ships orientation, in general
 */
public enum ShipOrientation
{
	NORTH, EAST, SOUTH, WEST;

	/**
	 * Get the opposite orientation of a given one
	 * 
	 * @param value Base orientation
	 * @return Opposite orientation
	 */
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

	/**
	 * Get the next orientation of a given one
	 * 
	 * @param value Base orientation
	 * @param clockwise True if rotation is clockwise, else false
	 * @return Next orientation in indicated rotation direction
	 */
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