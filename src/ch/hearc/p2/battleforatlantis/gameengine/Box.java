package ch.hearc.p2.battleforatlantis.gameengine;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

import ch.hearc.p2.battleforatlantis.utils.ImageShop;

public class Box extends JPanel
{
	/**
	 * Discover marker, true when box has been shot by ennemy
	 */
	private boolean discovered = false;
	
	/**
	 * Image for displaying box content (water)
	 */
	private Image imageBox;
	
	/**
	 * Image for displaying correct part of boat in box
	 */
	private Image imageOccupier = null;
	
	/**
	 * Map to which this box belong
	 */
	private Map map;
	
	/**
	 * Occupier of this box, or null if not
	 */
	private MapElement occupier;
	
	/**
	 * Type of box (surface, submarine, atlantis)
	 */
	private MapType type;
	
	/**
	 * Dimension of box
	 */
	private Dimension size;
	
	/**
	 * Horizontal coordinate of box in map (left-side is 0)
	 */
	private int x;
	
	/**
	 * Vertical coordinate of box in map (upper-side is 0)
	 */
	private int y;

	/**
	 * Default constructor for box-in-map instanciation
	 * 
	 * @param map Map to which current box belongs
	 * @param type Type of box to display (surface, submarine, atlantis)
	 * @param x Horizontal coordinate of box in map (left-side is 0)
	 * @param y Vertical coordinate of box in map (upper-side is 0)
	 */
	public Box(Map map, MapType type, int x, int y)
	{
		// Input fields
		this.type = type;
		this.map = map;
		this.x = x;
		this.y = y;

		// Get water image depending on map type
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

		// Set size of box to display on screen
		this.size = new Dimension(60, 60);
		setPreferredSize(this.size);
		setMinimumSize(this.size);
		setMaximumSize(this.size);
	}

	/**
	 * External call method for ennemy's shoot on this box
	 */
	public void shoot()
	{

	}

	/**
	 * Set the current occupier of the box
	 * 
	 * @param occupier Element that really occupied the box
	 * @param image Correct image to display in the box to represent part of occupier
	 */
	public void setOccupier(MapElement occupier, Image image)
	{
		this.occupier = occupier;
		this.imageOccupier = image;
		update(getGraphics());
	}

	/**
	 * Getter for horizontal coordinate
	 * 
	 * @return Horizontal coordinate (left-side is 0)
	 */
	public int getCoordX()
	{
		return this.x;
	}
	
	/**
	 * Getter for vertical coordinate
	 * 
	 * @return Vertical coordinate (top-side is 0)
	 */
	public int getCoordY()
	{
		return this.y;
	}

	/**
	 * Getter for current box type
	 * 
	 * @return Type of box displayed (surface, submarine, atlantis)
	 */
	public MapType getMapType()
	{
		return this.type;
	}

	/**
	 * Paint method for displaying box and part of element eventually contained in it
	 */
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

}
