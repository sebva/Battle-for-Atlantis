package ch.hearc.p2.battleforatlantis.gameengine;

import java.util.logging.Logger;

import ch.hearc.p2.battleforatlantis.utils.ImageShop;

public class Generator extends MapElement
{
	// Position of Generator
	private int positionX;
	private int positionY;
	private Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	// Size of Generator
	private static int sizeX = 1;
	private static int sizeY = 1;
	
	private Box box;
	private Atlantis atlantis;
	
	// Distance between Atlantis and Generator
	private static int distanceX = 2;
	private static int distanceY = 2;
	
	/**
	 * Construct a new Generator
	 * 
	 */
	public Generator(Box box, Atlantis atlantis)
	{
		super(sizeX * sizeY);
		
		this.box = box;
		this.atlantis = atlantis;
		
		// Set Position
		this.positionX = box.getCoordX();
		this.positionY = box.getCoordY();
		
		// Set Image
		this.images[0] = ImageShop.loadGeneratorImage(false);
		box.setOccupier(this, images[0]);
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

	@Override
	protected void setCurrentSize(int width, int height)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void shoot(Box target)
	{
		log.info("Shoot on generator");
		box.setOccupier(this, ImageShop.loadGeneratorImage(true));
		atlantis.generatorDestroyed();
	}
}
