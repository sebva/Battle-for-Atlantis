package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.gameengine.Box;
import ch.hearc.p2.battleforatlantis.gameengine.Ship;
import ch.hearc.p2.battleforatlantis.gameengine.ShipOrientation;
import ch.hearc.p2.battleforatlantis.net.NetworkMessage;
import ch.hearc.p2.battleforatlantis.utils.Settings;

public class MoveAction extends Action implements NetworkMessage
{
	private Ship ship;
	private Box center;
	private ShipOrientation orientation;

	public MoveAction(Ship ship, Box center, ShipOrientation orientation)
	{
		this.ship = ship;
		this.center = center;
		this.orientation = orientation;
	}

	@Override
	public void execute()
	{
		ship.move(center, orientation);
	}

	public static MoveAction createFromJson(JSONObject jo)
	{
		assert "move".equals(jo.getString("action")) : "This is not a MoveAction";
		
		int shipId = jo.getInt("shipId");
		JSONObject center = jo.getJSONObject("center");
		ShipOrientation orientation = ShipOrientation.valueOf(jo.getString("direction"));
		
		// Get the actual ship
		Ship finalShip = null;
		for(Ship ship : Settings.FRAME_MAIN.getShips())
		{
			if(ship.getId() == shipId)
			{
				finalShip = ship;
				break;
			}
		}
		if(finalShip != null)
			throw new RuntimeException("The ship with ID " + shipId + " does not exist");
		
		Box centerBox = Settings.PANEL_PLAY.getCurrentLevel(true).getBox(center);
		
		return new MoveAction(finalShip, centerBox, orientation);
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
}
