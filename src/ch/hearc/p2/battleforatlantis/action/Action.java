package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.net.NetworkMessage;

public abstract class Action implements NetworkMessage
{

	protected Action()
	{

	}

	public abstract void execute();

	// Ceci devrait être abstract, mais Java ne le permet pas (static+abstract interdit)
	public static Action createFromJson(JSONObject jo)
	{
		return null;
	}

	/**
	 * @see generalisations.NetworkMessage#getJson()
	 */
	@Override
	public JSONObject getJson()
	{
		return null;
	}

}
