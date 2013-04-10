package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.gameengine.Box;
import ch.hearc.p2.battleforatlantis.net.NetworkMessage;

public class ShootAction extends Action implements NetworkMessage
{

	public ShootAction()
	{

	}

	@Override
	public void execute()
	{

	}

	public static ShootAction createFromJson(JSONObject jo)
	{
		return null;
	}

	@Override
	public JSONObject getJson()
	{
		return null;
	}

	public void setTarget(Box target)
	{

	}

}
