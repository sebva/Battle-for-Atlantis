package ch.hearc.p2.battleforatlantis.action;

import java.util.logging.Logger;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.gameengine.Atlantis;
import ch.hearc.p2.battleforatlantis.gameengine.Box;
import ch.hearc.p2.battleforatlantis.gameengine.Generator;
import ch.hearc.p2.battleforatlantis.gameengine.MapElement;
import ch.hearc.p2.battleforatlantis.gameengine.MapType;
import ch.hearc.p2.battleforatlantis.gameengine.Player;
import ch.hearc.p2.battleforatlantis.gameengine.PlayerProgress;
import ch.hearc.p2.battleforatlantis.gameengine.Ship;
import ch.hearc.p2.battleforatlantis.net.NetworkMessage;
import ch.hearc.p2.battleforatlantis.sound.SoundManager;
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
		// Get occupier of the box shot
		MapElement occupier = this.target.getOccupier();

		int soundDelay = 0;

		// If we shot a ship
		if (occupier != null)
		{
			PlayerProgress.getInstance(Player.DISTANT).addProgress();
			int progress = PlayerProgress.getInstance(Player.DISTANT).getProgess();
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("[ShootAction::execute] Progress : " + progress);
			Settings.PANEL_PLAY.progressDistant.setValue(progress);

			if (occupier instanceof Ship)
			{
				if (occupier.getRemainingSize() > 1)
				{
					soundDelay = SoundManager.getInstance().playShoot(this.target.getMapType(), SoundManager.Direction.GET, SoundManager.Target.TOUCH);
					final int temporarySoundDelay = soundDelay;
					new Thread(new Runnable()
					{
						@Override
						public void run()
						{
							try
							{
								Thread.sleep(temporarySoundDelay);
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
							SoundManager.getInstance().setMusic(SoundManager.Music.TOUCHED);
						}
					}).start();
				}
				else
				{
					soundDelay = SoundManager.getInstance().playShoot(this.target.getMapType(), SoundManager.Direction.GET, SoundManager.Target.SINK);
					final int temporarySoundDelay = soundDelay;
					new Thread(new Runnable()
					{
						@Override
						public void run()
						{
							try
							{
								Thread.sleep(temporarySoundDelay);
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
							SoundManager.getInstance().setMusic(SoundManager.Music.CALM);
						}
					}).start();
				}
			}
			else if (occupier instanceof Generator)
			{
				soundDelay = SoundManager.getInstance().playShootAtlantis(SoundManager.Atlantis.GENERATOR);
				final int temporarySoundDelay = soundDelay;
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							Thread.sleep(temporarySoundDelay);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
						SoundManager.getInstance().setMusic(SoundManager.Music.FINAL);
					}
				}).start();
			}
			else if (occupier instanceof Atlantis)
			{
				if (((Atlantis) occupier).isDestroyable())
				{
					soundDelay = SoundManager.getInstance().playShootAtlantis(SoundManager.Atlantis.CITY);
				}
				else
				{
					soundDelay = SoundManager.getInstance().playShootAtlantis(SoundManager.Atlantis.SHIELD);
				}
			}
		}
		// Or shot nothing
		else
		{
			if (this.target.getMapType() == MapType.ATLANTIS)
			{
				soundDelay = SoundManager.getInstance().playShootAtlantis(SoundManager.Atlantis.MISS);
			}
			else
			{
				soundDelay = SoundManager.getInstance().playShoot(this.target.getMapType(), SoundManager.Direction.GET, SoundManager.Target.MISS);
			}
		}

		// Finalize the sound delay
		final int finalSoundDelay = soundDelay;

		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				try
				{
					Thread.sleep(finalSoundDelay);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				target.shoot();
				if (!(target.getOccupier() instanceof Generator) && Settings.PANEL_PLAY != null)
					Settings.PANEL_PLAY.endCurrentTurn();
			}
		}).start();
	}

	/**
	 * Make the shoot action from a request received by the player
	 * 
	 * @param jo JSON Object received by the player
	 * @return The ShootAction corresponding to the request
	 */
	public static ShootAction createFromJson(JSONObject jo)
	{
		assert ("fire".equals(jo.getString("action")));

		// Get the map level
		MapType level = MapType.valueOf(jo.getString("level"));

		// Get the box shot
		Box target = Settings.FRAME_MAIN.getMapByType(level, Player.LOCAL).getBox(jo.getJSONObject("target"));

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
