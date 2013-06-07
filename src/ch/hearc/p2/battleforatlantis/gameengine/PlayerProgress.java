package ch.hearc.p2.battleforatlantis.gameengine;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class PlayerProgress
{
	private Player playerType;
	private MapType levelMap = null;
	private Map<MapType, Integer> totalProgress;
	private Map<MapType, Integer> currentProgress;
	
	private static Map<Player, PlayerProgress> instances = new HashMap<Player, PlayerProgress>();
	
	public PlayerProgress(Player playerType)
	{
		this.playerType = playerType;
		this.levelMap = MapType.SURFACE;
		this.totalProgress = new HashMap<MapType, Integer>();
		this.currentProgress = new HashMap<MapType, Integer>();
	}
	
	public static PlayerProgress getInstance(Player playerType)
	{
		PlayerProgress instance = instances.get(playerType);
		
		synchronized (PlayerProgress.class)
		{
			if (instance == null)
			{
				instance = new PlayerProgress(playerType);
				instances.put(playerType, instance);
			}
		}
		
        return instance;
	}
	
	public void addProgress()
	{
		int progress = (currentProgress.get(levelMap) != null) ? currentProgress.get(levelMap) + 1 : 1;

		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("[PlayerProgress::addProgress/" + levelMap + "] Value updated : " + progress);
		currentProgress.put(levelMap, progress);
	}
	
	public int getProgess()
	{
		return getProgess(levelMap);
	}
	
	public int getProgess(MapType levelMap)
	{
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("[PlayerProgress::getProgress/" + levelMap + "] Current : " + currentProgress.get(levelMap));
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("[PlayerProgress::getProgress/" + levelMap + "] Total : " + totalProgress.get(levelMap));
		
		return (int) ( (currentProgress.get(levelMap) / (double) totalProgress.get(levelMap)) * 100 );
	}
	
	public void nextLevel()
	{
		if (levelMap.equals(MapType.SURFACE))
			levelMap = MapType.SUBMARINE;
		else
			levelMap = MapType.ATLANTIS;
		
		currentProgress.put(levelMap, 0);
	}
	
	public void nextLevel(MapElement[] shipList)
	{
		nextLevel();
		calculateTotalProgression(levelMap, shipList);
	}
	
	public void nextLevel(int totalProgressionValue)
	{
		nextLevel();
		calculateTotalProgression(levelMap, totalProgressionValue);
	}
	
	public void calculateTotalProgression(MapType levelMap, MapElement[] shipList)
	{
		int progressCalculate = 0;
		
		// For each MapElement to destroy by the player
		for (int i = 0; i < shipList.length; i++)
		{
			MapType shipMapType;
			
			if (shipList[i] instanceof Ship)
			{
				ShipType shipType = ((Ship)shipList[i]).getType(); 
				
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
	
	public void calculateTotalProgression(MapType levelMap, int totalProgressionValue)
	{
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("[PlayerProgress] totalProgressionValue : " + totalProgressionValue);
		
		totalProgress.put(levelMap, totalProgressionValue);
	}
	
	public Player getPlayerType()
	{
		return playerType;
	}
}
