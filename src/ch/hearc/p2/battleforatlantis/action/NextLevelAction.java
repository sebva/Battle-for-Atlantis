package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.gameengine.MapType;
import ch.hearc.p2.battleforatlantis.net.NetworkMessage;
import ch.hearc.p2.battleforatlantis.utils.Settings;

public class NextLevelAction extends Action implements NetworkMessage
{
	private MapType map;
	
	public NextLevelAction(MapType map)
	{
		this.map = map;
	}

	@Override
	public void execute()
	{
		Settings.PANEL_PLAY.setActiveMap(map, false);
	}

	public static NextLevelAction createFromJson(JSONObject jo)
	{
		assert("nextLevel".equals(jo.getString("action")));
		
		MapType map = MapType.valueOf(jo.getString("newLevel"));
		
		return new NextLevelAction(map);
	}

	@Override
	public JSONObject getJson()
	{
		JSONObject jo = new JSONObject();
		
		jo.put("action", "nextLevel");
		jo.put("newLevel", map.name());
		
		return jo;
	}

}
