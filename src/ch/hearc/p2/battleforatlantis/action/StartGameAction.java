package ch.hearc.p2.battleforatlantis.action;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.gameengine.Map;
import ch.hearc.p2.battleforatlantis.net.NetworkMessage;

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
		// TODO: Implement the execution
	}

	public static StartGameAction createFromJson(JSONObject jo)
	{
		return null;
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
