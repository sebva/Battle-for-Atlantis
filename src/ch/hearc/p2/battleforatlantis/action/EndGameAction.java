package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.gameengine.Box;
import ch.hearc.p2.battleforatlantis.gameengine.Map;
import ch.hearc.p2.battleforatlantis.gameengine.MapType;
import ch.hearc.p2.battleforatlantis.gameengine.Player;
import ch.hearc.p2.battleforatlantis.net.NetworkMessage;
import ch.hearc.p2.battleforatlantis.utils.Settings;

public class EndGameAction extends Action implements NetworkMessage
{
	/**
	 * Why the party ended
	 */
	public enum EndGameCause
	{
		ATLANTIS_DESTROYED, SURRENDERED
	}

	/**
	 * Indicates if the player has won or lost the game
	 */
	private boolean won;

	/**
	 * Indicates the cause of the end of the game
	 */
	private EndGameCause cause;

	/**
	 * Indicates the last shot
	 */
	private ShootAction shoot = null;

	/**
	 * Indicate the map type
	 */
	private MapType mapType = null;

	/**
	 * Indicates the box shot
	 */
	private Box box = null;

	/**
	 * EndGameAction representing the end of the game
	 * 
	 * @param gameWon Game won or lost by the player
	 * @param cause Cause of the end of the game
	 */
	public EndGameAction(boolean gameWon, EndGameCause cause)
	{
		this.won = gameWon;
		this.cause = cause;
	}

	/**
	 * EndGameAction representing the end of the game with the last shoot
	 * 
	 * @param gameWon Game won or lost by the player
	 * @param cause Cause of the end of the game
	 * @param target Last shot target
	 */
	public EndGameAction(boolean gameWon, EndGameCause cause, Box target)
	{
		this(gameWon, cause);
		this.box = target;
		this.mapType = target.getMapType();
		this.shoot = new ShootAction(target);
	}

	/**
	 * Execute the last shoot and the end of the game
	 */
	@Override
	public void execute()
	{
		if (shoot != null)
			shoot.execute();
		Settings.PANEL_PLAY.endGame(won, true);
	}

	/**
	 * Make the EndGame action from a request received by the player
	 * 
	 * @param jo JSON Object received by the player
	 * @return The EndGame corresponding to the request
	 */
	public static EndGameAction createFromJson(JSONObject jo)
	{
		// Get the won or lost information
		boolean won = jo.getBoolean("victory");

		// Get the end game cause
		EndGameCause cause = EndGameCause.valueOf(jo.getString("cause"));

		// If the JSON contains a last shot
		if (jo.has("level") && jo.has("target"))
		{
			MapType level = MapType.valueOf(jo.getString("level"));
			Map map = Settings.FRAME_MAIN.getMapByType(level, Player.LOCAL);
			return new EndGameAction(won, cause, map.getBox(jo.getJSONObject("target")));
		}
		// Otherwise
		else
			return new EndGameAction(won, cause);
	}

	/**
	 * Create a JSON Object to communicate the end game to the opposing player
	 */
	@Override
	public JSONObject getJson()
	{
		JSONObject jo = new JSONObject();
		jo.put("action", "endGame");
		jo.put("victory", won);
		jo.put("cause", cause.name());

		jo.putOpt("level", mapType);
		jo.putOpt("target", box);

		return jo;
	}
}
