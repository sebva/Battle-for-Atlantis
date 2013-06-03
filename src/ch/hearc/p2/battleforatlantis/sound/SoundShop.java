package ch.hearc.p2.battleforatlantis.sound;

import ch.hearc.p2.battleforatlantis.gameengine.MapType;

public final class SoundShop
{
	private static final String SOUND = "/sound/";
	private static final String FX = SOUND + "fx/";

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

}
