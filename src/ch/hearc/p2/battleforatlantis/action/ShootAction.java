package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.gameengine.Box;
import ch.hearc.p2.battleforatlantis.gameengine.MapType;
import ch.hearc.p2.battleforatlantis.net.NetworkMessage;
import ch.hearc.p2.battleforatlantis.utils.Settings;

public class ShootAction extends Action implements NetworkMessage
{
	/**
	 * Box target of the shoot
	 */
	private Box target;

	/**
	 * ShootAction representing the shoot in a box of the current map
	 * 
	 * @param target Box target
	 */
	public ShootAction(Box target)
	{
		this.target = target;
	}

	/**
	 * Performs the shoot on the map
	 */
	@Override
	public void execute()
	{
		target.shoot();
	}

	/**
	 * Make the shoot action from a request received by the player
	 * 
	 * @param jo JSON Object received by the player
	 * @return The ShootAction corresponding to the request
	 */
	public static ShootAction createFromJson(JSONObject jo)
	{
		assert("fire".equals(jo.getString("action")));
		
		// Get the map level
		MapType level = MapType.valueOf(jo.getString("level"));
		
		// Get the box shot
		Box target = Settings.FRAME_MAIN.getMapByType(level, true).getBox(jo.getJSONObject("target"));
		
		return new ShootAction(target);
	}

	/**
	 * Create a JSON Object to communicate the action to the opposing player
	 */
	@Override
	public JSONObject getJson()
	{
		JSONObject jo = new JSONObject();
		
		jo.put("action", "fire");
		jo.put("level", target.getMapType().name());
		jo.put("target", target);
		
		return jo;
	}
}
