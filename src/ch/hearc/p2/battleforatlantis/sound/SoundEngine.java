package ch.hearc.p2.battleforatlantis.sound;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Semaphore;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundEngine
{
	/**
	 * Thread for running deck play actions
	 */
	private Thread deck;

	/**
	 * Name of file to read
	 */
	private String filename;

	/**
	 * Cue point to use in reading procedure
	 */
	private Position cue;

	/**
	 * Source file to read
	 */
	private URL source;

	/**
	 * Stream generated from source file for read operations
	 */
	private AudioInputStream stream;

	/**
	 * Status of engine
	 */
	private Status status;

	/**
	 * Format of file to pass to output line
	 */
	private AudioFormat format;

	/**
	 * Output line for writing to speakers
	 */
	private SourceDataLine line;

	/**
	 * Informations on line
	 */
	private DataLine.Info info;

	/**
	 * Twin engine semaphore for syncing dual playing
	 */
	private Semaphore twin;

	/**
	 * Loop posistor
	 */
	private boolean loop;

	/**
	 * Size of buffer (do not touch !)
	 */
	private final int EXTERNAL_BUFFER_SIZE = 524288;

	enum Position
	{
		LEFT, RIGHT, NORMAL
	};

	public enum Status
	{
		BLANK, LOADING, READY, PLAYING, PAUSED, FINISHED
	};

	/**
	 * Default constructor
	 * 
	 * @param filename : name of file to open and read
	 */
	public SoundEngine(String filename)
	{
		this.filename = filename;
		this.deck = null;
		this.twin = null;
		this.cue = Position.NORMAL;
		this.status = Status.BLANK;
		this.loop = false;
	}

	/**
	 * Loads the file into stream and prepare line for playing
	 */
	public synchronized void load()
	{
		if (this.status == Status.BLANK)
		{
			this.status = Status.LOADING;
			new Thread(new Runnable()
			{

				@Override
				public void run()
				{
					// Open file
					SoundEngine.this.source = getClass().getResource(SoundEngine.this.filename);

					if (SoundEngine.this.source == null)
					{
						System.err.println("Wave file not found : " + SoundEngine.this.filename);
						return;
					}

					// Load stream from file
					SoundEngine.this.stream = null;
					try
					{
						SoundEngine.this.stream = AudioSystem.getAudioInputStream(SoundEngine.this.source);
					}
					catch (UnsupportedAudioFileException e1)
					{
						e1.printStackTrace();
						return;
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
						return;
					}

					// Open output line
					SoundEngine.this.format = SoundEngine.this.stream.getFormat();
					SoundEngine.this.line = null;
					SoundEngine.this.info = new DataLine.Info(SourceDataLine.class, SoundEngine.this.format);
					try
					{
						SoundEngine.this.line = (SourceDataLine) AudioSystem.getLine(SoundEngine.this.info);
						SoundEngine.this.line.open(SoundEngine.this.format);
					}
					catch (LineUnavailableException e)
					{
						e.printStackTrace();
						return;
					}
					catch (Exception e)
					{
						e.printStackTrace();
						return;
					}

					// Set pan mode
					if (SoundEngine.this.line.isControlSupported(FloatControl.Type.PAN))
					{
						FloatControl pan = (FloatControl) SoundEngine.this.line.getControl(FloatControl.Type.PAN);
						if (SoundEngine.this.cue == Position.RIGHT)
						{
							pan.setValue(1.0f);
						}
						else if (SoundEngine.this.cue == Position.LEFT)
						{
							pan.setValue(-1.0f);
						}
					}

					// Validate end of loading procedure
					SoundEngine.this.status = Status.READY;
				}
			}).start();
		}
	}

	/**
	 * Play the file, according to defined settings
	 */
	public synchronized void play()
	{
		this.deck = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				// Wait for engine to be in a READY state
				while (SoundEngine.this.status != Status.READY)
				{
					try
					{
						Thread.sleep(100);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}

				// Set PLAYING status
				SoundEngine.this.status = Status.PLAYING;

				// Create semaphore if deck is alone
				if (SoundEngine.this.twin == null)
				{
					SoundEngine.this.twin = new Semaphore(1);
				}

				// Open output line
				SoundEngine.this.line.start();
				int nBytesRead = 0;
				byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];

				// Acquire twin lock
				try
				{
					SoundEngine.this.twin.acquire();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				// Read stream
				try
				{
					while (nBytesRead != -1)
					{
						nBytesRead = SoundEngine.this.stream.read(abData, 0, abData.length);
						if (nBytesRead >= 0)
						{
							SoundEngine.this.line.write(abData, 0, nBytesRead);
						}
						while (SoundEngine.this.status == Status.PAUSED)
						{
							Thread.sleep(100);
						}
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return;
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				finally
				{
					if (!SoundEngine.this.loop)
					{
						SoundEngine.this.line.drain();
					}
					SoundEngine.this.twin.release();
					Thread.yield();
					try
					{
						Thread.sleep(10);
					}
					catch (InterruptedException e1)
					{
						e1.printStackTrace();
					}
					SoundEngine.this.line.close();
					try
					{
						SoundEngine.this.stream.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}

					SoundEngine.this.status = Status.BLANK;
					SoundEngine.this.load();

					// Loop if required
					if (SoundEngine.this.loop)
					{
						SoundEngine.this.play();
					}
				}
			}
		});
		this.deck.start();
	}

	/**
	 * Switch between PLAYING and PAUSED state
	 */
	@SuppressWarnings("incomplete-switch")
	public void pause()
	{
		switch (SoundEngine.this.status)
		{
			case PLAYING:
				SoundEngine.this.status = Status.PAUSED;
				SoundEngine.this.line.stop();
				break;
			case PAUSED:
				SoundEngine.this.line.drain();
				SoundEngine.this.line.start();
				SoundEngine.this.status = Status.PLAYING;
				break;
		}
	}

	/**
	 * Getter for engine status
	 * 
	 * @return status of engine
	 */
	public Status getStatus()
	{
		return this.status;
	}

	/**
	 * Setter for twin engine
	 * 
	 * @param twin : engine to pair with current one
	 * @return pairing success state
	 */
	private boolean setTwin(Semaphore twin)
	{
		if (this.status == Status.BLANK)
		{
			this.twin = twin;
			this.loop = true;
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Clearance of twin engine
	 */
	private void clearTwin()
	{
		this.twin = null;
		this.loop = false;
	}

	/**
	 * Accessor for pairing two engines
	 * 
	 * @param engine1 : first engine to pair
	 * @param engine2 : second engine to pair
	 */
	public static void pair(SoundEngine engine1, SoundEngine engine2)
	{
		Semaphore semaphore = new Semaphore(1);
		if (!(engine1.setTwin(semaphore) && engine2.setTwin(semaphore)))
		{
			engine1.clearTwin();
			engine2.clearTwin();
		}
	}
}
