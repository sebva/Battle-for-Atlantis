package ch.hearc.p2.battleforatlantis.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.hearc.p2.battleforatlantis.gameengine.Map;
import ch.hearc.p2.battleforatlantis.gameengine.Ship;
import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.ImageShop.ShipType;
import ch.hearc.p2.battleforatlantis.utils.Messages;

public class PanelPrepare extends JPanel
{
	private FrameMain rootFrame;
	private Ship selectedShip;

	public PanelPrepare(FrameMain rootFrame)
	{
		this.rootFrame = rootFrame;
		
		Box box = Box.createHorizontalBox();
		
		// TODO: Correct Map dimensions
		box.add(new Map(10, 10));
		
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
