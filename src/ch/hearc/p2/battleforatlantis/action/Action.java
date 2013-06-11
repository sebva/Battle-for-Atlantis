package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.net.NetworkManager;
import ch.hearc.p2.battleforatlantis.net.NetworkMessage;

public abstract class Action implements NetworkMessage
{
	/**
	 * Default constructor
	 */
	protected Action()
	{
		// Nothing
	}

	/**
	 * Abstract method for action execution, implemented in real actions
	 */
	public abstract void execute();
	
	@Override
	public abstract JSONObject getJson();

	/**
	 * Send the action over current network connection
	 */
	public void send()
	{
		NetworkManager.getInstance().send(this);
	}

	/**
	 * <pre>
	 * Secondary static constructor for create actions from JSON
	 * This should be abstract, however a method cannot be static and abstract in Java
	 * </pre>
	 * @param jo JSON code to convert to Action
	 * @return Action ready to be executed
	 */
	public static Action createFromJson(JSONObject jo)
	{
		return null;
	}
}
