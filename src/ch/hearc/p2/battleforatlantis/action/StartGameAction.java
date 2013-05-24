package ch.hearc.p2.battleforatlantis.action;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.gameengine.Map;
import ch.hearc.p2.battleforatlantis.net.NetworkMessage;
import ch.hearc.p2.battleforatlantis.utils.Settings;

public class StartGameAction extends Action implements NetworkMessage
{
	/**
	 * Set of maps belonging to the opponent player
	 */
	private Set<Map> mapSet;
	
	/**
	 * StartGameAction representing the starting of a game
	 */
	public StartGameAction()
	{
		mapSet = new HashSet<>();
	}

	/**
	 * Records all maps of the opponent player
	 */
	@Override
	public void execute()
	{
		Map[] maps = new Map[mapSet.size()];
		mapSet.toArray(maps);
		Settings.FRAME_MAIN.setDistantMaps(maps);
		
		Settings.PANEL_PREPARE.dismissWaitDialog();
	}
	
	/**
	 * Make the StartGame action from a request received by the player
	 * 
	 * @param jo JSON Object received by the player
	 * @return The StartGame corresponding to the request
	 */
	public static StartGameAction createFromJson(JSONObject jo)
	{
		assert "shipsPlacement".equals(jo.get("action")) : "Packet wrongly routed";
		
		// Get maps list
		JSONArray maps = jo.getJSONArray("levels");
		
		// Create the new StartGameAction
		StartGameAction startGameAction = new StartGameAction();
		
		// Add all maps
		for(int i = 0; i < maps.length(); i++)
		{
			startGameAction.addMap(Map.createFromJsonObject(maps.getJSONObject(i)));
		}
		
		return startGameAction;
	}

	/**
	 * Create a JSON Object to communicate the action to the opposing player 
	 */
	@Override
	public JSONObject getJson()
	{
		JSONObject jo = new JSONObject();
		jo.put("action", "shipsPlacement");
		jo.put("levels", new JSONArray(mapSet));
		return jo;
	}

	/**
	 * Add a map to the list of opponent player's maps
	 * 
	 * @param map New map to add
	 */
	public void addMap(Map map)
	{
		mapSet.add(map);
	}

}
