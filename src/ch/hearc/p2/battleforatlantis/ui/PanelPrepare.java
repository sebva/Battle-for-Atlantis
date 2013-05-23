package ch.hearc.p2.battleforatlantis.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import ch.hearc.p2.battleforatlantis.action.StartGameAction;
import ch.hearc.p2.battleforatlantis.gameengine.Map;
import ch.hearc.p2.battleforatlantis.gameengine.MapType;
import ch.hearc.p2.battleforatlantis.gameengine.Ship;
import ch.hearc.p2.battleforatlantis.gameengine.ShipType;
import ch.hearc.p2.battleforatlantis.net.NetworkManager;
import ch.hearc.p2.battleforatlantis.utils.Messages;

public class PanelPrepare extends JPanel
{
	/**
	 * Main frame displaying the panel
	 */
	private FrameMain rootFrame;

	/**
	 * Currently selected ship for placement, or null if no ship selected
	 */
	private Ship selectedShip = null;

	/**
	 * Surface map for placing ships
	 */
	private Map mapSurface;

	/**
	 * Submarine map for placing submarines
	 */
	private Map mapSubmarine;

	/**
	 * Debug logger
	 */
	private Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * Default constructor
	 * 
	 * @param rootFrame
	 *            Parent frame
	 */
	public PanelPrepare(FrameMain rootFrame)
	{
		// Input fields
		this.rootFrame = rootFrame;

		// Gather maps
		Map[] maps = rootFrame.getLocalMaps();
		for (Map map : maps)
		{
			if (map.getType() == MapType.SURFACE)
				mapSurface = map;
			else if (map.getType() == MapType.SUBMARINE)
				mapSubmarine = map;
		}

		// Set listeners on maps for preparation
		mapSurface.setPreparationListeners();
		mapSubmarine.setPreparationListeners();

		mapSurface.setAlignmentY(TOP_ALIGNMENT);
		mapSubmarine.setAlignmentY(TOP_ALIGNMENT);

		// Create canvas
		Box box = Box.createHorizontalBox();

		// Add surface map
		Box boxMapSurface = Box.createVerticalBox();
		boxMapSurface.add(Box.createVerticalGlue());
		boxMapSurface.add(mapSurface);
		boxMapSurface.add(Box.createVerticalGlue());
		box.add(Box.createHorizontalGlue());
		box.add(boxMapSurface);
		box.add(Box.createHorizontalGlue());

		// Add separator
		box.add(Box.createHorizontalStrut(20));
		box.add(new JSeparator(SwingConstants.VERTICAL));
		box.add(Box.createHorizontalStrut(20));

		// Add submarine map
		Box boxMapSubmarine = Box.createVerticalBox();
		boxMapSubmarine.add(Box.createVerticalGlue());
		boxMapSubmarine.add(mapSubmarine);
		boxMapSubmarine.add(Box.createVerticalGlue());
		box.add(boxMapSubmarine);
		box.add(Box.createHorizontalGlue());

		// Add separator
		box.add(Box.createHorizontalStrut(20));
		box.add(new JSeparator(SwingConstants.VERTICAL));
		box.add(Box.createHorizontalStrut(20));

		// Create menu
		Box boxMenu = Box.createVerticalBox();

		// Add "Validate" button
		CustomButton btn = new CustomButton(Messages.getString("PanelPrepare.Validate"));
		btn.setAlignmentX(RIGHT_ALIGNMENT);
		btn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				start();
			}
		});
		boxMenu.add(btn);

		// Add ships to menu
		ShipType currentType = null;
		for (Ship ship : rootFrame.getShips())
		{
			// Add label
			/*
			if (currentType == null || currentType != ship.getType())
			{
				currentType = ship.getType();
				JLabel label = null;
				
				switch (currentType)
				{
					case SHIP:
						label = new JLabel(Messages.getString("PanelPrepare.Boats"));
						break;
					case SUBMARINE:
						label = new JLabel(Messages.getString("PanelPrepare.Submarines"));
						break;
				}
				label.setAlignmentX(RIGHT_ALIGNMENT);
				boxMenu.add(label);
			}*/

			// Add ship
			ship.setAlignmentX(RIGHT_ALIGNMENT);
			boxMenu.add(ship);
		}

		// Finalize
		boxMenu.add(Box.createVerticalGlue());
		// box.add(boxMenu);
		box.setBackground(Color.RED);

		setLayout(new BorderLayout());
		add(box, BorderLayout.CENTER);
		add(boxMenu, BorderLayout.EAST);
	}

	/**
	 * A ship has been clicked, either by its button on the right or on the map.
	 * 
	 * @param ship
	 *            Clicked ship
	 */
	public void shipClick(Ship ship)
	{
		// The ship clicked is currently floating, so we validate its position
		if (this.selectedShip == ship && ship.getCenter() != null)
		{
			selectedShip.setBackgroundColor(Color.DARK_GRAY);
			selectedShip = null;
		}
		
		// The ship is not floating, so it's been clicked from either the button on the right
		// or on the map (the user validated the position but is not satisfied with it).
		else
		{
			if (selectedShip != null)
				selectedShip.setBackgroundColor(Color.BLACK);
			this.selectedShip = ship;
			this.selectedShip.setBackgroundColor(Color.BLUE);
			log.info("selected ship");
		}
	}

	/**
	 * External call for place ship on map
	 * 
	 * @param box
	 *            Center of ship
	 */
	public void place(ch.hearc.p2.battleforatlantis.gameengine.Box box)
	{
		if (this.selectedShip != null)
		{
			if (box == null)
			{
				this.selectedShip.moveOut();
			}
			// The ship is in its environment
			else if ((selectedShip.getType() == ShipType.SHIP && box.getMapType() == MapType.SURFACE)
					|| (selectedShip.getType() == ShipType.SUBMARINE && box.getMapType() == MapType.SUBMARINE))
			{
				this.selectedShip.move(box, null);
			}
		}
	}

	/**
	 * External call for rotate ship
	 */
	public void rotate()
	{
		if (this.selectedShip != null)
		{
			this.selectedShip.rotate(true);
		}
	}

	/**
	 * External call for start game
	 */
	public void start()
	{
		// Check that every ship has been placed
		for (Ship ship : rootFrame.getShips())
			if (ship.getCenter() == null)
			{
				JOptionPane.showMessageDialog(this, Messages.getString("PanelPrepare.ValidateErrorMessage"),
						Messages.getString("PanelPrepare.ValidateErrorTitle"), JOptionPane.ERROR_MESSAGE);
				return;
			}

		StartGameAction sga = new StartGameAction();
		sga.addMap(mapSurface);
		sga.addMap(mapSubmarine);
		NetworkManager.getInstance().send(sga);

		rootFrame.startGame();
	}

	/**
	 * Getter for surface map
	 * 
	 * @return Surface map
	 */
	public Map getMapSurface()
	{
		return this.mapSurface;
	}

	/**
	 * Getter for submarine map
	 * 
	 * @return Submarine map
	 */
	public Map getMapSubmarine()
	{
		return this.mapSubmarine;
	}

}
