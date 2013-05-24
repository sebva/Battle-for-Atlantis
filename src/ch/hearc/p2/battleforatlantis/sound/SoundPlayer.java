package ch.hearc.p2.battleforatlantis.sound;

public class SoundPlayer
{
	/**
	 * First engine for playing odd cues
	 */
	private SoundEngine odd;
	
	/**
	 * Second engine for playing even cues
	 */
	private SoundEngine even;
	
	/**
	 * Loop mode
	 */
	private LoopMode loop;

	public enum LoopMode
	{
		LOOP, ONCE
	};

	/**
	 * Default constructor
	 * @param filename : name of file to open
	 * @param loop : loop mode to apply
	 */
	public SoundPlayer(String filename, LoopMode loop)
	{
		// Assign loop mode
		this.loop = loop;

		// Create required engines
		this.odd = new SoundEngine(filename);
		if (loop == LoopMode.LOOP)
		{
			this.even = new SoundEngine(filename);
			SoundEngine.pair(this.odd, this.even);
			this.even.load();
		}
		this.odd.load();
	}

	/**
	 * Play the engine content, or resume from pause if necessary
	 */
	public void play()
	{
		if (this.odd.getStatus() == SoundEngine.Status.PAUSED)
		{
			this.odd.pause();
		}
		else
		{
			this.odd.play();
		}
		if (this.loop == LoopMode.LOOP)
		{
			if (this.even.getStatus() == SoundEngine.Status.PAUSED)
			{
				this.even.pause();
			}
			else
			{
				this.even.play();
			}
		}
	}

	/**
	 * Pause the engine if playing
	 */
	public void pause()
	{
		if (this.odd.getStatus() == SoundEngine.Status.PLAYING)
		{
			this.odd.pause();
		}
		if (this.loop == LoopMode.LOOP)
		{
			if (this.even.getStatus() == SoundEngine.Status.PLAYING)
			{
				this.even.pause();
			}
		}
	}
}
