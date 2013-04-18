package ch.hearc.p2.battleforatlantis.gameengine;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	private MapType type;
	private Dimension size;

	public Box(MapType type)
	{
		this.type = type;
		switch (type)
		{
			case SURFACE:
				this.image = ImageShop.BACKGROUND_SURFACE;
				break;
			case SUBMARINE:
				this.image = ImageShop.BACKGROUND_SUBMARINE;
				break;
			case ATLANTIS:
				this.image = ImageShop.BACKGROUND_ATLANTIS;
				break;
		}
		
		this.size = new Dimension(60, 60);
		setPreferredSize(this.size);
		setMinimumSize(this.size);
		setMaximumSize(this.size);
	}

	public void shoot()
	{

	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(this.image, 0, 0, null);
	}

}
