package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.net.NetworkMessage;
import ch.hearc.p2.battleforatlantis.utils.Settings;

public class NextLevelAction extends Action implements NetworkMessage
{
	
	public NextLevelAction()
	{
		// Nothin'
	}

	@Override
	public void execute()
	{
		Settings.PANEL_PLAY.nextLevel();
	}

	public static NextLevelAction createFromJson(JSONObject jo)
	{
		assert("nextLevel".equals(jo.getString("action")));
		
		return new NextLevelAction();
	}

	@Override
	public JSONObject getJson()
	{
		JSONObject jo = new JSONObject();
		
		jo.put("action", "nextLevel");
		
		return jo;
	}

}
