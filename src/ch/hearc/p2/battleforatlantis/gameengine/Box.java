package ch.hearc.p2.battleforatlantis.gameengine;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

import ch.hearc.p2.battleforatlantis.utils.ImageShop;

public class Box extends JPanel
{
	private boolean discovered = false;
	private Image imageBox;
	private Image imageOccupier = null;
	private Map map;
	private MapElement occupier;
	private MapType type;
	private Dimension size;
	private int x;
	private int y;

	public Box(Map map, MapType type, int x, int y)
	{
		this.type = type;
		this.map = map;
		this.x = x;
		this.y = y;

		switch (type)
		{
			case SURFACE:
				this.imageBox = ImageShop.BACKGROUND_SURFACE;
				break;
			case SUBMARINE:
				this.imageBox = ImageShop.BACKGROUND_SUBMARINE;
				break;
			case ATLANTIS:
				this.imageBox = ImageShop.BACKGROUND_ATLANTIS;
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
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(this.imageBox, 0, 0, null);
		if (this.imageOccupier != null)
		{
			g2d.drawImage(this.imageOccupier, 0, 0, null);
		}
	}

	public void setOccupier(MapElement occupier, Image image)
	{
		this.occupier = occupier;
		this.imageOccupier = image;
		update(getGraphics());
	}

	public int getCoordX()
	{
		return this.x;
	}

	public int getCoordY()
	{
		return this.y;
	}

	public MapType getMapType()
	{
		return this.type;
	}

}
