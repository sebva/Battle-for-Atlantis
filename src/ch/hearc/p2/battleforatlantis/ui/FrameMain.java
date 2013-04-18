package ch.hearc.p2.battleforatlantis.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ch.hearc.p2.battleforatlantis.action.Action;
import ch.hearc.p2.battleforatlantis.utils.Messages;

public class FrameMain extends JFrame
{
	private static final int kDefaultWidth = 1550;
	private static final int kDefaultHeight = 850;
	private static final String kWindowTitle = Messages.getString("FrameMain.WindowTitle");
	
	private PanelCards cards;
	
	private static PanelHome panelHome;
	private static PanelConnection panelConnection;
	private static PanelPrepare panelPrepare;
	private static PanelPlay panelPlay;
	
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
	
	public FrameMain()
	{
		windowConfig();
		
		FrameMain.panelHome = new PanelHome(this);
		FrameMain.panelConnection = new PanelConnection(this);
		FrameMain.panelPrepare = new PanelPrepare(this);
		FrameMain.panelPlay = new PanelPlay(this);
		
		cards = new PanelCards();
		add(cards, BorderLayout.CENTER);
		cards.add(panelHome, PanelHome.class.getSimpleName());
		cards.add(panelConnection, PanelConnection.class.getSimpleName());
		cards.add(panelPrepare, PanelPrepare.class.getSimpleName());
		cards.add(panelPlay, PanelPlay.class.getSimpleName());
		
		setVisible(true);
	}

	private void windowConfig()
	{
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(kDefaultWidth, kDefaultHeight);
		setTitle(kWindowTitle);
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
		return FrameMain.panelPrepare;
	}

}
