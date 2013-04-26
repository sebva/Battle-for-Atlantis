package ch.hearc.p2.battleforatlantis.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ch.hearc.p2.battleforatlantis.action.Action;
import ch.hearc.p2.battleforatlantis.gameengine.Map;
import ch.hearc.p2.battleforatlantis.gameengine.Ship;
import ch.hearc.p2.battleforatlantis.gameinit.Loader;
import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.Messages;
import ch.hearc.p2.battleforatlantis.utils.Settings;

public class FrameMain extends JFrame
{
	private static final int kDefaultWidth = 1550;
	private static final int kDefaultHeight = 850;
	private static final String kWindowTitle = Messages.getString("FrameMain.WindowTitle");
	
	private PanelCards cards;
	
	private Map[] localMaps;
	private Ship[] ships;
	
	
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
		Loader loader = new Loader();
		loader.load();
		localMaps = loader.getMapsWithoutAtlantis();
		ships = loader.getShips();
		
		windowConfig();
		
		Settings.PANEL_HOME = new PanelHome(this);
		Settings.PANEL_CONNECTIONS = new PanelConnection(this);
		Settings.PANEL_PREPARE = new PanelPrepare(this);
		Settings.PANEL_PLAY = new PanelPlay(this);
		
		cards = new PanelCards();
		add(cards, BorderLayout.CENTER);
		cards.add(Settings.PANEL_HOME, PanelHome.class.getSimpleName());
		cards.add(Settings.PANEL_CONNECTIONS, PanelConnection.class.getSimpleName());
		cards.add(Settings.PANEL_PREPARE, PanelPrepare.class.getSimpleName());
		cards.add(Settings.PANEL_PLAY, PanelPlay.class.getSimpleName());
		
		setVisible(true);
	}

	private void windowConfig()
	{
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
	}

	public void placeShips()
	{
		cards.showCard(PanelPrepare.class.getSimpleName());
	}

	public void startGame()
	{
		cards.showCard(PanelPlay.class.getSimpleName());
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

	public Ship[] getShips()
	{
		return ships;
	}

}
