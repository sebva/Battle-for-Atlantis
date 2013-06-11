package ch.hearc.p2.battleforatlantis.gameinit;

import ch.hearc.p2.battleforatlantis.gameengine.Atlantis;
import ch.hearc.p2.battleforatlantis.gameengine.Generator;
import ch.hearc.p2.battleforatlantis.gameengine.Map;
import ch.hearc.p2.battleforatlantis.gameengine.MapType;

public class AtlantisCreator
{
	/**
	 * Atlantis object
	 */
	private static Atlantis atlantis = null;

	/**
	 * Generator object
	 */
	private static Generator generator = null;

	/**
	 * Map object containing the Atlantis level
	 */
	private static Map map = null;

	/**
	 * Generate the Atlantis Generator (only one). When already created, returns this one.
	 * 
	 * @return Atlantis Generator Object
	 * @throws Exception Atlantis object not defined
	 */
	public static Generator generateGenerator() throws Exception
	{
		if (AtlantisCreator.atlantis == null)
			throw new Exception("[AtlantisCreator::generateGenerator] Can't generate Generator without Atlantis object.");

		if (AtlantisCreator.generator == null)
			AtlantisCreator.generator = AtlantisCreator.atlantis.getGenerator();

		return AtlantisCreator.generator;
	}

	/**
	 * Generate the Atlantis (only one). When already created, returns this one.
	 * 
	 * @param width Atlantis Width Setting
	 * @param height Atlantis Height Setting
	 * @return Atlantis Object
	 * @throws Exception Map Not Found
	 */
	public static Atlantis generateAtlantis(int width, int height) throws Exception
	{
		if (AtlantisCreator.map == null)
			throw new Exception("[AtlantisCreator::generateAtlantis] Can't instanciate Atlantis object without a map.");

		if (AtlantisCreator.atlantis == null)
		{
			AtlantisCreator.atlantis = new Atlantis(width, height, map);
			AtlantisCreator.atlantis.generatePosition();
		}

		return AtlantisCreator.atlantis;
	}

	/**
	 * Set the Atlantis Level Map
	 * 
	 * @param map Atlantis Map
	 * @throws Exception Invalid Map Type
	 */
	public static void setMap(Map map) throws Exception
	{
		if (map.getType() != MapType.ATLANTIS)
			throw new Exception("[AtlantisCreator::setMap] Map Type is not the Atlantis Level");

		AtlantisCreator.map = map;
	}

	/**
	 * Retrieve the Atlantis Level Map
	 * 
	 * @return Atlantis Level Map
	 */
	public static Map getMap()
	{
		return AtlantisCreator.map;
	}

	/**
	 * Clear the objects for a new game
	 */
	public static void clear()
	{
		AtlantisCreator.atlantis = null;
		AtlantisCreator.generator = null;
		AtlantisCreator.map = null;
	}
}
