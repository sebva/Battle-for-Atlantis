package ch.hearc.p2.battleforatlantis.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import ch.hearc.p2.battleforatlantis.action.EndGameAction;
import ch.hearc.p2.battleforatlantis.action.EndGameAction.EndGameCause;
import ch.hearc.p2.battleforatlantis.action.MoveAction;
import ch.hearc.p2.battleforatlantis.action.NextLevelAction;
import ch.hearc.p2.battleforatlantis.action.ShootAction;
import ch.hearc.p2.battleforatlantis.gameengine.Map;
import ch.hearc.p2.battleforatlantis.gameengine.MapElement;
import ch.hearc.p2.battleforatlantis.gameengine.MapType;
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
	
	/** Stats of the shots made **/
	private PanelStats panelStats;
	
	/** Total shots **/
	private int totalShots;
	/** Shots in ships **/
	private int effectiveShots;
	/** Shots in water **/ 
	private int waterShots;
	
	private Map currentLocalMap;
	private Map currentDistantMap;
	private final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private PanelPlayerView distantPlayerView;
	

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
		private Map atlantis;

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
				add(map, map.getType().name());
			
			this.atlantis = atlantis;

			showMap(MapType.SURFACE);
		}

		/**
		 * Display the map relative to a particular type
		 * @param type The MapType of the map to be displayed
		 */
		public void showMap(MapType type)
		{
			if(type == MapType.ATLANTIS && getComponentCount() <= 2)
				add(atlantis, atlantis.getType().name());
			
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
		private JLabel labelTotalShots;
		private JLabel labelEffectiveShots;
		private JLabel labelWaterShots;

		public PanelStats()
		{
			Box box = Box.createVerticalBox();
			
			labelTotalShots = new JLabel("");
			box.add(labelTotalShots);
			labelEffectiveShots = new JLabel("");
			box.add(labelEffectiveShots);
			labelWaterShots = new JLabel("");
			box.add(labelWaterShots);

			add(box, BorderLayout.CENTER);
		}
		
		public void refresh()
		{
			labelTotalShots.setText(totalShots + " " + Messages.getString("PanelPlay.TotalShots"));
			labelEffectiveShots.setText(effectiveShots + " " + Messages.getString("PanelPlay.EffectiveShots"));
			labelWaterShots.setText(waterShots + " " + Messages.getString("PanelPlay.WaterShots"));
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

		// FIXME: /!\ Swing does not permit a JPanel to be in two places at the same time !
		Map atlantis = rootFrame.getMapByType(MapType.ATLANTIS, true);
		levelsOther = new PanelMaps(rootFrame.getDistantMaps(), atlantis);
		levelsMe = new PanelMaps(rootFrame.getLocalMaps(), atlantis);
		
		currentLocalMap = rootFrame.getMapByType(MapType.SURFACE, true);
		currentDistantMap = rootFrame.getMapByType(MapType.SURFACE, false);

		Box boxH = Box.createHorizontalBox();

		boxH.add(new PanelPlayerView(rootFrame.getPlayerName(), true));
		boxH.add(new JSeparator(SwingConstants.VERTICAL));
		distantPlayerView = new PanelPlayerView(rootFrame.getDistantPlayerName(), false);
		boxH.add(distantPlayerView);

		boxH.add(new JSeparator(SwingConstants.VERTICAL));

		Box boxHUD = Box.createVerticalBox();

		JButton btnCapitulate = new CustomButton(Messages.getString("PanelPlay.Capitulate"));
		btnCapitulate.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				endGame(false, false);
			}
		});
		boxHUD.add(btnCapitulate);
		
		JButton btnNextLevel = new CustomButton(Messages.getString("PanelPlay.NextLevel"));
		btnNextLevel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				nextLevel();
			}
		});
		boxHUD.add(btnNextLevel);

		boxHUD.add(new JLabel(Messages.getString("PanelPlay.ConquestProgress")));
		boxHUD.add(new PanelProgress());
		boxHUD.add(new JSeparator(SwingConstants.HORIZONTAL));
		boxHUD.add(new JLabel(Messages.getString("PanelPlay.Stats")));
		
		panelStats = new PanelStats();
		boxHUD.add(panelStats);
		
		boxHUD.add(Box.createVerticalGlue());

		boxH.add(boxHUD);

		add(boxH, BorderLayout.CENTER);
	}

	/**
	 * Called when the remote player shoots on a particular box
	 * @param location The reference to the Box that is targeted
	 */
	public void shoot(ch.hearc.p2.battleforatlantis.gameengine.Box location)
	{
		// Get occupier of the box shot
		MapElement occupier = location.getOccupier();
		
		// If we shot a ship
		if (occupier != null)
			effectiveShots++;
		// Or shot nothing
		else
			waterShots++;
		
		// Count total shots
		totalShots++;
		
		// Refresh the stats pannel
		panelStats.refresh();
		
		// Made the shot at the box location
		location.shoot();
		
		// Send the shot to the opponent
		new ShootAction(location).send();
	}

	/**
	 * Rotate a particular Ship
	 * @param ship The ship to be rotated
	 * @param clockwise Whether the boat is to be rotated in a clockwise angle
	 */
	public void rotate(Ship ship, boolean clockwise)
	{
		ship.rotate(clockwise);
		new MoveAction(ship, ship.getCenter(), ship.getOrientation()).send();
	}

	/**
	 * Move a particular ship forwards/backwards
	 * @param forward True = forwards, false = backwards
	 */
	public void place(Ship ship, boolean forward)
	{
		ship.place(forward);
		new MoveAction(ship, ship.getCenter(), ship.getOrientation()).send();
	}

	/**
	 * Called when a player asks to go to the next level 
	 */
	public void nextLevel()
	{
		log.info("Next level");
		
		MapType oldMap = currentDistantMap.getType();
		
		MapType newMap = null;
		switch (oldMap)
		{
			case SUBMARINE:
				newMap = MapType.ATLANTIS;
				break;
			case SURFACE:
				newMap = MapType.SUBMARINE;
				break;
			default:
				// Should not happen
				throw new RuntimeException("nextLevel requested, but player is at latest level");
		}
		
		setActiveMap(newMap, false);
		
		new NextLevelAction(newMap).send();
	}

	/**
	 * The game is finished
	 * @param isWinner True when the local player is the winner
	 * @param fromNetwork True if this call comes from the network
	 */
	public void endGame(boolean isWinner, boolean fromNetwork)
	{
		if(!fromNetwork)
		{
			EndGameCause cause = isWinner ? EndGameCause.ATLANTIS_DESTROYED : EndGameCause.SURRENDERED;
			new EndGameAction(!isWinner, cause).send();
		}
		DialogEndGame.announceGameResult(this, isWinner);
		rootFrame.endGame();
	}

	/**
	 * Like nextLevel(), but more generic because the Player to which
	 * the level change applies can be set as well as the destination MapType
	 * @param map The MapType to be shown on the Player's map
	 * @param local True if this applies to the local player
	 */
	public void setActiveMap(MapType map, boolean local)
	{
		if(local)

			currentLocalMap = rootFrame.getMapByType(map, true);
		else
			currentDistantMap = rootFrame.getMapByType(map, false);
		
		if(MapType.ATLANTIS == currentDistantMap.getType() &&
				MapType.ATLANTIS == currentLocalMap.getType())
		{
			// We play on one panel
			for(Component c : distantPlayerView.getComponents())
				distantPlayerView.remove(c);
			remove(distantPlayerView);
			local = true;
		}
		
		if(local)
			levelsMe.showMap(map);
		else
			levelsOther.showMap(map);
		
		validate();	
	}
	
	public Map getCurrentLevel(boolean local)
	{
		return local ? currentLocalMap : currentDistantMap;
	}

}
