package ch.hearc.p2.battleforatlantis.gameengine;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.BorderUIResource;

import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.ImageShop.ShipType;

public class Box extends JPanel
{
	private boolean discovered = false;
	private Image image;
	private MapElement mapElement;

	public Box()
	{
		// TODO: Remove test code below
		setLayout(new BorderLayout(0,0));
		JLabel img = new JLabel(new ImageIcon(ImageShop.BACKGROUND_SURFACE));
		add(img, BorderLayout.CENTER);
	}

	public void shoot()
	{

	}

}
