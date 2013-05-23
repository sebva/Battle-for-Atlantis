package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.gameengine.Box;
import ch.hearc.p2.battleforatlantis.gameengine.MapType;
import ch.hearc.p2.battleforatlantis.net.NetworkMessage;
import ch.hearc.p2.battleforatlantis.utils.Settings;

public class ShootAction extends Action implements NetworkMessage
{
	private Box target;

	public ShootAction(Box target)
	{
		this.target = target;
	}

	@Override
	public void execute()
	{
		Settings.PANEL_PLAY.shoot(target);
	}

	public static ShootAction createFromJson(JSONObject jo)
	{
		assert("fire".equals(jo.getString("action")));
		
		MapType level = MapType.valueOf(jo.getString("level"));
		Box target = Settings.FRAME_MAIN.getMapByType(level, true).getBox(jo.getJSONObject("target"));
		
		return new ShootAction(target);
	}

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
