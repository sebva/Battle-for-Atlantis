package ch.hearc.p2.battleforatlantis.gameengine;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Management of player progress into current level
 */
public class PlayerProgress
{
	/** Player type (local or distant) */
	private Player playerType;

	/** Map type (surface, submarine, atlantis) */
	private MapType levelMap = null;

	/** Total progress needed for the map */
	private Map<MapType, Integer> totalProgress;

	/** Achieved progress on the map */
	private Map<MapType, Integer> currentProgress;

	/** Instances ready to be used */
	private static Map<Player, PlayerProgress> instances = new HashMap<Player, PlayerProgress>();

	/**
	 * Default constructor
	 * 
	 * @param playerType Type of player
	 */
	public PlayerProgress(Player playerType)
	{
		this.playerType = playerType;
		this.levelMap = MapType.SURFACE;
		this.totalProgress = new HashMap<MapType, Integer>();
		this.currentProgress = new HashMap<MapType, Integer>();
	}

	/**
	 * Get the current instance for a player
	 * 
	 * @param playerType Type of player
	 * @return Instance of PanelProgress to use
	 */
	public static synchronized PlayerProgress getInstance(Player playerType)
	{
		PlayerProgress instance = instances.get(playerType);

		if (instance == null)
		{
			instance = new PlayerProgress(playerType);
			instances.put(playerType, instance);
		}

		return instance;
	}

	/**
	 * Progress into quest
	 */
	public void addProgress()
	{
		int progress = (currentProgress.get(levelMap) != null) ? currentProgress.get(levelMap) + 1 : 1;

		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("[PlayerProgress::addProgress/" + levelMap + "] Value updated : " + progress);
		currentProgress.put(levelMap, progress);
	}

	/**
	 * Get current progress
	 * 
	 * @return Current progress
	 */
	public int getProgess()
	{
		return getProgess(levelMap);
	}

	/**
	 * Get progress for a given map type
	 * 
	 * @param levelMap Map type
	 * @return Current progress
	 */
	public int getProgess(MapType levelMap)
	{
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("[PlayerProgress::getProgress/" + levelMap + "] Current : " + currentProgress.get(levelMap));
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("[PlayerProgress::getProgress/" + levelMap + "] Total : " + totalProgress.get(levelMap));

		return (int) ((currentProgress.get(levelMap) / (double) totalProgress.get(levelMap)) * 100);
	}

	/**
	 * Go to next level
	 */
	public void nextLevel()
	{
		if (levelMap.equals(MapType.SURFACE))
			levelMap = MapType.SUBMARINE;
		else
			levelMap = MapType.ATLANTIS;

		currentProgress.put(levelMap, 0);
	}

	/**
	 * Go to next level
	 * 
	 * @param shipList List of ships to consider in computation
	 */
	public void nextLevel(MapElement[] shipList)
	{
		nextLevel();
		calculateTotalProgression(levelMap, shipList);
	}

	/**
	 * Go to next level
	 * 
	 * @param totalProgressionValue Value of total progress to directly consider
	 */
	public void nextLevel(int totalProgressionValue)
	{
		nextLevel();
		calculateTotalProgression(levelMap, totalProgressionValue);
	}

	/**
	 * Calculate total progression possible
	 * 
	 * @param levelMap Type of level
	 * @param shipList List of ships to consider
	 */
	public void calculateTotalProgression(MapType levelMap, MapElement[] shipList)
	{
		int progressCalculate = 0;

		// For each MapElement to destroy by the player
		for (int i = 0; i < shipList.length; i++)
		{
			MapType shipMapType;

			if (shipList[i] instanceof Ship)
			{
				ShipType shipType = ((Ship) shipList[i]).getType();

				// Ship / SURFACE
				if (shipType.equals(ShipType.SHIP))
					shipMapType = MapType.SURFACE;

				// Submarine / SUBMARINE
				else
					shipMapType = MapType.SUBMARINE;
			}
			// Atlantis + Generator
			else
			{
				shipMapType = MapType.ATLANTIS;
			}

			// Check if the ship indicated is for the current level
			if (shipMapType.equals(levelMap))
			{
				// Add the value of the ship to the total progression
				progressCalculate += shipList[i].getWholeSize();
			}
		}

		calculateTotalProgression(levelMap, progressCalculate);
	}

	/**
	 * Calculate total progression possible
	 * 
	 * @param levelMap Type of level
	 * @param totalProgressionValue Value of total progression
	 */
	public void calculateTotalProgression(MapType levelMap, int totalProgressionValue)
	{
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("[PlayerProgress] totalProgressionValue : " + totalProgressionValue);

		totalProgress.put(levelMap, totalProgressionValue);
	}

	/**
	 * Get type of player in current progress instance
	 * 
	 * @return Type of player
	 */
	public Player getPlayerType()
	{
		return playerType;
	}
}
