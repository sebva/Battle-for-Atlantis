package ch.hearc.p2.battleforatlantis.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import ch.hearc.p2.battleforatlantis.gameengine.Map;
import ch.hearc.p2.battleforatlantis.gameengine.MapElement;
import ch.hearc.p2.battleforatlantis.gameengine.MapType;
import ch.hearc.p2.battleforatlantis.gameengine.Ship;
import ch.hearc.p2.battleforatlantis.gameengine.ShipType;
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
	 * Ships and submarines listed to be placed
	 */
	private MapElement ships[];
	
	/**
	 * Debug logger
	 */
	private Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * Hack to preserve formatting. This class, extending JPanel, does nothing but add a Map (also extending JPanel) to itself. This hack is necessary in order
	 * to maintain the Boxes composing the Map together.
	 * 
	 * @author Sébastien Vaucher
	 */
	private class PanelMap extends JPanel
	{
		public PanelMap(Map map)
		{
			add(map, BorderLayout.CENTER);
		}
	}

	/**
	 * Default constructor
	 * 
	 * @param rootFrame Parent frame
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

		// Create canvas
		Box box = Box.createHorizontalBox();

		// Add surface map
		Box boxMapSurface = Box.createVerticalBox();
		boxMapSurface.add(Box.createVerticalGlue());
		boxMapSurface.add(new PanelMap(mapSurface));
		box.add(boxMapSurface);

		// Add separator
		box.add(new JSeparator(SwingConstants.VERTICAL));

		// Add submarine map
		Box boxMapSubmarine = Box.createVerticalBox();
		boxMapSubmarine.add(Box.createVerticalGlue());
		boxMapSubmarine.add(new PanelMap(mapSubmarine));
		box.add(boxMapSubmarine);
		
		// Add separator
		box.add(new JSeparator(SwingConstants.VERTICAL));

		// Create menu
		Box boxMenu = Box.createVerticalBox();

		// Add "Validate" button
		CustomButton btn = new CustomButton(Messages.getString("PanelPrepare.Validate"));
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
			if (currentType == null || currentType != ship.getType())
			{
				currentType = ship.getType();
				switch (currentType)
				{
					case SHIP:
						boxMenu.add(new JLabel(Messages.getString("PanelPrepare.Boats")));
						break;
					case SUBMARINE:
						boxMenu.add(new JLabel(Messages.getString("PanelPrepare.Submarines")));
						break;
				}
			}
			
			// Add listener for ship selection
			ship.addMouseListener(new MouseAdapter()
			{				
				@Override
				public void mousePressed(MouseEvent e)
				{
					select((Ship)e.getComponent());
				}
			});
			
			// Add ship
			boxMenu.add(ship);
		}
		
		// Finalize
		boxMenu.add(Box.createVerticalGlue());
		box.add(boxMenu);
		add(box, BorderLayout.CENTER);
	}

	/**
	 * External call for ship selection
	 * 
	 * @param ship Selected ship
	 */
	public void select(Ship ship)
	{
		if (this.selectedShip != null)
		{
			selectedShip.setBackground(Color.BLACK);
		}
		this.selectedShip = ship;
		this.selectedShip.setBackground(Color.DARK_GRAY);
		log.info("selected ship");

	}

	/**
	 * External call for place ship on map
	 * 
	 * @param box Center of ship
	 */
	public void place(ch.hearc.p2.battleforatlantis.gameengine.Box box)
	{
		if (this.selectedShip != null)
		{
			if (box == null)
			{
				this.selectedShip.moveOut();
			}
			else
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
			this.selectedShip.rotate();
		}
	}

	/**
	 * External call for start game
	 */
	public void start()
	{
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
