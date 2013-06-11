package ch.hearc.p2.battleforatlantis.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.hearc.p2.battleforatlantis.action.EndGameAction;
import ch.hearc.p2.battleforatlantis.action.EndGameAction.EndGameCause;
import ch.hearc.p2.battleforatlantis.action.MoveAction;
import ch.hearc.p2.battleforatlantis.action.NextLevelAction;
import ch.hearc.p2.battleforatlantis.action.ShootAction;
import ch.hearc.p2.battleforatlantis.gameengine.Atlantis;
import ch.hearc.p2.battleforatlantis.gameengine.Generator;
import ch.hearc.p2.battleforatlantis.gameengine.Map;
import ch.hearc.p2.battleforatlantis.gameengine.MapElement;
import ch.hearc.p2.battleforatlantis.gameengine.MapType;
import ch.hearc.p2.battleforatlantis.gameengine.Player;
import ch.hearc.p2.battleforatlantis.gameengine.PlayerProgress;
import ch.hearc.p2.battleforatlantis.gameengine.Ship;
import ch.hearc.p2.battleforatlantis.sound.SoundManager;
import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.Messages;
import ch.hearc.p2.battleforatlantis.utils.Settings;

/**
 * Main panel for playing the game
 */
public class PanelPlay extends JPanel
{
	/** Main frame displaying the panel */
	private FrameMain rootFrame;

	/** Local maps panel */
	private PanelMaps levelsMe;

	/** Distant maps panel */
	private PanelMaps levelsOther;

	/** Map currently displayed as local map */
	private Map currentLocalMap;

	/** Map currently displayed as distant map */
	private Map currentDistantMap;

	/** Logger */
	private final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/** Type of player currently playing */
	private Player playerPlaying;

	/** Currently selected ship for movement */
	private Ship selectedShip = null;

	/** Top part of map display for local player */
	private PanelPlayerInfos infosLocal;

	/** Top part of map display for distant player */
	private PanelPlayerInfos infosDistant;

	/** Progress bar for local player */
	public CustomProgress progressLocal;

	/** Progress bar for distant player */
	public CustomProgress progressDistant;

	/** Bottom stats panel */
	private PanelStats panelStats;

	/** Box holding the distant player elements for removing them at Atlantis time */
	private Box boxDistant;

	/** Font for player name */
	private static final Font fontName = new Font("Arial", Font.BOLD, 20);

	/** Font for level indication */
	private static final Font fontLevel = new Font("Arial", Font.PLAIN, 14);

	/** Button for going to next level */
	private JButton btnNextLevel;

	/** Holder preventing a player from shooting 2 times at 1 play tour */
	private boolean canThreadPlay;

	/**
	 * Panel containing player informations (upper part of map)
	 */
	private class PanelPlayerInfos extends JPanel
	{
		/** Label indicating level number */
		private JLabel labelLevel;

		/** Indicator of currently playing or not */
		private boolean playing;

		/**
		 * Default constructor
		 * 
		 * @param playerName Name of player to display
		 * @param playing True if player is currently playing, else False
		 */
		public PanelPlayerInfos(String playerName, boolean playing)
		{
			this.setMaximumSize(new Dimension(600, 39));
			this.setMinimumSize(new Dimension(300, 39));

			this.labelLevel = new JLabel();
			this.labelLevel.setFont(PanelPlay.fontLevel);
			JLabel labelName = new JLabel(playerName);
			labelName.setForeground(Color.BLACK);
			labelName.setFont(PanelPlay.fontName);
			labelName.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 0));

			this.setLevelNumber(1);

			setLayout(new BorderLayout(0, 0));
			add(labelName, BorderLayout.WEST);
			add(labelLevel, BorderLayout.EAST);

