package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.gameengine.MapType;
import ch.hearc.p2.battleforatlantis.gameengine.Player;
import ch.hearc.p2.battleforatlantis.gameengine.PlayerProgress;
import ch.hearc.p2.battleforatlantis.net.NetworkMessage;
import ch.hearc.p2.battleforatlantis.sound.SoundManager;
import ch.hearc.p2.battleforatlantis.utils.Settings;

public class NextLevelAction extends Action implements NetworkMessage
{
	/**
	 * New map type to be applied
	 */
	private MapType map;

	/**
	 * NextLevelAction representing the passage to a new level
	 */
	public NextLevelAction(MapType map)
	{
		this.map = map;
	}
	
	@Override
	public void execute()
	{
		switch (this.map)
		{
			case SUBMARINE:
				SoundManager.getInstance().playVoice(SoundManager.Voice.SUBMARINE);
				break;
			case ATLANTIS:
				SoundManager.getInstance().playVoice(SoundManager.Voice.ATLANTIS);
				break;
			default:
				break;
		}
		
		PlayerProgress.getInstance(Player.DISTANT).nextLevel(Settings.PANEL_PLAY.getDistantShip());
		Settings.PANEL_PLAY.progressDistant.setValue(PlayerProgress.getInstance(Player.DISTANT).getProgess());
		
		Settings.PANEL_PLAY.setActiveMap(map, Player.LOCAL);
		Settings.PANEL_PLAY.endCurrentTurn();
	}
	
	@Override
	public JSONObject getJson()
	{
		JSONObject jo = new JSONObject();

		jo.put("action", "nextLevel");
		jo.put("newLevel", map.name());

		return jo;
	}

	/**
	 * Make the next level action from a request received by the player
	 * 
	 * @param jo JSON Object received by the player
	 * @return The NextLevelAction corresponding to the request
	 */
	public static NextLevelAction createFromJson(JSONObject jo)
	{
		assert ("nextLevel".equals(jo.getString("action")));

		MapType map = MapType.valueOf(jo.getString("newLevel"));

		return new NextLevelAction(map);
	}

}
