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
	private FrameMain rootFrame;
	private Ship selectedShip = null;
	private Map mapSurface;
	private Map mapSubmarine;
	private MapElement ships[];
	
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

	public PanelPrepare(FrameMain rootFrame)
	{
		this.rootFrame = rootFrame;

		Map[] maps = rootFrame.getLocalMaps();
		for (Map map : maps)
		{
			if (map.getType() == MapType.SURFACE)
				mapSurface = map;
			else if (map.getType() == MapType.SUBMARINE)
				mapSubmarine = map;
		}
		mapSurface.setPreparationListeners();
		mapSubmarine.setPreparationListeners();

		Box box = Box.createHorizontalBox();

		Box boxMapSurface = Box.createVerticalBox();
		boxMapSurface.add(Box.createVerticalGlue());
		boxMapSurface.add(new PanelMap(mapSurface));
		box.add(boxMapSurface);

		box.add(new JSeparator(SwingConstants.VERTICAL));

		Box boxMapSubmarine = Box.createVerticalBox();
		boxMapSubmarine.add(Box.createVerticalGlue());
		boxMapSubmarine.add(new PanelMap(mapSubmarine));

		box.add(boxMapSubmarine);
		box.add(new JSeparator(SwingConstants.VERTICAL));

		Box boxMenu = Box.createVerticalBox();

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

		ShipType currentType = null;
		for (Ship ship : rootFrame.getShips())
		{
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
			ship.addMouseListener(new MouseAdapter()
			{				
				@Override
				public void mousePressed(MouseEvent e)
				{
					select((Ship)e.getComponent());
				}
			});
			boxMenu.add(ship);
		}

		boxMenu.add(Box.createVerticalGlue());

		box.add(boxMenu);
		add(box, BorderLayout.CENTER);
	}

	public void select(Ship ship)
	{
		if (this.selectedShip != null)
		{
			selectedShip.setBackground(Color.LIGHT_GRAY);
		}
		this.selectedShip = ship;
		this.selectedShip.setBackground(Color.DARK_GRAY);
		log.info("selected ship");

	}

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

	public void rotate()
	{
		if (this.selectedShip != null)
		{
			this.selectedShip.rotate();
		}
	}

	public void start()
	{
		rootFrame.startGame();
	}

	public Map getMapSurface()
	{
		return this.mapSurface;
	}

	public Map getMapSubmarine()
	{
		return this.mapSubmarine;
	}

}
