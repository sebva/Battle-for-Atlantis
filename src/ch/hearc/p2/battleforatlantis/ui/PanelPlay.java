package ch.hearc.p2.battleforatlantis.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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

/**
 * The Panel where the game takes place.
 * 
 * @author sebastie.vaucher
 */
public class PanelPlay extends JPanel
{
	/** The FrameMain object containing this Panel */
	private FrameMain rootFrame;
	/** The PanelMaps displaying local maps */
	private PanelMaps levelsMe;
	/** The PanelMaps displaying distant maps */
	private PanelMaps levelsOther;

	/**
	 * The Panel that shows the maps as well as some stats relative to this map.
	 */
	private class PanelPlayerView extends JPanel
	{
		/**
		 * 
		 * @param playerName The player's name to be shown
		 * @param isMe Whether to display local maps
		 */
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
			box.add(isMe ? levelsMe : levelsOther);

			box.add(new JLabel(Messages.getString("PanelPlay.CurrentLevelStatus")));
			box.add(new JProgressBar());

			add(box, BorderLayout.CENTER);
		}
	}

	/**
	 * CardLayout to display one map at time
	 */
	public class PanelMaps extends JPanel
	{
		private CardLayout cards;

		/**
		 * 
		 * @param maps The maps to be shown (usually a reference to FrameMain.getLocal/DistantMaps)
		 * @param atlantis The Atlantis map object (usually a reference to FrameMain.getAtlantis).
		 */
		public PanelMaps(Map[] maps, Map atlantis)
		{
			cards = new CardLayout();
			setLayout(cards);
			for (Map map : maps)
				add(map, map.getType().toString());
			add(atlantis, atlantis.getType().toString());

			showMap(MapType.SURFACE);
		}

		/**
		 * Display the map relative to a particular type
		 * @param type The MapType of the map to be displayed
		 */
		public void showMap(MapType type)
		{
			cards.show(this, type.toString());
		}
	}

	/**
	 * A subpanel that shows his progression to the player
	 */
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

	/**
	 * A subpanel containing some statistics
	 */
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

	/**
	 * Instantiate a new PanelPlay.
	 * All the maps have to be set in FrameMain before calling this constructor !
	 * @param rootFrame The application's FrameMain object
	 */
	public PanelPlay(FrameMain rootFrame)
	{
		this.rootFrame = rootFrame;

		levelsMe = new PanelMaps(rootFrame.getLocalMaps(), rootFrame.getMapByType(MapType.ATLANTIS, true));
		levelsOther = new PanelMaps(rootFrame.getDistantMaps(), rootFrame.getMapByType(MapType.ATLANTIS, true));

		Box boxH = Box.createHorizontalBox();

		boxH.add(new PanelPlayerView(rootFrame.getPlayerName(), true));
		boxH.add(new JSeparator(SwingConstants.VERTICAL));
		boxH.add(new PanelPlayerView(rootFrame.getDistantPlayerName(), false));

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

	/**
	 * Called when the remote player shoots on a particular box
	 * @param location The reference to the Box that is targeted
	 */
	public void shoot(Box location)
	{

	}

	/**
	 * Rotate a particular Ship
	 * @param ship The ship to be rotated
	 * @param clockwise Whether the boat is to be rotated in a clockwise angle
	 */
	public void rotate(Ship ship, boolean clockwise)
	{

	}

	/**
	 * Move a particular ship forwards/backwards
	 * @param forward True = forwards, false = backwards
	 */
	public void place(Ship ship, boolean forward)
	{

	}

	/**
	 * Called when the local player asks to go to the next level 
	 */
	public void nextLevel()
	{

	}

	/**
	 * The game is finished
	 * @param isWinner True when the local player is the winner
	 */
	public void endGame(boolean isWinner)
	{
		DialogEndGame.announceGameResult(this, isWinner);
		rootFrame.endGame();
	}

	/**
	 * Like nextLevel(), but more generic because the Player to which
	 * the level change applies can be set as well as the destination MapType
	 * @param pt The Player to which this applies
	 * @param map The MapType to be shown on the Player's map
	 */
	public void setActiveMap(Player pt, MapType map)
	{

	}

}
