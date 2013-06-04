package ch.hearc.p2.battleforatlantis.sound;

import ch.hearc.p2.battleforatlantis.gameengine.MapType;

public class SoundManager
{
	/**
	 * Current instance of singleton
	 */
	private static final SoundManager instance = new SoundManager();

	/**
	 * Private constructor for singleton
	 */
	private SoundManager()
	{
	}

	/**
	 * Instance getter for singleton
	 * @return current instance
	 */
	public static SoundManager getInstance()
	{
		return SoundManager.instance;
	}

	/**
	 * Enum of available stream songs
	 */
	public enum Stream
	{
		NONE, PLACEMENT, SURFACE, SUBMARINE, ATLANTIS
	};

	/**
	 * Enum of available directions for shooting ships
	 */
	public enum Direction
	{
		SEND, GET
	};

	/**
	 * Enum of available target types for shooting ships
	 */
	public enum Target
	{
		MISS, TOUCH, SINK
	};

	/**
	 * Enum of available target types for shooting on atlantis
	 */
	public enum Atlantis
	{
		MISS, SHIELD, GENERATOR, CITY
	};
	
	/**
	 * Enum of available musics
	 */
	public enum Music
	{
		NONE, MENU, CALM, TOUCHED, FINAL
	};

	/**
	 * Set the stream song looping in background
	 * @param stream : stream type to play through
	 */
	public void setStream(Stream stream)
	{
		SoundShop.STREAM_PLACE.pause();
		SoundShop.STREAM_SUBMARINE.pause();
		SoundShop.STREAM_SURFACE.pause();
		switch (stream)
		{
			case PLACEMENT:
				SoundShop.STREAM_PLACE.play();
				break;
			case SURFACE:
				SoundShop.STREAM_SURFACE.play();
				break;
			case SUBMARINE:
				SoundShop.STREAM_SUBMARINE.play();
				break;
			case ATLANTIS:
				SoundShop.STREAM_SUBMARINE.play();
				break;
			default:
				break;
		}
	}
	
	/**
	 * Set the music song looping in background
	 * @param music : music to play loop
	 */
	public void setMusic(Music music)
	{
		SoundShop.MUSIC_MENU.pause();
		SoundShop.MUSIC_CALM.pause();
		SoundShop.MUSIC_TOUCHED.pause();
		SoundShop.MUSIC_FINAL.pause();
		switch (music)
		{
			case MENU:
				SoundShop.MUSIC_MENU.play();
				break;
			case CALM:
				SoundShop.MUSIC_CALM.play();
				break;
			case TOUCHED:
				SoundShop.MUSIC_TOUCHED.play();
				break;
			case FINAL:
				SoundShop.MUSIC_FINAL.play();
				break;
			default:
				break;
		}
	}

	/**
	 * Play a song corresponding to a level transition
	 * @param level : type of new level
	 */
	public void playNextLevel(MapType level)
	{
		switch (level)
		{
			case SURFACE:
				SoundShop.START_SURFACE.play();
				break;
			case SUBMARINE:
				SoundShop.START_SUBMARINE.play();
				break;
			case ATLANTIS:
				SoundShop.START_ATLANTIS.play();
				break;
		}
	}

	/**
	 * Play a song corresponding to a ship's placement
	 * @param type : type of map on which the ship has been placed
	 */
	public void playPlace(MapType type)
	{
		SoundShop.getPlace(type).play();
	}

	/**
	 * Play a song corresponding to a shoot on a box in surface or submarine map
	 * @param level : type of level
	 * @param direction : direction of shoot (listener is sending or receiving the shoot)
	 * @param target : type of target shot
	 */
	public void playShoot(MapType level, Direction direction, Target target)
	{
		switch (level)
		{
			case SURFACE:
				switch (direction)
				{
					case SEND:
						switch (target)
						{
							case MISS:
								SoundShop.SURFACE_SEND_MISS.play();
								break;
							case TOUCH:
								SoundShop.SURFACE_SEND_TOUCH.play();
								break;
							case SINK:
								SoundShop.SURFACE_SEND_SINK.play();
								break;
						}
						break;
					case GET:
						switch (target)
						{
							case MISS:
								SoundShop.SURFACE_GET_MISS.play();
								break;
							case TOUCH:
								SoundShop.SURFACE_GET_TOUCH.play();
								break;
							case SINK:
								SoundShop.SURFACE_GET_SINK.play();
								break;
						}
						break;
				}
				break;
			case SUBMARINE:
				switch (direction)
				{
					case SEND:
						switch (target)
						{
							case MISS:
								SoundShop.SUBMARINE_SEND_MISS.play();
								break;
							case TOUCH:
								SoundShop.SUBMARINE_SEND_TOUCH.play();
								break;
							case SINK:
								SoundShop.SUBMARINE_SEND_SINK.play();
								break;
						}
						break;
					case GET:
						switch (target)
						{
							case MISS:
								SoundShop.SUBMARINE_GET_MISS.play();
								break;
							case TOUCH:
								SoundShop.SUBMARINE_GET_TOUCH.play();
								break;
							case SINK:
								SoundShop.SUBMARINE_GET_SINK.play();
								break;
						}
						break;
				}
				break;
			default:
				break;
		}
	}

	/**
	 * Play a song corresponding to a shoot on a box in atlantis map
	 * @param target : type of target shot
	 */
	public void playShootAtlantis(Atlantis target)
	{
		switch (target)
		{
			case MISS:
				SoundShop.ATLANTIS_SEND_MISS.play();
				break;
			case SHIELD:
				SoundShop.ATLANTIS_SEND_SHIELD.play();
				break;
			case GENERATOR:
				SoundShop.ATLANTIS_SEND_GENERATOR.play();
				break;
			case CITY:
				SoundShop.ATLANTIS_SEND_CITY.play();
				break;
			default:
				break;
		}
	}

}
