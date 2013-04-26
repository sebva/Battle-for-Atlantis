package ch.hearc.p2.battleforatlantis.gameinit;

import ch.hearc.p2.battleforatlantis.gameengine.Atlantis;
import ch.hearc.p2.battleforatlantis.gameengine.Generator;
import ch.hearc.p2.battleforatlantis.gameengine.Map;

public class AtlantisCreator
{
	private static Atlantis atlantis = null;
	private static Generator generator = null;
	private static Map map = null;

	public static Map generateMap()
	{
		return null;
	}
	
	public static Generator generateGenerator()
	{
		if (AtlantisCreator.generator == null)
			AtlantisCreator.generator = AtlantisCreator.atlantis.getGenerator();
		
		return AtlantisCreator.generator;
	}
	
	public static Atlantis generateAtlantis(int width, int height) throws Exception
	{
		if (AtlantisCreator.map == null)
			throw new Exception("Can't instanciate Atlantis object without a map.");
		
		if (AtlantisCreator.atlantis == null)
			AtlantisCreator.atlantis = new Atlantis(width, height, map.getMapWidth(), map.getMapHeight());
		
		return AtlantisCreator.atlantis;
	}

	public static void setMap(Map map) 
	{
		AtlantisCreator.map = map;
	}

}
