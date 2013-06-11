package ch.hearc.p2.battleforatlantis.gameengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.Settings;

public class Atlantis extends MapElement
{
	/**
	 * Dimensions of Atlantis
	 */
	private int width, height;

	/**
	 * Destroyable attribute
	 */
	private boolean destroyable;

	/**
	 * Initial images
	 */
	private BufferedImage[][] images;

	/**
	 * List of box occupied by element
	 */
	private Box[][] occupied;

	/**
	 * Map on which element is placed
	 */
	private Map map;

	/**
	 * Generator of the shield
	 */
	private Generator shieldGenerator = null;

	/**
	 * Position X of Atlantis
	 */
	private int positionX;
	
	/**
	 * Position Y of Atlantis
	 */
	private int positionY;

	/**
	 * Width of map
	 */
	private int mapWidth;
	
	/**
	 * Height of map
	 */
	private int mapHeight;

	/**
	 * Logger
	 */
	private Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * Generate a new Atlantis game element
	 * 
	 * @param width Width of the Atlantis
	 * @param height Height of the Atlantis
	 * @param map Atlantis map
	 * @throws Exception Invalid Size Map
	 */
	public Atlantis(int width, int height, Map map) throws Exception
	{
		super(width * height);

		// Size of Atlantis
		this.width = width;
		this.height = height;

		this.map = map;

		// Size of map
		this.mapWidth = map.getMapWidth();
		this.mapHeight = map.getMapHeight();

		// Some verification for Generator
		if (width >= mapWidth - 2 && height >= mapHeight - 2)
			throw new Exception("Can't create Atlantis with this size on this map. Generator can't be created.");

		// Display settings
		this.preferredSize = new Dimension(width * 60, height * 60);
		setLayout(new GridLayout(height, width, 0, 0));
		this.setPreferredSize(this.preferredSize);
		this.setMinimumSize(this.preferredSize);
		this.setMaximumSize(this.preferredSize);
		this.setBackground(Color.WHITE);

		this.images = new BufferedImage[height][width];
		this.occupied = new Box[height][width];

		// Load images corresponding to boat parts
		for (int row = 0; row < height; row++)
		{
			for (int col = 0; col < width; col++)
			{
				this.images[row][col] = ImageShop.loadAtlantisImage(row, col, true);
			}
		}

		// By default, the Atlantis is not destroyable (shield generator is running)
		this.destroyable = false;
	}

	/**
	 * Generate random position for the Atlantis
	 */
	public void generatePosition()
	{
		// Position of Atlantis
		positionX = ((int) (Math.random() * 1000)) % (mapWidth - width);
		positionY = ((int) (Math.random() * 1000)) % (mapHeight - height);

		occupy();
	}

	/**
	 * Occupy the boxes that must be occupied
	 */
	private void occupy()
	{
		int row = 0;
		for (int y = positionY; y < positionY + height; y++, row++)
		{
			int col = 0;
			for (int x = positionX; x < positionX + width; x++, col++)
			{
				if (images[row][col] != null)
				{
					this.occupied[row][col] = map.getBox(x, y);
					this.occupied[row][col].setOccupier(this, this.images[row][col]);
				}
			}
		}
	}

	/**
	 * Generate a new Atlantis Generator. If already created, returns this one.
	 * 
	 * @return Atlantis Generator
	 */
	public Generator getGenerator()
	{
		// Create new shield generator if necessary
		if (this.shieldGenerator == null)
		{
			int generatorPositionX = 0;
			int generatorPositionY = 0;

			boolean isPlaced = false;

			int direction = (int) (Math.random() * 4.0);

			while (isPlaced == false)
			{
				// Choose position
				switch (direction)
				{
					case 0:
						// If we've some place for the top
						if (this.positionY >= 2)
						{
							generatorPositionX = this.positionX + 1 + (int) (Math.random() * (this.width - 2));
							generatorPositionY = this.positionY - 2;
							isPlaced = true;
						}
						break;
					case 1:
						// If we've some place for the left
						if (positionX >= 2)
						{
							generatorPositionX = this.positionX - 2;
							generatorPositionY = this.positionY + 1 + (int) (Math.random() * (this.height - 2));
							isPlaced = true;
						}
						break;
					case 2:
						// If we've some place for the right
						if ((this.positionX + this.width + 1) < this.mapWidth)
						{
							generatorPositionX = this.positionX + this.width + 1;
							generatorPositionY = this.positionY + 1 + (int) (Math.random() * (this.height - 2));
							isPlaced = true;
						}
						break;
					case 3:
						// If we've some place for the bottom
						if ((this.positionY + this.height + 1) < this.mapHeight)
						{
							generatorPositionX = this.positionX + 1 + (int) (Math.random() * (this.width - 2));
							generatorPositionY = this.positionY + this.height + 1;
							isPlaced = true;
						}
						break;
				}
				direction++;
				if (direction > 3)
				{
					direction = 0;
				}
			}

			this.shieldGenerator = new Generator(map.getBox(generatorPositionX, generatorPositionY), this);
		}

		return this.shieldGenerator;
	}

	/**
	 * Set the generator associed with this city
	 * @param gen Generator to assocy
	 */
	public void setGenerator(Generator gen)
	{
		this.shieldGenerator = gen;
	}

	/**
	 * Indicate that the generator is destroyed, the Atlantis is destroyable !
	 */
	public void generatorDestroyed()
	{
		this.destroyable = true;

		for (int row = 0; row < height; row++)
		{
			for (int col = 0; col < width; col++)
			{
				this.images[row][col] = ImageShop.loadAtlantisImage(row, col, false);
				if (images[row][col] != null && occupied[row][col] != null)
					this.occupied[row][col].setOccupier(this, this.images[row][col]);
			}
		}
	}

	/**
	 * Indicate if Atlantis is destroyable
	 * 
	 * @return True when the Atlantis is destroyable, false otherwise
	 */
	public boolean isDestroyable()
	{
		return this.destroyable;
	}

	/**
	 * Get the X Position of the Atlantis (start at 0)
	 * 
	 * @return Position X
	 */
	public int getPositionX()
	{
		return this.positionX;
	}

	/**
	 * Get the Y Position of the Atlantis (start at 0)
	 * 
	 * @return Position Y
	 */
	public int getPositionY()
	{
		return this.positionY;
	}

	/**
	 * Set the X and Y position of the Atlantis. Used when Atlantis is retrieved by other player
	 * 
	 * @param positionX Position in X
	 * @param positionY Position in Y
	 */
	public void setPosition(int positionX, int positionY)
	{
		this.positionX = positionX;
		this.positionY = positionY;

		occupy();
	}

	@Override
	protected void setCurrentSize(int width, int height)
	{
		// nothing
	}

	@Override
	public void shoot(Box target)
	{
		log.info("Shoot on atlantis");
		if (destroyable && Settings.PANEL_PLAY.isLocalPlayerPlaying())
			Settings.PANEL_PLAY.endGame(true, false);
		else
		{
			boolean fatal = true;
			outer: for (Box[] row : occupied)
			{
				for (Box box : row)
					if (box != null && !target.equals(box) && !box.isDiscovered())
					{
						fatal = false;
						break outer;
					}
			}

			if (fatal && Settings.PANEL_PLAY.isLocalPlayerPlaying())
				Settings.PANEL_PLAY.endGame(false, false);
		}
	}

}
