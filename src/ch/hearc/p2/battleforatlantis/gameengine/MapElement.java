package ch.hearc.p2.battleforatlantis.gameengine;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JPanel;

public abstract class MapElement extends JPanel
{

	/**
	 * Image list for displaying element
	 */
	protected Image[] images;
	
	/**
	 * Box considered as center of element
	 */
	protected Box center = null;
	
	/**
	 * List of boxes occupied by element
	 */
	protected Box[] occupied;
	
	/**
	 * Total amount of boxes occupied by element
	 */
	protected final int wholeSize;
	
	/**
	 * Amount of boxes occupied by element that have been shot by ennemy
	 */
	protected int touchedSize;
	
	/**
	 * Size in pixels for displaying element
	 */
	protected Dimension displaySize;

	/**
	 * Default constructor for element
	 * 
	 * @param wholeSize Total amount of boxes occupied by element
	 */
	public MapElement(int wholeSize)
	{
		// Input fields
		this.wholeSize = wholeSize;
		this.touchedSize = 0;
		this.images = new Image[wholeSize];
		this.occupied = new Box[wholeSize];
	}

	/**
	 * External call for shoot on element
	 */
	public void shoot()
	{

	}

}
