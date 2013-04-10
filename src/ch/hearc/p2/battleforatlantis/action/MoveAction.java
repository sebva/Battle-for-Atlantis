package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.gameengine.Box;
import ch.hearc.p2.battleforatlantis.gameengine.Ship;
import ch.hearc.p2.battleforatlantis.gameengine.ShipOrientation;
import ch.hearc.p2.battleforatlantis.net.NetworkMessage;

public class MoveAction extends Action implements NetworkMessage
{

	public MoveAction()
	{

	}

	@Override
	public void execute()
	{

	}

	public static MoveAction createFromJson(JSONObject jo)
	{
		return null;
	}

	@Override
	public JSONObject getJson()
	{
		return null;
	}

	public void prepare(Ship ship, Box box, ShipOrientation orientation)
	{

	}

}
