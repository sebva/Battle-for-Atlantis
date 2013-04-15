package ch.hearc.p2.battleforatlantis.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import ch.hearc.p2.battleforatlantis.gameengine.Map;
import ch.hearc.p2.battleforatlantis.gameengine.Ship;
import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.ImageShop.ShipType;
import ch.hearc.p2.battleforatlantis.utils.Messages;

public class PanelPrepare extends JPanel
{
	private FrameMain rootFrame;
	private Ship selectedShip;
	private Map mapSurface;
	private Map mapSubmarine;

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
		mapSurface = new Map(10, 10);
		mapSubmarine = new Map(8, 6);

		Box box = Box.createHorizontalBox();

		Box boxMap1 = Box.createVerticalBox();
		boxMap1.add(Box.createVerticalGlue());
		boxMap1.add(new PanelMap(mapSurface));
		box.add(boxMap1);

		box.add(new JSeparator(SwingConstants.VERTICAL));

		Box boxMap2 = Box.createVerticalBox();
		boxMap2.add(Box.createVerticalGlue());
		boxMap2.add(new PanelMap(mapSubmarine));

		box.add(boxMap2);
		box.add(new JSeparator(SwingConstants.VERTICAL));

		Box boxMenu = Box.createVerticalBox();

		JButton btn = new JButton(Messages.getString("PanelPrepare.Validate"), new ImageIcon(ImageShop.UI_BUTTON));
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
		boxMenu.add(new JLabel(new ImageIcon(ImageShop.loadShipImage(ShipType.SHIP, 3, 1, false))));
		boxMenu.add(new JLabel(new ImageIcon(ImageShop.loadShipImage(ShipType.SHIP, 2, 1, false))));
		boxMenu.add(new JLabel(new ImageIcon(ImageShop.loadShipImage(ShipType.SHIP, 4, 1, false))));

		boxMenu.add(Box.createVerticalGlue());
		
		box.add(boxMenu);
		add(box, BorderLayout.CENTER);
	}

	public void select(Ship ship)
	{

	}

	public void place(Box location)
	{

	}

	public void rotate()
	{

	}

	public void start()
	{
		rootFrame.startGame();
	}

}
