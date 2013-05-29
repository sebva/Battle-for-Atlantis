package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.gameengine.MapType;
import ch.hearc.p2.battleforatlantis.gameengine.Player;
import ch.hearc.p2.battleforatlantis.net.NetworkMessage;
import ch.hearc.p2.battleforatlantis.utils.Settings;

public class NextLevelAction extends Action implements NetworkMessage
{
	private MapType map;
	
	/**
	 * NextLevelAction representing the passage to a new level
	 */
	public NextLevelAction(MapType map)
	{
		this.map = map;
	}

	/**
	 * Move to the next level map
	 */
	@Override
	public void execute()
	{
		Settings.PANEL_PLAY.setActiveMap(map, Player.LOCAL);
	}

	/**
	 * Make the next level action from a request received by the player
	 * 
	 * @param jo JSON Object received by the player
	 * @return The NextLevelAction corresponding to the request
	 */
	public static NextLevelAction createFromJson(JSONObject jo)
	{
		assert("nextLevel".equals(jo.getString("action")));
		
		MapType map = MapType.valueOf(jo.getString("newLevel"));
		
		return new NextLevelAction(map);
	}

	/**
	 * Create a JSON Object to communicate the action to the opposing player
	 */
	@Override
	public JSONObject getJson()
	{
		JSONObject jo = new JSONObject();
		
		jo.put("action", "nextLevel");
		jo.put("newLevel", map.name());
		
		return jo;
	}

}
