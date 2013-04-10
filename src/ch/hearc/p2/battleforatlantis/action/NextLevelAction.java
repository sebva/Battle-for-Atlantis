package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.gameengine.MapType;
import ch.hearc.p2.battleforatlantis.net.NetworkMessage;

public class NextLevelAction extends Action implements NetworkMessage
{

	public NextLevelAction()
	{

	}

	@Override
	public void execute()
	{

	}

	public static NextLevelAction createFromJson(JSONObject jo)
	{
		return null;
	}

	@Override
	public JSONObject getJson()
	{
		return null;
	}

	public void setLevel(MapType level)
	{

	}

}
