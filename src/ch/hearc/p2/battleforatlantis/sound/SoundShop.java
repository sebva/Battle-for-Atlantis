package ch.hearc.p2.battleforatlantis.sound;

import java.net.StandardProtocolFamily;

import ch.hearc.p2.battleforatlantis.gameengine.MapType;

public final class SoundShop
{
	private static final String SOUND = "/sound/";
	private static final String FX = SOUND + "fx/";
	private static final String MUSIC = SOUND + "music/";
	private static final String VOICE = SOUND + "voice/";

	// Streams
	public static final SoundPlayer STREAM_PLACE = new SoundPlayer(FX + "surface_place_stream.wav", SoundPlayer.LoopMode.LOOP);
	public static final SoundPlayer STREAM_SURFACE = new SoundPlayer(FX + "surface_shoot_stream.wav", SoundPlayer.LoopMode.LOOP);
	public static final SoundPlayer STREAM_SUBMARINE = new SoundPlayer(FX + "submarine_shoot_stream.wav", SoundPlayer.LoopMode.LOOP);

	// Level starters
	public static final SoundPlayer START_SURFACE = new SoundPlayer(FX + "start_surface.wav", SoundPlayer.LoopMode.ONCE);
	public static final SoundPlayer START_SUBMARINE = new SoundPlayer(FX + "start_submarine.wav", SoundPlayer.LoopMode.ONCE);
	public static final SoundPlayer START_ATLANTIS = new SoundPlayer(FX + "start_atlantis.wav", SoundPlayer.LoopMode.ONCE);

	// Placement
	private static final String PLACE_SURFACE = FX + "surface_place.wav";
	private static final String PLACE_SUBMARINE = FX + "submarine_place.wav";
	public static SoundPlayer getPlace(MapType type)
	{
		switch (type)
		{
			case SURFACE:
				return new SoundPlayer(PLACE_SURFACE, SoundPlayer.LoopMode.ONCE);
			case SUBMARINE:
				return new SoundPlayer(PLACE_SUBMARINE, SoundPlayer.LoopMode.ONCE);
			default:
					return null;
		}
	}

	// Surface shoots
	public static final SoundPlayer SURFACE_SEND_MISS = new SoundPlayer(FX + "surface_send_miss.wav", SoundPlayer.LoopMode.ONCE);
	public static final SoundPlayer SURFACE_SEND_TOUCH = new SoundPlayer(FX + "surface_send_touch.wav", SoundPlayer.LoopMode.ONCE);
	public static final SoundPlayer SURFACE_SEND_SINK = new SoundPlayer(FX + "surface_send_sink.wav", SoundPlayer.LoopMode.ONCE);
	public static final SoundPlayer SURFACE_GET_MISS = new SoundPlayer(FX + "surface_get_miss.wav", SoundPlayer.LoopMode.ONCE);
	public static final SoundPlayer SURFACE_GET_TOUCH = new SoundPlayer(FX + "surface_get_touch.wav", SoundPlayer.LoopMode.ONCE);
	public static final SoundPlayer SURFACE_GET_SINK = new SoundPlayer(FX + "surface_get_sink.wav", SoundPlayer.LoopMode.ONCE);

	// Submarine shoots
	public static final SoundPlayer SUBMARINE_SEND_MISS = new SoundPlayer(FX + "submarine_send_miss.wav", SoundPlayer.LoopMode.ONCE);
	public static final SoundPlayer SUBMARINE_SEND_TOUCH = new SoundPlayer(FX + "submarine_send_touch.wav", SoundPlayer.LoopMode.ONCE);
	public static final SoundPlayer SUBMARINE_SEND_SINK = new SoundPlayer(FX + "submarine_send_sink.wav", SoundPlayer.LoopMode.ONCE);
	public static final SoundPlayer SUBMARINE_GET_MISS = new SoundPlayer(FX + "submarine_get_miss.wav", SoundPlayer.LoopMode.ONCE);
	public static final SoundPlayer SUBMARINE_GET_TOUCH = new SoundPlayer(FX + "submarine_get_touch.wav", SoundPlayer.LoopMode.ONCE);
	public static final SoundPlayer SUBMARINE_GET_SINK = new SoundPlayer(FX + "submarine_get_sink.wav", SoundPlayer.LoopMode.ONCE);

	// Atlantis shoots
	public static final SoundPlayer ATLANTIS_SEND_MISS = new SoundPlayer(FX + "atlantis_send_miss.wav", SoundPlayer.LoopMode.ONCE);
	public static final SoundPlayer ATLANTIS_SEND_SHIELD = new SoundPlayer(FX + "atlantis_send_shield.wav", SoundPlayer.LoopMode.ONCE);
	public static final SoundPlayer ATLANTIS_SEND_GENERATOR = new SoundPlayer(FX + "atlantis_send_generator.wav", SoundPlayer.LoopMode.ONCE);
	public static final SoundPlayer ATLANTIS_SEND_CITY = new SoundPlayer(FX + "atlantis_send_city.wav", SoundPlayer.LoopMode.ONCE);
	
	// Musics
	public static final SoundPlayer MUSIC_MENU = new SoundPlayer(MUSIC + "menu16.wav", SoundPlayer.LoopMode.LOOP);
	public static final SoundPlayer MUSIC_CALM = new SoundPlayer(MUSIC + "game_calm_loop.wav", SoundPlayer.LoopMode.LOOP);
	public static final SoundPlayer MUSIC_TOUCHED = new SoundPlayer(MUSIC + "game_touched_loop.wav", SoundPlayer.LoopMode.LOOP);
	public static final SoundPlayer MUSIC_FINAL = new SoundPlayer(MUSIC + "game_final_loop.wav", SoundPlayer.LoopMode.LOOP);
	
	// Voices
	public static final SoundPlayer VOICE_VICTORY = new SoundPlayer(VOICE + "voice_victory.wav", SoundPlayer.LoopMode.ONCE);
	public static final SoundPlayer VOICE_DEFEAT = new SoundPlayer(VOICE + "voice_defeat.wav", SoundPlayer.LoopMode.ONCE);
	public static final SoundPlayer VOICE_SUBMARINE = new SoundPlayer(VOICE + "voice_submarine.wav", SoundPlayer.LoopMode.ONCE);
	public static final SoundPlayer VOICE_ATLANTIS = new SoundPlayer(VOICE + "voice_atlantis.wav", SoundPlayer.LoopMode.ONCE);
	
	// Time limits for syncing actions with sounds
	public static final int TIME_ATLANTIS_SEND_CITY = 1656;
	public static final int TIME_ATLANTIS_SEND_GENERATOR = 1884;
	public static final int TIME_ATLANTIS_SEND_MISS = 2000;
	public static final int TIME_ATLANTIS_SEND_SHIELD = 2000;
	public static final int TIME_SUBMARINE_GET_MISS = 1647;
	public static final int TIME_SUBMARINE_GET_SINK = 1725;
	public static final int TIME_SUBMARINE_GET_TOUCH = 1706;
	public static final int TIME_SUBMARINE_SEND_MISS = 1715;
	public static final int TIME_SUBMARINE_SEND_SINK = 1726;
	public static final int TIME_SUBMARINE_SEND_TOUCH = 1720;
	public static final int TIME_SURFACE_GET_MISS = 1499;
	public static final int TIME_SURFACE_GET_SINK = 1260;
	public static final int TIME_SURFACE_GET_TOUCH = 1407;
	public static final int TIME_SURFACE_SEND_MISS = 1511;
	public static final int TIME_SURFACE_SEND_SINK = 1262;
	public static final int TIME_SURFACE_SEND_TOUCH = 1404;
}
