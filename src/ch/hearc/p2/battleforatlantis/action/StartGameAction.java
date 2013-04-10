package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.gameengine.Map;
import ch.hearc.p2.battleforatlantis.net.NetworkMessage;

public class StartGameAction extends Action implements NetworkMessage
{

	public StartGameAction()
	{

	}

	@Override
	public void execute()
	{

	}

	public static StartGameAction createFromJson(JSONObject jo)
	{
		return null;
	}

	@Override
	public JSONObject getJson()
	{
		return null;
	}

	public void setMap(Map map)
	{

	}

}