			this.setPlaying(playing);
		}

		/**
		 * Set the level number
		 * 
		 * @param level New level number to display
		 */
		public void setLevelNumber(Integer level)
		{
			this.labelLevel.setText(Messages.getString("PanelPlay.Level") + " " + level.toString());
		}

		/**
		 * Change the color of the playing indicator (player name background)
		 * 
		 * @param playing True if player is playing now, else false
		 */
		public void setPlaying(boolean playing)
		{
			this.playing = playing;
			repaint();
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g;

			if (this.playing)
			{
				g2d.drawImage(ImageShop.UI_PLAYERNAME_YES, 0, 0, null);
			}
			else
			{
				g2d.drawImage(ImageShop.UI_PLAYERNAME_NO, 0, 0, null);
			}
		}
	}

	/**
	 * CardLayout to display one map at time
	 */
	private class PanelMaps extends JPanel
	{
		/** Layout holding the maps */
		private CardLayout cards;

		/** Atlanis common map */
		private Map atlantis;

		/** Player specific maps */
		private Map[] maps;

		/**
		 * Default constructor
		 * 
		 * @param maps The maps to be shown (usually a reference to FrameMain.getLocal/DistantMaps)
		 * @param atlantis The Atlantis map object (usually a reference to FrameMain.getAtlantis).
		 */
		public PanelMaps(Map[] maps, Map atlantis)
		{
			this.setBorder(BorderFactory.createLineBorder(new Color(97, 173, 233), 1));

			this.maps = maps;

			cards = new CardLayout();
			setLayout(cards);
			for (Map map : maps)
			{
				map.setBackground(new Color(12, 21, 28));
				add(map, map.getType().name());
				if (map.getType() == MapType.SURFACE)
				{
					setMaximumSize(map.getMaximumSize());
				}
			}

			this.atlantis = atlantis;

			showMap(MapType.SURFACE);
		}

		/**
		 * Display the map relative to a particular type
		 * 
		 * @param type The MapType of the map to be displayed
		 */
		public void showMap(MapType type)
		{
			if (type == MapType.ATLANTIS && getComponentCount() <= 2)
				add(atlantis, atlantis.getType().name());

			cards.show(this, type.toString());
		}

		/**
		 * Remove all maps at Atlantis time
		 */
		public void clearMaps()
		{
			for (Map map : maps)
			{
				remove(map);
			}
		}
	}

	/**
	 * Panel for displaying rotative label with statistics
	 */
	private class PanelStats extends JPanel
	{
		/** Text displayed as statistics informations */
		private String text;

		/** Number of missed shot */
		private int missed;

		/** Number of shot that have touched or sank a ship */
		private int touched;

		/** Number of sank ships */
		private int sank;

		/** Horizontal cyclic position of text */
		private int textPosition;

		/** Horizontal cyclic position of border lights */
		private int lightPosition;

		/** Holds the thread run control */
		private boolean threadRunning;

		/** Font for displaying the text */
		private final Font font = new Font("Arial", Font.BOLD, 12);

		/**
		 * Default constructor
		 */
		public PanelStats()
		{
			// Initialize values
			this.missed = 0;
			this.touched = 0;
			this.sank = 0;

			// Set visual settings
			this.setLayout(null);
			this.setBackground(Color.BLACK);

			// Set sizes
			this.setMinimumSize(new Dimension(100, 100));
			this.setPreferredSize(new Dimension(2000, 100));

			// Init the rotation
			this.refreshLabel();
			this.launchRotation();

			// Place the elements at start position, out of window range
			this.textPosition = this.getWidth();
			this.lightPosition = this.getWidth();
		}

		/**
		 * Adds a missed shot to counter
		 */
		public void addMissedShot()
		{
			this.missed++;
			this.refreshLabel();
		}

		/**
		 * Adds a touched shot to counter
		 */
		public void addTouchedShot()
		{
			this.touched++;
			this.refreshLabel();
		}

		/**
		 * Adds a boat sank to counter, including, the final touching shot
		 */
		public void addSankShot()
		{
			this.touched++;
			this.sank++;
			this.refreshLabel();
		}

		/**
		 * Starts the main loop of label rotation
		 */
		public void launchRotation()
		{
			this.threadRunning = true;
			Thread thread = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						while (PanelStats.this.threadRunning)
						{
							PanelStats.this.lightPosition += 5;
							if (PanelStats.this.lightPosition > 2500)
							{
								PanelStats.this.lightPosition = -300;
							}
							PanelStats.this.textPosition -= 1;
							if (PanelStats.this.textPosition < -500)
							{
								PanelStats.this.textPosition = PanelStats.this.getWidth();
							}
							PanelStats.this.repaint();
							Thread.sleep(10);
						}
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			});
			thread.start();
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g;

			for (int i = 0; i <= this.getWidth(); i++)
			{
				g2d.drawImage(ImageShop.UI_DEFIL_BACKGROUND, i, 35, null);
			}
			g2d.drawImage(ImageShop.UI_DEFIL_LIGHT, this.lightPosition, 35, null);
			g2d.drawImage(ImageShop.UI_DEFIL_LIGHT, this.lightPosition, 68, null);

			g2d.setColor(Color.WHITE);
			g2d.setFont(this.font);
			g2d.drawString(this.text, this.textPosition, 57);
		}

		/**
		 * Refresh the label content
		 */
		private void refreshLabel()
		{
			this.text = String.format(Messages.getString("PanelPlay.PanelStatsText"), missed, touched, missed + touched, sank);

			this.repaint();
		}
	}

	/**
	 * <pre>
	 * Default constructor
	 * All the maps have to be set in FrameMain before calling this constructor !
	 * </pre>
	 * 
	 * @param rootFrame Main frame in which the panel will be displayed
	 */
	public PanelPlay(FrameMain rootFrame)
	{
		// Assign input parameters
		this.rootFrame = rootFrame;
		this.playerPlaying = rootFrame.getFirstPlayerToPlay();

		// Gather all maps for settings application
		Map atlantis = rootFrame.getMapByType(MapType.ATLANTIS, Player.LOCAL);
		Map mySurface = rootFrame.getMapByType(MapType.SURFACE, Player.LOCAL);
		Map mySubmarine = rootFrame.getMapByType(MapType.SUBMARINE, Player.LOCAL);
		Map yourSurface = rootFrame.getMapByType(MapType.SURFACE, Player.DISTANT);
		Map yourSubmarine = rootFrame.getMapByType(MapType.SUBMARINE, Player.DISTANT);

		// Set alignment of maps for correct displaying
		atlantis.setAlignmentX(LEFT_ALIGNMENT);
		mySurface.setAlignmentX(LEFT_ALIGNMENT);
		mySubmarine.setAlignmentX(LEFT_ALIGNMENT);
		yourSurface.setAlignmentX(LEFT_ALIGNMENT);
		yourSubmarine.setAlignmentX(LEFT_ALIGNMENT);

		// Assign first current maps (surfaces) at start of game
		currentLocalMap = rootFrame.getMapByType(MapType.SURFACE, Player.LOCAL);
		currentDistantMap = rootFrame.getMapByType(MapType.SURFACE, Player.DISTANT);

		// Create canvas
		setLayout(new BorderLayout(0, 0));
		Box canvasMaps = Box.createHorizontalBox();
		Box.createVerticalBox();

		// Prepare local map box
		this.infosLocal = new PanelPlayerInfos(rootFrame.getPlayerName(), true);
		this.infosLocal.setAlignmentX(LEFT_ALIGNMENT);
		this.levelsMe = new PanelMaps(rootFrame.getLocalMaps(), atlantis);
		this.levelsMe.setAlignmentX(LEFT_ALIGNMENT);
		Box boxLocal = Box.createVerticalBox();
		boxLocal.add(this.infosLocal);
		boxLocal.add(this.levelsMe);

		// Prepare distant map box
		this.infosDistant = new PanelPlayerInfos(rootFrame.getDistantPlayerName(), false);
		this.infosDistant.setAlignmentX(LEFT_ALIGNMENT);
		this.levelsOther = new PanelMaps(rootFrame.getDistantMaps(), atlantis);
		this.levelsOther.setAlignmentX(LEFT_ALIGNMENT);
		this.boxDistant = Box.createVerticalBox();
		this.boxDistant.add(this.infosDistant);
		this.boxDistant.add(this.levelsOther);

		// Add maps to canvas
		canvasMaps.add(Box.createHorizontalGlue());
		canvasMaps.add(Box.createHorizontalStrut(20));
		canvasMaps.add(boxLocal);
		canvasMaps.add(Box.createHorizontalStrut(10));
		canvasMaps.add(Box.createHorizontalGlue());
		canvasMaps.add(Box.createHorizontalStrut(10));
		canvasMaps.add(boxDistant);
		canvasMaps.add(Box.createHorizontalStrut(20));
		canvasMaps.add(Box.createHorizontalGlue());

		JButton btnCapitulate = new CustomButton(Messages.getString("PanelPlay.Capitulate"));
		btnCapitulate.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				endGame(false, false);
			}
		});

		// Button for next level transition
		btnNextLevel = new CustomButton(Messages.getString("PanelPlay.NextLevel"));
		btnNextLevel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				nextLevel();
			}
		});
		btnNextLevel.setVisible(false);

		// Progress bars
		this.progressLocal = new CustomProgress(0, 100);
		this.progressDistant = new CustomProgress(0, 100);

		Set<Ship> distantShipSet = Settings.FRAME_MAIN.getDistantShips();
		MapElement[] distantShipList = new MapElement[distantShipSet.size()];
		distantShipSet.toArray(distantShipList);

		PlayerProgress.getInstance(Player.LOCAL).calculateTotalProgression(MapType.SURFACE, Settings.FRAME_MAIN.getShips());
		PlayerProgress.getInstance(Player.DISTANT).calculateTotalProgression(MapType.SURFACE, getDistantShip());

		// Labels for progress bars
		JLabel labelProgressLocal = new JLabel(Messages.getString("PanelPlay.CurrentLevelStatus"));
		labelProgressLocal.setFont(PanelPlay.fontLevel);
		labelProgressLocal.setForeground(Color.WHITE);
		JLabel labelProgressDistant = new JLabel(Messages.getString("PanelPlay.CurrentLevelStatus"));
		labelProgressDistant.setFont(PanelPlay.fontLevel);
		labelProgressDistant.setForeground(Color.WHITE);

		// Set alignment
		this.progressLocal.setAlignmentX(CENTER_ALIGNMENT);
		this.progressDistant.setAlignmentX(CENTER_ALIGNMENT);
		labelProgressLocal.setAlignmentX(CENTER_ALIGNMENT);
		labelProgressDistant.setAlignmentX(CENTER_ALIGNMENT);

		// Canvas local progress bar
		Box canvasProgressLocal = Box.createVerticalBox();
		canvasProgressLocal.add(Box.createVerticalStrut(15));
		canvasProgressLocal.add(labelProgressLocal);
		canvasProgressLocal.add(Box.createVerticalStrut(15));
		canvasProgressLocal.add(progressLocal);
		canvasProgressLocal.add(Box.createVerticalStrut(15));

		// Canvas distant progress bar
		Box canvasProgressDistant = Box.createVerticalBox();
		canvasProgressDistant.add(Box.createHorizontalStrut(15));
		canvasProgressDistant.add(labelProgressDistant);
		canvasProgressDistant.add(Box.createHorizontalStrut(15));
		canvasProgressDistant.add(progressDistant);
		canvasProgressDistant.add(Box.createHorizontalStrut(15));

		// Canvas for buttons
		Box canvasButtons = Box.createVerticalBox();
		canvasButtons.add(btnCapitulate);
		canvasButtons.add(btnNextLevel);

		// Canvas for progress bars
		Box canvasLevels = Box.createHorizontalBox();
		canvasLevels.add(Box.createHorizontalGlue());
		canvasLevels.add(canvasProgressLocal);
		canvasLevels.add(Box.createHorizontalGlue());
		canvasLevels.add(canvasProgressDistant);
		canvasLevels.add(Box.createHorizontalGlue());
		canvasLevels.add(canvasButtons);

		// Panel for statistics
		this.panelStats = new PanelStats();

		// Main canvas
		add(canvasMaps, BorderLayout.CENTER);
		add(canvasLevels, BorderLayout.NORTH);
		add(this.panelStats, BorderLayout.SOUTH);

		// Assign currently playing player
		boolean isLocalPlaying = playerPlaying == Player.LOCAL;
		infosLocal.setPlaying(isLocalPlaying);
		infosDistant.setPlaying(!isLocalPlaying);
		canThreadPlay = true;

		// Set sounds
		SoundManager.getInstance().playNextLevel(MapType.SURFACE);
		SoundManager.getInstance().setStream(SoundManager.Stream.SURFACE);
		SoundManager.getInstance().setMusic(SoundManager.Music.CALM);
	}

	/**
	 * Called when the remote player shoots on a particular box
	 * 
	 * @param location The reference to the Box that is targeted
	 */
	public void shoot(final ch.hearc.p2.battleforatlantis.gameengine.Box location)
	{
		if (location.isDiscovered())
			return;
		if (playerPlaying != Player.LOCAL)
			return;
		if (location.getMapType() == MapType.ATLANTIS && currentDistantMap.getType() != MapType.ATLANTIS)
			return;
		if (!canThreadPlay)
			return;

		canThreadPlay = false;

		int soundDelay = 0;

		// Get occupier of the box shot
		final MapElement occupier = location.getOccupier();

		// If we shot a ship
		if (occupier != null)
		{
			final int statisticRemaining = occupier.getRemainingSize();
			if (occupier instanceof Ship)
			{
				if (occupier.getRemainingSize() > 1)
				{
					soundDelay = SoundManager.getInstance().playShoot(location.getMapType(), SoundManager.Direction.SEND, SoundManager.Target.TOUCH);
				}
				else
				{
					soundDelay = SoundManager.getInstance().playShoot(location.getMapType(), SoundManager.Direction.SEND, SoundManager.Target.SINK);
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

			final int statisticSoundDelay = soundDelay;
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						Thread.sleep(statisticSoundDelay);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					// User shot => local player progression
					PlayerProgress.getInstance(Player.LOCAL).addProgress();
					progressLocal.setValue(PlayerProgress.getInstance(Player.LOCAL).getProgess());
					Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("[PanelPlay::shoot] Progress : " + PlayerProgress.getInstance(Player.LOCAL).getProgess());

					if (statisticRemaining > 1)
					{
						panelStats.addTouchedShot();
					}
					else
					{
						panelStats.addSankShot();
					}
				}
			}).start();
		}
		// Or shot nothing
		else
		{
			if (location.getMapType() == MapType.ATLANTIS)
			{
				soundDelay = SoundManager.getInstance().playShootAtlantis(SoundManager.Atlantis.MISS);
			}
			else
			{
				soundDelay = SoundManager.getInstance().playShoot(location.getMapType(), SoundManager.Direction.SEND, SoundManager.Target.MISS);
			}

			final int statisticSoundDelay = soundDelay;
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						Thread.sleep(statisticSoundDelay);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					panelStats.addMissedShot();
				}
			}).start();
		}

		// Finalize the sound delay
		final int finalSoundDelay = soundDelay;

		// Made the shot at the box location with sound delay
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
				location.shoot();
				btnNextLevel.setVisible(currentDistantMap.isFinished());
				validate();
				if (!(occupier instanceof Generator))
					endCurrentTurn();
				canThreadPlay = true;
			}
		}).start();

		// Send the shot to the opponent
		new ShootAction(location).send();
	}

	/**
	 * Rotate a particular Ship
	 * 
	 * @param ship The ship to be rotated
	 * @param clockwise Whether the boat is to be rotated in a clockwise angle
	 */
	public void rotate(Ship ship, boolean clockwise)
	{
		if (playerPlaying != Player.LOCAL)
			return;

		if (!ship.rotationPossible(clockwise))
			return;

		if (!canThreadPlay)
			return;

		ship.getCenter().getMap().removeShipControls(ship);
		ship.rotate(clockwise);

		endCurrentTurn();

		new MoveAction(ship, ship.getCenter(), ship.getOrientation()).send();
	}

	/**
	 * Move a particular ship forwards/backwards
	 * 
	 * @param forward True = forwards, false = backwards
	 */
	public void place(Ship ship, boolean forward)
	{
		if (playerPlaying != Player.LOCAL)
			return;

		if (!canThreadPlay)
			return;

		ship.getCenter().getMap().removeShipControls(ship);
		ship.moveOut();
		ship.place(forward);

		endCurrentTurn();

		new MoveAction(ship, ship.getCenter(), ship.getOrientation()).send();
	}

	/**
	 * Select a local ship
	 * 
	 * @param ship Selected ship
	 * @param mouseButton Button pressed on mouse
	 */
	public void select(Ship ship, int mouseButton)
	{
		if (playerPlaying != Player.LOCAL)
			return;

		if (selectedShip != null)
		{
			selectedShip.getCenter().getMap().removeShipControls(selectedShip);
			if (selectedShip == ship && (mouseButton == MouseEvent.BUTTON1 || mouseButton == MouseEvent.BUTTON3) && !ship.isTouched())
			{
				rotate(ship, mouseButton == MouseEvent.BUTTON1);
				return;
			}
		}
		selectedShip = ship;
		ship.getCenter().getMap().addShipControls(ship);
	}

	/**
	 * Called when a player asks to go to the next level
	 */
	public void nextLevel()
	{
		if (playerPlaying != Player.LOCAL || !currentDistantMap.isFinished())
			return;

		if (!canThreadPlay)
			return;

		log.info("Next level");

		btnNextLevel.setVisible(false);
		validate();

		PlayerProgress.getInstance(Player.LOCAL).nextLevel(Settings.FRAME_MAIN.getShips());
		progressLocal.setValue(PlayerProgress.getInstance(Player.LOCAL).getProgess());

		MapType oldMap = currentDistantMap.getType();

		MapType newMap = null;
		switch (oldMap)
		{
			case SUBMARINE:
				newMap = MapType.ATLANTIS;
				SoundManager.getInstance().playNextLevel(MapType.ATLANTIS);
				SoundManager.getInstance().setStream(SoundManager.Stream.ATLANTIS);
				break;
			case SURFACE:
				newMap = MapType.SUBMARINE;
				SoundManager.getInstance().playNextLevel(MapType.SUBMARINE);
				SoundManager.getInstance().setStream(SoundManager.Stream.SUBMARINE);
				break;
			case ATLANTIS:
				endGame(true, false);
				return;
		}

		setActiveMap(newMap, Player.DISTANT);

		endCurrentTurn();

		new NextLevelAction(newMap).send();
	}

	/**
	 * End the current turn and give the hand to other player
	 */
	public void endCurrentTurn()
	{
		if (selectedShip != null)
			selectedShip.getCenter().getMap().removeShipControls(selectedShip);

		selectedShip = null;
		playerPlaying = playerPlaying == Player.LOCAL ? Player.DISTANT : Player.LOCAL;
		boolean isLocalPlaying = playerPlaying == Player.LOCAL;
		infosLocal.setPlaying(isLocalPlaying);
		infosDistant.setPlaying(!isLocalPlaying);
	}

	/**
	 * The game is finished
	 * 
	 * @param isWinner True when the local player is the winner
	 * @param fromNetwork True if this call comes from the network
	 */
	public void endGame(boolean isWinner, boolean fromNetwork)
	{
		if (!fromNetwork)
		{
			EndGameCause cause = isWinner ? EndGameCause.ATLANTIS_DESTROYED : EndGameCause.SURRENDERED;
			new EndGameAction(!isWinner, cause).send();
		}
		DialogEndGame.announceGameResult(this, isWinner);
		rootFrame.endGame();
	}

	/**
	 * Like nextLevel(), but more generic because the Player to which the level change applies can be set as well as the destination MapType
	 * 
	 * @param map The MapType to be shown on the Player's map
	 * @param local True if this applies to the local player
	 */
	public void setActiveMap(MapType map, Player player)
	{
		int newLevel = 0;
		switch (map)
		{
			case ATLANTIS:
				newLevel = 3;
				break;
			case SUBMARINE:
				newLevel = 2;
				break;
			case SURFACE:
				newLevel = 1;
				break;
		}

		if (player == Player.LOCAL)
		{
			currentLocalMap = rootFrame.getMapByType(map, Player.LOCAL);
			this.infosLocal.setLevelNumber(newLevel);
		}
		else
		{
			currentDistantMap = rootFrame.getMapByType(map, Player.DISTANT);
			this.infosDistant.setLevelNumber(newLevel);
		}

		if (MapType.ATLANTIS == currentDistantMap.getType() && MapType.ATLANTIS == currentLocalMap.getType())
		{
			// We play on one panel
			this.levelsOther.clearMaps();
			player = Player.LOCAL;
		}

		if (player == Player.LOCAL)
			levelsMe.showMap(map);
		else
			levelsOther.showMap(map);

		currentLocalMap.resizeComponent();
		currentDistantMap.resizeComponent();
	}

	/**
	 * Get the current level of the given player type
	 * 
	 * @param player Player type from which gather current level
	 * @return Map currently played by player
	 */
	public Map getCurrentLevel(Player player)
	{
		return player == Player.LOCAL ? currentLocalMap : currentDistantMap;
	}

	/**
	 * Init the sizes of maps
	 */
	public void initSizes()
	{
		currentLocalMap.resizeComponent();
		currentDistantMap.resizeComponent();
	}

	/**
	 * Check if the local player is currently playing
	 * 
	 * @return True if the local player is playing, else false
	 */
	public boolean isLocalPlayerPlaying()
	{
		return playerPlaying == Player.LOCAL;
	}

	/**
	 * Get the distant ships collection
	 * 
	 * @return Array of distant ships
	 */
	public MapElement[] getDistantShip()
	{
		Set<Ship> distantShipSet = Settings.FRAME_MAIN.getDistantShips();
		MapElement[] distantShipList = new MapElement[distantShipSet.size()];
		distantShipSet.toArray(distantShipList);
		return distantShipList;
	}
}
