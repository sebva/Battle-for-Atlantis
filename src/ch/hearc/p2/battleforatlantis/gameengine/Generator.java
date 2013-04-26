package ch.hearc.p2.battleforatlantis.gameengine;

import ch.hearc.p2.battleforatlantis.utils.ImageShop;

public class Generator extends MapElement
{
	// Position of Generator
	private int positionX;
	private int positionY;
	
	// Size of Generator
	private static int sizeX = 1;
	private static int sizeY = 1;
	
	// Distance between Atlantis and Generator
	private static int distanceX = 2;
	private static int distanceY = 2;
	
	/**
	 * Construct a new Generator
	 * 
	 * @param positionX Position X of the generator (start at 0)
	 * @param positionY Position Y of the generator (start at 0)
	 */
	public Generator(int positionX, int positionY)
	{
		super(sizeX * sizeY);
		
		// Set Position
		this.positionX = positionX;
		this.positionY = positionY;
		
		// Set Image
		this.images[0] = ImageShop.loadGeneratorImage(false);
	}

	/**
	 * Get the Y Position of the generator (start at 0)
	 * 
	 * @return Position X
	 */
	public int getPositionX()
	{
		return this.positionX;
	}
	
	/**
	 * Get the Y position of the generator (start at 0)
	 * 
	 * @return Position Y
	 */
	public int getPositionY()
	{
		return this.positionY;
	}
	
	/**
	 * Get the X size
	 * 
	 * @return Size in X
	 */
	public int getSizeX()
	{
		return sizeX;
	}
	
	/**
	 * Get the Y size
	 * 
	 * @return Size in Y
	 */
	public int getSizeY()
	{
		return sizeY;
	}
	
	/**
	 * Get the distance in X between Atlantis and Generator 
	 * 
	 * @return Distance in X
	 */
	public int getDistanceX()
	{
		return distanceX;
	}
	
	/**
	 * Get the distance in Y between Atlantis and Generator
	 * 
	 * @return Distance in Y
	 */
	public int getDistanceY()
	{
		return distanceY;
	}
}
