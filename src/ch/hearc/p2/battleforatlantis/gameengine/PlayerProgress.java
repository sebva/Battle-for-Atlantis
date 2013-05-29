package ch.hearc.p2.battleforatlantis.gameengine;

import java.util.HashMap;
import java.util.Set;
import java.util.Map;

public class PlayerProgress
{
	private Player playerType;
	private MapType levelMap;
	private Map<Integer, Integer> progressValueShip = new HashMap<Integer, Integer>();
	
	public PlayerProgress(Player playerType)
	{
		this.playerType = playerType;
		this.levelMap = MapType.SURFACE;
	}
	
	public void progress(MapElement elementShot)
	{
		int shipSize = elementShot.getWholeSize();
		int value = getProgess(shipSize);
		
		value = (value >= 100) ? 100 : value + 1;
		
		progressValueShip.put(shipSize, value + 1);
	}
	
	public int getProgess(int shipSize)
	{
		if (progressValueShip.containsKey(shipSize))
			return progressValueShip.get(shipSize);
		else
			return 0;
	}
	
	public Set<Integer> getShipSizeList()
	{
		return progressValueShip.keySet();
	}
	
	public void nextLevel()
	{
		if (levelMap.equals(MapType.SURFACE))
			levelMap = MapType.SUBMARINE;
		else
			levelMap = MapType.ATLANTIS;
	}
	
	public Player getPlayerType()
	{
		return playerType;
	}
}
