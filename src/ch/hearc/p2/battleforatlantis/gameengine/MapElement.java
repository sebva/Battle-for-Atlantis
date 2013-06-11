package ch.hearc.p2.battleforatlantis.gameengine;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

/**
 * Abstract class representing a map element which can be placed on a map
 */
public abstract class MapElement extends JLabel
{
	/** Image list for displaying element */
	protected BufferedImage[] images;

	/** Box considered as center of element */
	protected Box center = null;

	/** List of boxes occupied by element */
	protected Box[] occupied;

	/** Total amount of boxes occupied by element */
	protected final int wholeSize;

	/** Amount of boxes occupied by element that have been shot by ennemy */
	protected int touchedSize;

	/** Size in pixels for displaying element */
	protected Dimension preferredSize;

	/** Minimal size in pixels for displaying element */
	protected Dimension minimumSize;

	/** Current size in pixels for element */
	protected Dimension currentSize;

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
		this.images = new BufferedImage[wholeSize];
		this.occupied = new Box[wholeSize];
	}

	/**
	 * External call for shoot on element
	 */
	abstract public void shoot(Box target);

	/**
	 * Set the initial size of element
	 */
	protected void setInitialSize(int width, int height)
	{
		this.preferredSize = new Dimension(width, height);
		this.minimumSize = new Dimension(width / 2, height / 2);
		this.currentSize = new Dimension(width, height);
		this.setPreferredSize(this.preferredSize);
		this.setMaximumSize(this.preferredSize);
		this.setMinimumSize(this.minimumSize);
	}

	/**
	 * Set the size of boat when resizing
	 */
	protected abstract void setCurrentSize(int width, int height);

	/**
	 * Get the size of the element
	 * 
	 * @return Size of the element
	 */
	public int getWholeSize()
	{
		return this.wholeSize;
	}

	/**
	 * Check if the element is touched
	 * 
	 * @return True if the element is touched
	 */
	public boolean isTouched()
	{
		return touchedSize > 0;
	}

	/**
	 * Get the number of box to shoot to achieve the element
	 * 
	 * @return Number of box
	 */
	public int getRemainingSize()
	{
		return this.wholeSize - this.touchedSize;
	}

	/**
	 * Get the boxes occupied by the map element
	 * 
	 * @return List of boxes
	 */
	public Box[] getOccupied()
	{
		return occupied;
	}

}
