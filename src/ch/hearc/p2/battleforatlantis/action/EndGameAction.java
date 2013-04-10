package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.net.NetworkMessage;

public class EndGameAction extends Action implements NetworkMessage
{
	private boolean won;

	public EndGameAction()
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

	public void setWinner(boolean isWinner)
	{

	}

}
