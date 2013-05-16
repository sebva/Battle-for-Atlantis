package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.gameengine.Box;
import ch.hearc.p2.battleforatlantis.gameengine.MapType;
import ch.hearc.p2.battleforatlantis.net.NetworkMessage;

public class EndGameAction extends Action implements NetworkMessage
{
	public enum EndGameCause
	{
		ATLANTIS_DESTROYED, SURRENDERED
	}
	private boolean won;
	private EndGameCause cause;

	public EndGameAction(boolean gameWon, EndGameCause cause)
	{

	}
	
	public EndGameAction(boolean gameWon, EndGameCause cause, MapType level, Box target)
	{

	}

	@Override
	public void execute()
	{

	}

	public static EndGameAction createFromJson(JSONObject jo)
	{
		return null;
	}

	@Override
	public JSONObject getJson()
	{
		return null;
	}
}
