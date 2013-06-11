package ch.hearc.p2.battleforatlantis.gameengine;

import java.util.logging.Logger;

import ch.hearc.p2.battleforatlantis.utils.ImageShop;

/**
 * Generator representation for placing, managing and displaying itself
 */
public class Generator extends MapElement
{
	/** Position X of generator */
	private int positionX;

	/** Position Y of generator */
	private int positionY;

	/** Logger */
	private Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/** Size of generator (X) */
	private static int sizeX = 1;

	/** Size of generator (Y) */
	private static int sizeY = 1;

	/** Box on which generator is placed */
	private Box box;

	/** Atlantis that this generator is protecting */
	private Atlantis atlantis;

	/** Distance in X between Atlantis and generator */
	private static int distanceX = 2;

	/** Distance in Y between Atlantis and generator */
	private static int distanceY = 2;

	/**
	 * Construct a new Generator
	 * 
	 * @param box Box in which generator will be placed
	 * @param atlantis Lost city that this lost generator will protect until it is destroyed by a lost submarine
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
		// nothing
	}

	@Override
	public void shoot(Box target)
	{
		log.info("Shoot on generator");
		box.setOccupier(this, ImageShop.loadGeneratorImage(true));
		atlantis.generatorDestroyed();
	}
}
