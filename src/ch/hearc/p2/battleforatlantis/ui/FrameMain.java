package ch.hearc.p2.battleforatlantis.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.HashSet;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ch.hearc.p2.battleforatlantis.Main;
import ch.hearc.p2.battleforatlantis.gameengine.Map;
import ch.hearc.p2.battleforatlantis.gameengine.MapType;
import ch.hearc.p2.battleforatlantis.gameengine.Player;
import ch.hearc.p2.battleforatlantis.gameengine.Ship;
import ch.hearc.p2.battleforatlantis.gameinit.Loader;
import ch.hearc.p2.battleforatlantis.net.Host;
import ch.hearc.p2.battleforatlantis.net.NetworkManager;
import ch.hearc.p2.battleforatlantis.sound.SoundManager;
import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.Messages;
import ch.hearc.p2.battleforatlantis.utils.Settings;

public class FrameMain extends JFrame
{
	/** Default width of window at opening */
	private static final int kDefaultWidth = 1315;

	/** Default height of window at opening */
	private static final int kDefaultHeight = 859;

	/** Title of window */
	private static final String kWindowTitle = Messages.getString("FrameMain.WindowTitle");

	/** Name of player at opening */
	private static final String kKeyPlayerName = "playerName";

	/** Card panel for switching between main panels */
	private PanelCards cards;

	/** List of maps for displaying local player maps */
	private Map[] localMaps;

	/** List of maps for displaying distant player maps (null at opening, no connection set yet) */
	private Map[] distantMaps = null;

	/** Atlantis map */
	private Map atlantis;

	/** List of ships of local player */
	private Ship[] ships;

	/** Set of ships of distant player */
	private Set<Ship> distantShips;

	/** Hash of the configuration file for coherence verification */
	private String hashConfig;

	/** Name of local player */
	private String playerName;

	/** Name of distant player */
	private String distantPlayerName;

	/** Indicator of first player to play (local or distant) */
	private Player firstPlayerToPlay;

	/**
	 * Panel containing the cards
	 */
	private class PanelCards extends JPanel
	{
		/** Layout for card display */
		private CardLayout cardLayout;

		/**
		 * Default constructor
		 */
		public PanelCards()
		{
			cardLayout = new CardLayout();
			setLayout(cardLayout);
		}

		/**
		 * Change the card displayed
		 * 
		 * @param cardName Name of card to display
		 */
		public void showCard(String cardName)
		{
			cardLayout.show(this, cardName);
		}

	}

	/**
	 * Default constructor
	 * 
	 * @throws Exception
	 */
	public FrameMain() throws Exception
	{
		// Register frame main
		Settings.FRAME_MAIN = this;
		initGame();

		// Configure window with default settings
		windowConfig();

		// Load the player's name from the Preferences
		getPlayerName();

		// Load default panels
		Settings.PANEL_HOME = new PanelHome(this);
		Settings.PANEL_CONNECTIONS = new PanelConnection(this);

		// Main panel for cards
		cards = new PanelCards();
		add(cards, BorderLayout.CENTER);
		cards.add(Settings.PANEL_HOME, PanelHome.class.getSimpleName());
		cards.add(Settings.PANEL_CONNECTIONS, PanelConnection.class.getSimpleName());

		// Prevent the window from being resized (graphical bug inside, thank's to Swing !)
		this.addWindowStateListener(new WindowStateListener()
		{
			@Override
			public void windowStateChanged(WindowEvent e)
			{
				if ((e.getNewState() == Frame.MAXIMIZED_BOTH))
				{
					FrameMain.this.setExtendedState(Frame.NORMAL);
				}
			}
		});

		// Set the base sounds
		SoundManager.getInstance().setStream(SoundManager.Stream.NONE);
		SoundManager.getInstance().setMusic(SoundManager.Music.MENU);

		// Display window
		setVisible(true);
	}

	/**
	 * Initialize the game from config file
	 * 
	 * @throws Exception
	 */
	private void initGame() throws Exception
	{
		distantShips = new HashSet<>();

		Loader loader = new Loader();
		loader.load();
		localMaps = loader.getMapsWithoutAtlantis();
		distantMaps = null;
		atlantis = loader.getAtlantis();
		ships = loader.getShips();
		hashConfig = loader.getHash();
	}

