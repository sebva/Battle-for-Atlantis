package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.gameengine.Box;
import ch.hearc.p2.battleforatlantis.gameengine.Map;
import ch.hearc.p2.battleforatlantis.gameengine.MapType;
import ch.hearc.p2.battleforatlantis.net.NetworkMessage;
import ch.hearc.p2.battleforatlantis.utils.Settings;

public class EndGameAction extends Action implements NetworkMessage
{
	public enum EndGameCause
	{
		ATLANTIS_DESTROYED, SURRENDERED
	}
	
	private boolean won;
	private EndGameCause cause;
	private ShootAction shoot = null;
	private MapType mapType = null;
	private Box box = null;

	public EndGameAction(boolean gameWon, EndGameCause cause)
	{
		this.won = gameWon;
		this.cause = cause;
	}
	
	public EndGameAction(boolean gameWon, EndGameCause cause, Box target)
	{
		this(gameWon, cause);
		this.box = target;
		this.mapType = target.getMapType();
		this.shoot = new ShootAction(target);
	}

	@Override
	public void execute()
	{
		if(shoot != null)
			shoot.execute();
		Settings.PANEL_PLAY.endGame(won);
	}

	public static EndGameAction createFromJson(JSONObject jo)
	{
		boolean won = jo.getBoolean("victory");
		EndGameCause cause = EndGameCause.valueOf(jo.getString("cause"));
		
		if(jo.has("level") && jo.has("target"))
		{
			MapType level = MapType.valueOf(jo.getString("level"));
			Map map = Settings.FRAME_MAIN.getMapByType(level, true);
			return new EndGameAction(won, cause, map.getBox(jo.getJSONObject("target")));
		}
		else
			return new EndGameAction(won, cause);
	}

	@Override
	public JSONObject getJson()
	{
		JSONObject jo = new JSONObject();
		jo.put("action", "endGame");
		jo.put("victory", won);
		jo.put("cause", cause.name());
		
		jo.putOpt("level", mapType.name());
		jo.putOpt("target", box);
		
		return jo;
	}
}
