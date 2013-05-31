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
		/*
		JSONObject jo = new JSONObject();
		jo.put("action", "shipsPlacement");
		jo.put("levels", new JSONArray(mapSet));
		return jo;
		//*/
		return new JSONObject("{ \"levels\": [ { \"levelName\": \"SUBMARINE\", \"ships\": [ { \"shipId\": 9, \"center\": { \"y\": 4, \"x\": 6 }, \"shipType\": \"SUBMARINE\", \"direction\": \"SOUTH\", \"size\": 2 }, { \"shipId\": 11, \"center\": { \"y\": 2, \"x\": 2 }, \"shipType\": \"SUBMARINE\", \"direction\": \"SOUTH\", \"size\": 2 }, { \"shipId\": 12, \"center\": { \"y\": 1, \"x\": 5 }, \"shipType\": \"SUBMARINE\", \"direction\": \"SOUTH\", \"size\": 3 }, { \"shipId\": 13, \"center\": { \"y\": 3, \"x\": 3 }, \"shipType\": \"SUBMARINE\", \"direction\": \"EAST\", \"size\": 3 }, { \"shipId\": 10, \"center\": { \"y\": 4, \"x\": 2 }, \"shipType\": \"SUBMARINE\", \"direction\": \"NORTH\", \"size\": 2 } ] }, { \"levelName\": \"SURFACE\", \"ships\": [ { \"shipId\": 1, \"center\": { \"y\": 4, \"x\": 3 }, \"shipType\": \"SHIP\", \"direction\": \"SOUTH\", \"size\": 2 }, { \"shipId\": 5, \"center\": { \"y\": 8, \"x\": 1 }, \"shipType\": \"SHIP\", \"direction\": \"EAST\", \"size\": 3 }, { \"shipId\": 8, \"center\": { \"y\": 4, \"x\": 7 }, \"shipType\": \"SHIP\", \"direction\": \"EAST\", \"size\": 5 }, { \"shipId\": 6, \"center\": { \"y\": 1, \"x\": 1 }, \"shipType\": \"SHIP\", \"direction\": \"WEST\", \"size\": 4 }, { \"shipId\": 4, \"center\": { \"y\": 6, \"x\": 7 }, \"shipType\": \"SHIP\", \"direction\": \"SOUTH\", \"size\": 3 }, { \"shipId\": 7, \"center\": { \"y\": 6, \"x\": 4 }, \"shipType\": \"SHIP\", \"direction\": \"EAST\", \"size\": 4 }, { \"shipId\": 2, \"center\": { \"y\": 2, \"x\": 6 }, \"shipType\": \"SHIP\", \"direction\": \"WEST\", \"size\": 2 }, { \"shipId\": 3, \"center\": { \"y\": 8, \"x\": 7 }, \"shipType\": \"SHIP\", \"direction\": \"EAST\", \"size\": 2 } ] } ], \"action\": \"shipsPlacement\", \"uuid\": \"5f8caaa4-215f-4e88-a42b-8f909560d6e9\" }");
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