	/**
	 * Configure the window with default settings
	 */
	private void windowConfig()
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(kDefaultWidth, kDefaultHeight);
		setTitle(kWindowTitle);
		setIconImage(ImageShop.UI_LOGO);
	}

	/**
	 * Go to the connection panel
	 */
	public void searchPlayer()
	{
		cards.showCard(PanelConnection.class.getSimpleName());
		NetworkManager.getInstance().setAutodiscoverListener(Settings.PANEL_CONNECTIONS);
	}

	/**
	 * Initialize connection and go to the placement panel
	 * 
	 * @param h Host to join
	 */
	public void placeShips(Host h)
	{
		this.distantPlayerName = h.getName();

		Settings.PANEL_PREPARE = new PanelPrepare(this);
		cards.add(Settings.PANEL_PREPARE, PanelPrepare.class.getSimpleName());
		cards.showCard(PanelPrepare.class.getSimpleName());

		NetworkManager.getInstance().removeAutodiscoverListener(Settings.PANEL_CONNECTIONS);
		forceResize();
		SoundManager.getInstance().setStream(SoundManager.Stream.PLACEMENT);
		SoundManager.getInstance().setMusic(SoundManager.Music.NONE);
	}

	/**
	 * Start the game after placement
	 */
	public void startGame()
	{
		if (distantMaps != null)
			showGame();
	}

	/**
	 * Display the play panel
	 */
	private void showGame()
	{
		Settings.PANEL_PLAY = new PanelPlay(this);
		cards.add(Settings.PANEL_PLAY, PanelPlay.class.getSimpleName());

		cards.showCard(PanelPlay.class.getSimpleName());
		forceResize();
		SoundManager.getInstance().setMusic(SoundManager.Music.CALM);
		SoundManager.getInstance().setStream(SoundManager.Stream.SURFACE);
	}

	/**
	 * Force the linear resize of the window to prevent (in some cases huhu ^^) the graphical interface from bugging
	 */
	private void forceResize()
	{
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				try
				{
					int originalWidth = FrameMain.this.getWidth();
					int minimalWidth = originalWidth - 5;
					Thread.sleep(80);
					while (FrameMain.this.getWidth() > minimalWidth)
					{
						FrameMain.this.setSize(FrameMain.this.getWidth() - 1, FrameMain.this.getHeight());
						Thread.sleep(2);
					}
					while (FrameMain.this.getWidth() < originalWidth)
					{
						FrameMain.this.setSize(FrameMain.this.getWidth() + 1, FrameMain.this.getHeight());
						Thread.sleep(2);
					}
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Reinitialize the game at main menu
	 */
	public void endGame()
	{
		NetworkManager.getInstance().closeTcpConnection();
		try
		{
			initGame();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		cards.showCard(PanelHome.class.getSimpleName());
		if (Settings.PANEL_PLAY != null)
		{
			cards.remove(Settings.PANEL_PLAY);
			Settings.PANEL_PLAY = null;
		}

		SoundManager.getInstance().setMusic(SoundManager.Music.MENU);
		SoundManager.getInstance().setStream(SoundManager.Stream.NONE);
	}

	/**
	 * Get the local maps
	 * 
	 * @return Local maps
	 */
	public Map[] getLocalMaps()
	{
		return localMaps;
	}

	/**
	 * Get the map corresponding to a player (network side) and a level
	 * 
	 * @param type Type of map (level)
	 * @param player Type of player (network side)
	 * @return Map found for given settings
	 */
	public Map getMapByType(MapType type, Player player)
	{
		// Special case for Atlantis Map
		if (type == MapType.ATLANTIS)
			return atlantis;

		// Local or distant Map List
		Map[] mapList = (player == Player.LOCAL) ? localMaps : distantMaps;

		// List each map
		for (Map map : mapList)
		{
			// Check type asked
			if (map.getType().equals(type))
			{
				return map;
			}
		}

		return null;
	}

	/**
	 * Get the distant maps
	 * 
	 * @return Array of distant maps
	 */
	public Map[] getDistantMaps()
	{
		return distantMaps;
	}

	/**
	 * Set the distant maps
	 * 
	 * @param maps Array of distant maps
	 */
	public void setDistantMaps(Map[] maps)
	{
		this.distantMaps = maps;
	}

	/**
	 * Get the local ships
	 * 
	 * @return Array of local ships
	 */
	public Ship[] getShips()
	{
		return ships;
	}

	/**
	 * Get the distant ships
	 * 
	 * @return Set of distant ships
	 */
	public Set<Ship> getDistantShips()
	{
		return distantShips;
	}

	/**
	 * Get the hash of config file
	 * 
	 * @return Hash of config file
	 */
	public String getHashConfig()
	{
		return hashConfig;
	}

	/**
	 * Receive a connection attempt from another player
	 * 
	 * @param h Host who have sent the request
	 */
	public void connectionAttempt(Host h)
	{
		int result = JOptionPane.showConfirmDialog(this,
				String.format(Messages.getString("FrameMain.ConnectionAttemptMessage"), h.getName(), h.getAddress().getHostAddress()),
				Messages.getString("FrameMain.ConnectionAttemptTitle"), JOptionPane.YES_NO_OPTION);
		boolean connectionAccepted = (result == JOptionPane.YES_OPTION);
		NetworkManager.getInstance().connectionResponse(connectionAccepted, h);
	}

	/**
	 * Indicates that a player asked to play has refused the game
	 * 
	 * @param h Host who refused
	 */
	public void connectionRefused(Host h)
	{
		JOptionPane.showMessageDialog(this,
				String.format(Messages.getString("FrameMain.ConnectionRefusedMessage"), h.getName(), h.getAddress().getHostAddress()),
				Messages.getString("FrameMain.ConnectionRefusedTitle"), JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Get the local player name
	 * 
	 * @return Local player name
	 */
	public String getPlayerName()
	{
		if (playerName == null)
			playerName = Preferences.userNodeForPackage(Main.class).get(kKeyPlayerName, Messages.getString("FrameMain.Player"));
		return playerName;
	}

	/**
	 * Set the local player name
	 * 
	 * @param playerName New player name
	 */
	public void setPlayerName(String playerName)
	{
		this.playerName = playerName;
		Preferences.userNodeForPackage(Main.class).put(kKeyPlayerName, playerName);
	}

	/**
	 * Get the distant player name
	 * 
	 * @return Distant player name
	 */
	public String getDistantPlayerName()
	{
		return distantPlayerName;
	}

	/**
	 * Obtain the type of the first player to play in game
	 * 
	 * @return Type of first player
	 */
	public Player getFirstPlayerToPlay()
	{
		return firstPlayerToPlay;
	}

	/**
	 * Defines the first player that will play in game
	 * 
	 * @param firstPlayerToPlay Type of first player
	 */
	public void setFirstPlayerToPlay(Player firstPlayerToPlay)
	{
		this.firstPlayerToPlay = firstPlayerToPlay;
	}
}
