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
import ch.hearc.p2.battleforatlantis.gameengine.Map;
import ch.hearc.p2.battleforatlantis.gameengine.MapElement;
import ch.hearc.p2.battleforatlantis.gameengine.MapType;
import ch.hearc.p2.battleforatlantis.gameengine.Player;
import ch.hearc.p2.battleforatlantis.gameengine.Ship;
import ch.hearc.p2.battleforatlantis.utils.ImageShop;
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

	/** Total shots **/
	private int totalShots;
	/** Shots in ships **/
	private int effectiveShots;
	/** Shots in water **/
	private int waterShots;

	private Map currentLocalMap;
	private Map currentDistantMap;
	private final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private Player playerPlaying;

	private Ship selectedShip = null;

	private PanelPlayerInfos infosLocal;
	private PanelPlayerInfos infosDistant;

	private CustomProgress progressLocal;
	private CustomProgress progressDistant;

	private static final Font fontName = new Font("Arial", Font.BOLD, 20);
	private static final Font fontLevel = new Font("Arial", Font.PLAIN, 14);

	/**
	 * Panel containing player informations (upper part of map)
	 */
	private class PanelPlayerInfos extends JPanel
	{
		private JLabel labelLevel;
		private boolean playing;

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

		public void setLevelNumber(Integer level)
		{
			this.labelLevel.setText(Messages.getString("PanelPlay.Level") + " " + level.toString());
		}

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
		private CardLayout cards;
		private Map atlantis;

		/**
		 * 
		 * @param maps The maps to be shown (usually a reference to FrameMain.getLocal/DistantMaps)
		 * @param atlantis The Atlantis map object (usually a reference to FrameMain.getAtlantis).
		 */
		public PanelMaps(Map[] maps, Map atlantis)
		{
			this.setBorder(BorderFactory.createLineBorder(new Color(97, 173, 233), 1));

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
	}

	/**
	 * Instantiate a new PanelPlay. All the maps have to be set in FrameMain before calling this constructor !
	 * 
	 * @param rootFrame The application's FrameMain object
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
		Box menuMaps = Box.createVerticalBox();

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
		Box boxDistant = Box.createVerticalBox();
		boxDistant.add(this.infosDistant);
		boxDistant.add(this.levelsOther);

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

		// Button for capitulation
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
		JButton btnNextLevel = new CustomButton(Messages.getString("PanelPlay.NextLevel"));
		btnNextLevel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				nextLevel();
			}
		});

		// Progress bars
		// TODO gather total number of occupied boxes on current for maximum value (instead of 100)
		this.progressLocal = new CustomProgress(0, 100);
		this.progressDistant = new CustomProgress(0, 100);

		this.progressLocal.setValue(30);
		this.progressDistant.setValue(55);

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

		// Canvas for progress bars
		Box canvasProgressLocal = Box.createVerticalBox();
		canvasProgressLocal.add(labelProgressLocal);
		canvasProgressLocal.add(progressLocal);
		Box canvasProgressDistant = Box.createVerticalBox();
		canvasProgressDistant.add(labelProgressDistant);
		canvasProgressDistant.add(progressDistant);
		Box canvasLevels = Box.createHorizontalBox();
		canvasLevels.add(Box.createHorizontalGlue());
		canvasLevels.add(canvasProgressLocal);
		canvasLevels.add(Box.createHorizontalGlue());
		canvasLevels.add(canvasProgressDistant);
		canvasLevels.add(Box.createHorizontalGlue());

		// Main canvas
		add(canvasMaps, BorderLayout.CENTER);
		add(canvasLevels, BorderLayout.NORTH);

		// Assign currently playing player
		// TODO better
		boolean isLocalPlaying = playerPlaying == Player.LOCAL;
		infosLocal.setPlaying(isLocalPlaying);
		infosDistant.setPlaying(!isLocalPlaying);
	}

	/**
	 * Called when the remote player shoots on a particular box
	 * 
	 * @param location The reference to the Box that is targeted
	 */
	public void shoot(ch.hearc.p2.battleforatlantis.gameengine.Box location)
	{
		if (playerPlaying != Player.LOCAL)
			return;

		// Get occupier of the box shot
		MapElement occupier = location.getOccupier();

		// If we shot a ship
		if (occupier != null)
		{
			effectiveShots++;

			if (occupier instanceof Ship)
			{
				// FIXME: NullPointerException in the following method call !
				// this.panelProgress.addProgress((Ship) occupier);
			}
		}
		// Or shot nothing
		else
			waterShots++;

		// Count total shots
		totalShots++;

		// Made the shot at the box location
		location.shoot();

		endCurrentTurn();

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

		ship.getCenter().getMap().removeShipControls(ship);
		ship.moveOut();
		ship.place(forward);

		endCurrentTurn();

		new MoveAction(ship, ship.getCenter(), ship.getOrientation()).send();
	}

	public void select(Ship ship, int mouseButton)
	{
		if (playerPlaying != Player.LOCAL)
			return;

		if (selectedShip != null)
		{
			ship.getCenter().getMap().removeShipControls(ship);
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
			case ATLANTIS:
				endGame(true, false);
				return;
		}

		setActiveMap(newMap, Player.DISTANT);

		endCurrentTurn();

		new NextLevelAction(newMap).send();
	}

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
		if (player == Player.LOCAL)
			currentLocalMap = rootFrame.getMapByType(map, Player.LOCAL);
		else
			currentDistantMap = rootFrame.getMapByType(map, Player.DISTANT);

		if (MapType.ATLANTIS == currentDistantMap.getType() && MapType.ATLANTIS == currentLocalMap.getType())
		{
			// We play on one panel
			// for (Component c : distantPlayerView.getComponents())
			// distantPlayerView.remove(c);
			// remove(distantPlayerView);
			player = Player.LOCAL;
		}

		if (player == Player.LOCAL)
			levelsMe.showMap(map);
		else
			levelsOther.showMap(map);
	}

	public Map getCurrentLevel(Player player)
	{
		return player == Player.LOCAL ? currentLocalMap : currentDistantMap;
	}

}
