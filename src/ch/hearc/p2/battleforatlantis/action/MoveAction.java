package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.gameengine.Box;
import ch.hearc.p2.battleforatlantis.gameengine.Player;
import ch.hearc.p2.battleforatlantis.gameengine.Ship;
import ch.hearc.p2.battleforatlantis.gameengine.ShipOrientation;
import ch.hearc.p2.battleforatlantis.net.NetworkMessage;
import ch.hearc.p2.battleforatlantis.utils.Settings;

public class MoveAction extends Action implements NetworkMessage
{
	/**
	 * Ship making the action
	 */
	private Ship ship;

	/**
	 * Box on which the operation is performed. Represents the center of the boat
	 */
	private Box center;

	/**
	 * Orientation of the ship
	 */
	private ShipOrientation orientation;

	/**
	 * MoveAction representing the movement of a ship
	 * 
	 * @param ship Ship making the action
	 * @param center Box representing the center of the ship
	 * @param orientation Orientation of the ship
	 */
	public MoveAction(Ship ship, Box center, ShipOrientation orientation)
	{
		this.ship = ship;
		this.center = center;
		this.orientation = orientation;
	}
	
	@Override
	public void execute()
	{
		ship.moveOut();
		ship.move(center, orientation);
		Settings.PANEL_PLAY.endCurrentTurn();
	}
	
	@Override
	public JSONObject getJson()
	{
		JSONObject jo = new JSONObject();

		jo.put("action", "move");
		jo.put("shipId", ship.getId());
		jo.put("direction", orientation);
		jo.put("center", center);

		return jo;
	}

	/**
	 * Make the movement action from a request received by the player
	 * 
	 * @param jo JSON Object received by the player
	 * @return The MoveAction corresponding to the request
	 */
	public static MoveAction createFromJson(JSONObject jo)
	{
		assert "move".equals(jo.getString("action")) : "This is not a MoveAction";

		// Get the information from the JSON Object
		int shipId = jo.getInt("shipId");
		JSONObject center = jo.getJSONObject("center");
		ShipOrientation orientation = ShipOrientation.valueOf(jo.getString("direction"));

		// Get the current ship
		Ship finalShip = null;
		for (Ship ship : Settings.FRAME_MAIN.getDistantShips())
		{
			if (ship.getId() == shipId)
			{
				finalShip = ship;
				break;
			}
		}

		assert finalShip != null : "The ship with ID " + shipId + " does not exist";

		// Get the center box of the ship
		Box centerBox = Settings.PANEL_PLAY.getCurrentLevel(Player.DISTANT).getBox(center);

		// Return the MoveAction corresponding to the request
		return new MoveAction(finalShip, centerBox, orientation);
	}
}
