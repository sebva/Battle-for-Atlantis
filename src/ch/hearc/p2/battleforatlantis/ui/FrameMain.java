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
import ch.hearc.p2.battleforatlantis.action.Action;
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
	private static final int kDefaultWidth = 1024;
	private static final int kDefaultHeight = 768;
	private static final String kWindowTitle = Messages.getString("FrameMain.WindowTitle");
	private static final String kKeyPlayerName = "playerName";

	private PanelCards cards;

	private Map[] localMaps;
	private Map[] distantMaps = null;
	private Map atlantis;
	private Ship[] ships;
	private Set<Ship> distantShips;
	private String hashConfig;
	private String playerName;
	private String distantPlayerName;
	private Player firstPlayerToPlay;

	private class PanelCards extends JPanel
	{
		private CardLayout cardLayout;

		public PanelCards()
		{
			cardLayout = new CardLayout();
			setLayout(cardLayout);
		}

		public void showCard(String cardName)
		{
			cardLayout.show(this, cardName);
		}

	}

	public FrameMain() throws Exception
	{
		Settings.FRAME_MAIN = this;
		distantShips = new HashSet<>();

		Loader loader = new Loader();
		loader.load();
		localMaps = loader.getMapsWithoutAtlantis();
		atlantis = loader.getAtlantis();
		ships = loader.getShips();
		hashConfig = loader.getHash();

		windowConfig();

		// Load the player's name from the Preferences
		getPlayerName();

		Settings.PANEL_HOME = new PanelHome(this);
		Settings.PANEL_CONNECTIONS = new PanelConnection(this);
		Settings.PANEL_PREPARE = new PanelPrepare(this);

		cards = new PanelCards();
		add(cards, BorderLayout.CENTER);
		cards.add(Settings.PANEL_HOME, PanelHome.class.getSimpleName());
		cards.add(Settings.PANEL_CONNECTIONS, PanelConnection.class.getSimpleName());
		cards.add(Settings.PANEL_PREPARE, PanelPrepare.class.getSimpleName());

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

		SoundManager.getInstance().setStream(SoundManager.Stream.NONE);

		// cards.showCard(PanelPrepare.class.getSimpleName());

		setVisible(true);
	}

	private void windowConfig()
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(kDefaultWidth, kDefaultHeight);
		setTitle(kWindowTitle);
		setIconImage(ImageShop.UI_LOGO);
	}

	public boolean sendAction(Action action)
	{
		return false;
	}

	public void searchPlayer()
	{
		cards.showCard(PanelConnection.class.getSimpleName());
		NetworkManager.getInstance().setAutodiscoverListener(Settings.PANEL_CONNECTIONS);
	}

	public void placeShips(Host h)
	{
		this.distantPlayerName = h.getName();
		cards.showCard(PanelPrepare.class.getSimpleName());
		NetworkManager.getInstance().removeAutodiscoverListener(Settings.PANEL_CONNECTIONS);
		forceResize();
		SoundManager.getInstance().setStream(SoundManager.Stream.PLACEMENT);
	}

	public void startGame()
	{
		if (distantMaps != null)
			showGame();
		else
			; // TODO: Wait for other player

	}

	@SuppressWarnings("deprecation")
	private void showGame()
	{
		Settings.PANEL_PLAY = new PanelPlay(this);
		cards.add(Settings.PANEL_PLAY, PanelPlay.class.getSimpleName());

		cards.showCard(PanelPlay.class.getSimpleName());
		forceResize();
	}

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

	public void endGame()
	{
		cards.showCard(PanelHome.class.getSimpleName());
	}

	public static PanelPrepare getPanelPrepare()
	{
		return Settings.PANEL_PREPARE;
	}

	public Map[] getLocalMaps()
	{
		return localMaps;
	}

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

	public Map[] getDistantMaps()
	{
		return distantMaps;
	}

	public void setDistantMaps(Map[] maps)
	{
		this.distantMaps = maps;
	}

	public Ship[] getShips()
	{
		return ships;
	}

	public Set<Ship> getDistantShips()
	{
		return distantShips;
	}

	public String getHashConfig()
	{
		return hashConfig;
	}

	public void connectionAttempt(Host h)
	{
		int result = JOptionPane.showConfirmDialog(this,
				String.format(Messages.getString("FrameMain.ConnectionAttemptMessage"), h.getName(), h.getAddress().getHostAddress()),
				Messages.getString("FrameMain.ConnectionAttemptTitle"), JOptionPane.YES_NO_OPTION);
		boolean connectionAccepted = (result == JOptionPane.YES_OPTION);
		NetworkManager.getInstance().connectionResponse(connectionAccepted, h);
	}

	public void connectionRefused(Host h)
	{
		JOptionPane.showMessageDialog(this,
				String.format(Messages.getString("FrameMain.ConnectionRefusedMessage"), h.getName(), h.getAddress().getHostAddress()),
				Messages.getString("FrameMain.ConnectionRefusedTitle"), JOptionPane.ERROR_MESSAGE);
	}

	public String getPlayerName()
	{
		if (playerName == null)
			playerName = Preferences.userNodeForPackage(Main.class).get(kKeyPlayerName, Messages.getString("FrameMain.Player"));
		return playerName;
	}

	public void setPlayerName(String playerName)
	{
		this.playerName = playerName;
		Preferences.userNodeForPackage(Main.class).put(kKeyPlayerName, playerName);
	}

	public String getDistantPlayerName()
	{
		return distantPlayerName;
	}

	public Player getFirstPlayerToPlay()
	{
		return firstPlayerToPlay;
	}

	public void setFirstPlayerToPlay(Player firstPlayerToPlay)
	{
		this.firstPlayerToPlay = firstPlayerToPlay;
	}
}
