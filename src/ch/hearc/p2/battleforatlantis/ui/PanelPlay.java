package ch.hearc.p2.battleforatlantis.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import ch.hearc.p2.battleforatlantis.gameengine.Map;
import ch.hearc.p2.battleforatlantis.gameengine.MapType;
import ch.hearc.p2.battleforatlantis.gameengine.Player;
import ch.hearc.p2.battleforatlantis.gameengine.Ship;
import ch.hearc.p2.battleforatlantis.utils.Messages;

public class PanelPlay extends JPanel
{
	private Map levelMe;
	private Map levelOther;

	private FrameMain rootFrame;

	private class PanelPlayerView extends JPanel
	{
		public PanelPlayerView(String playerName, boolean isMe)
		{
			Box box = Box.createVerticalBox();

			box.add(new JLabel(playerName));

			Box boxH = Box.createHorizontalBox();
			boxH.add(new JLabel(isMe ? Messages.getString("PanelPlay.YourMap") : Messages.getString("PanelPlay.OtherMap")));
			boxH.add(Box.createHorizontalGlue());
			// TODO: Retrieve level number
			boxH.add(new JLabel(Messages.getString("PanelPlay.Level") + " 1"));

			box.add(boxH);
			box.add(isMe ? levelMe : levelOther);

			box.add(new JLabel(Messages.getString("PanelPlay.CurrentLevelStatus")));
			box.add(new JProgressBar());

			add(box, BorderLayout.CENTER);
		}
	}
	
	private class PanelProgress extends JPanel
	{
		public PanelProgress()
		{
			// TODO: Remove and replace temporary code
			setLayout(new GridLayout(2, 2));
			add(new JProgressBar());
			add(new JLabel("Porte-avions"));
			add(new JProgressBar());
			add(new JLabel("Croiseur"));
		}
	}
	
	private class PanelStats extends JPanel
	{
		public PanelStats()
		{
			Box box = Box.createVerticalBox();
			// TODO: Retrieve correct statistics
			box.add(new JLabel("14 " + Messages.getString("PanelPlay.TotalShots")));
			box.add(new JLabel("11 " + Messages.getString("PanelPlay.EffectiveShots")));
			box.add(new JLabel("3 " + Messages.getString("PanelPlay.WaterShots")));
			
			add(box, BorderLayout.CENTER);
		}
	}

	public PanelPlay(FrameMain rootFrame)
	{
		this.rootFrame = rootFrame;
		// TODO: Create Map that are of desired size
		levelMe = new Map(10, 10, MapType.SURFACE);
		levelOther = new Map(8, 6, MapType.SURFACE);

		Box boxH = Box.createHorizontalBox();
		// TODO: Retrieve players' names
		boxH.add(new PanelPlayerView("Me", true));
		boxH.add(new JSeparator(SwingConstants.VERTICAL));
		boxH.add(new PanelPlayerView("Other", false));

		boxH.add(new JSeparator(SwingConstants.VERTICAL));
		
		Box boxHUD = Box.createVerticalBox();

		JButton btnCapitulate = new CustomButton(Messages.getString("PanelPlay.Capitulate"));
		btnCapitulate.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				endGame(false);
			}
		});
		boxHUD.add(btnCapitulate);
		
		boxHUD.add(new JLabel(Messages.getString("PanelPlay.ConquestProgress")));
		boxHUD.add(new PanelProgress());
		boxHUD.add(new JSeparator(SwingConstants.HORIZONTAL));
		boxHUD.add(new JLabel(Messages.getString("PanelPlay.Stats")));
		boxHUD.add(new PanelStats());
		boxHUD.add(Box.createVerticalGlue());

		boxH.add(boxHUD);		
		
		add(boxH, BorderLayout.CENTER);
	}

	public void shoot(Box location)
	{

	}

	public void select(Ship ship)
	{

	}

	public void rotate(boolean clockwise)
	{

	}

	public void place(boolean forward)
	{

	}

	public void nextLevel()
	{

	}

	public void endGame(boolean isWinner)
	{
		DialogEndGame.announceGameResult(this, isWinner);
		rootFrame.endGame();
	}

	public void setActiveMap(Player pt, MapType map)
	{

	}

}
