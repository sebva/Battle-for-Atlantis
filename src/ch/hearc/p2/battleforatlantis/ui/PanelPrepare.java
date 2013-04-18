package ch.hearc.p2.battleforatlantis.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import ch.hearc.p2.battleforatlantis.gameengine.Map;
import ch.hearc.p2.battleforatlantis.gameengine.MapElement;
import ch.hearc.p2.battleforatlantis.gameengine.MapType;
import ch.hearc.p2.battleforatlantis.gameengine.Ship;
import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.ImageShop.ShipType;
import ch.hearc.p2.battleforatlantis.utils.Messages;

public class PanelPrepare extends JPanel
{
	private FrameMain rootFrame;
	private Ship selectedShip = null;
	private Map mapSurface;
	private Map mapSubmarine;
	private MapElement ships[];

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

		// TODO: Correct Map dimensions
		mapSurface = new Map(10, 10, MapType.SURFACE);
		mapSubmarine = new Map(8, 6, MapType.SUBMARINE);
		mapSurface.setPreparationListeners();
		mapSubmarine.setPreparationListeners();
		
		// TODO: Temporary hardcoded ships instanciation
		ships = new MapElement[6];
		ships[0] = new Ship(ShipType.SHIP, 2);
		ships[1] = new Ship(ShipType.SHIP, 3);
		ships[2] = new Ship(ShipType.SHIP, 4);
		ships[3] = new Ship(ShipType.SHIP, 5);
		ships[4] = new Ship(ShipType.SUBMARINE, 2);
		ships[5] = new Ship(ShipType.SUBMARINE, 3);

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

		boxMenu.add(new JLabel(Messages.getString("PanelPrepare.Boats")));
		/*
		boxMenu.add(new JLabel(new ImageIcon(ImageShop.loadShipImage(ShipType.SHIP, 3, 1, false))));
		boxMenu.add(new JLabel(new ImageIcon(ImageShop.loadShipImage(ShipType.SHIP, 2, 1, false))));
		boxMenu.add(new JLabel(new ImageIcon(ImageShop.loadShipImage(ShipType.SHIP, 4, 1, false))));
		*/
		for (int i=0; i<6; i++)
		{
			boxMenu.add(ships[i]);
			final Ship ship = (Ship)ships[i];
			ships[i].addMouseListener(new MouseAdapter()
			{			
				@Override
				public void mousePressed(MouseEvent e)
				{
					select(ship);
				}
			});
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
		System.out.println("[PanelPrepare] selected ship");
		
	}

	public void place(ch.hearc.p2.battleforatlantis.gameengine.Box box)
	{
		if (this.selectedShip != null)
		{
			this.selectedShip.move(box, null);
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
