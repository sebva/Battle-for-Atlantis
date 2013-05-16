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
	private Set<Map> mapSet;
	
	public StartGameAction()
	{
		mapSet = new HashSet<>();
	}

	@Override
	public void execute()
	{
		Map[] maps = new Map[mapSet.size()];
		mapSet.toArray(maps);
		Settings.FRAME_MAIN.setDistantMaps(maps);
	}

	public static StartGameAction createFromJson(JSONObject jo)
	{
		assert "shipsPlacement".equals(jo.get("action")) : "Packet wrongly routed";
		JSONArray maps = jo.getJSONArray("levels");
		
		StartGameAction startGameAction = new StartGameAction();
		
		for(int i = 0; i < maps.length(); i++)
		{
			startGameAction.addMap(Map.createFromJsonObject(maps.getJSONObject(i)));
		}
		
		return startGameAction;
	}

	@Override
	public JSONObject getJson()
	{
		JSONObject jo = new JSONObject();
		jo.put("action", "shipsPlacement");
		jo.put("levels", new JSONArray(mapSet));
		return jo;
	}

	public void addMap(Map map)
	{
		mapSet.add(map);
	}

}
