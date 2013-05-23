package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.net.NetworkManager;
import ch.hearc.p2.battleforatlantis.net.NetworkMessage;

public abstract class Action implements NetworkMessage
{
	protected Action()
	{

	}

	public abstract void execute();

	// This should be abstract, however a method cannot be static and abstract in Java
	public static Action createFromJson(JSONObject jo)
	{
		return null;
	}
	
	@Override
	public abstract JSONObject getJson();
	
	public void send()
	{
		NetworkManager.getInstance().send(this);
	}
}
