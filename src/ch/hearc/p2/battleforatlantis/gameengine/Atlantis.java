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
	// Dimensions of Atlantis
	private int width, height;
	
	// Destroyable attribute
	private boolean destroyable;
	
	// Initial images
	private BufferedImage[][] images;
	
	private Box[][] occupied;
	
	private Map map;
	
	// Generator of the shield
	private Generator shieldGenerator = null;
	
	// Positions of Atlantis
	private int positionX;
	private int positionY;
	
	// Dimensions of the Map
	private int mapWidth;
	private int mapHeight;

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
		if (width < mapWidth - 2 && height < mapHeight - 2)
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
				
				// FIXME: There is a bug in one of the two following lines
				// this.initialImages[(row * width) + col] = new ImageIcon(this.images[(row * width) + col]);
				// add(new JLabel(this.initialImages[(row * width) + col]));
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
		positionX = (int)(Math.random() * (mapWidth - this.width));
		positionY = (int)(Math.random() * (mapHeight - this.height));
		
		occupy();
	}

	private void occupy()
	{
		int row = 0;
		for(int y = positionY; y < positionY + height; y++, row++)
		{
			int col = 0;
			for(int x = positionX; x < positionX + width; x++, col++)
			{
				this.occupied[row][col] = map.getBox(x, y);
				if(images[row][col] != null)
					this.occupied[row][col].setOccupier(this, this.images[row][col]);
			}
		}
	}
	
	/**
	 * Generate a new Atlantis Generator.
	 * If already created, returns this one.
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
			
			/* FIXME: The switch doesn't cover all the possibilities, and so
			 * an infinite loop may occur, thus blocking the TCP reception thread.
			 * When fixed, remember to set isPlaced to false again ! 
			 */
			boolean isPlaced = true;
			
			while (isPlaced == false)
			{
				// Choose position
				switch ((int)(Math.random() * 4.0)) // *4.0 because (int) floors the result
				{
					case 0:
						// If we've some place for the top
						if (this.positionY >= 2)
						{
							generatorPositionX = this.positionX + (int)(Math.random() * (this.width - 2 - 1));
							generatorPositionY = this.positionY - 2;
							isPlaced = true;
						}
						break;
					case 1:
						// If we've some place for the left
						if (positionX >= 2)
						{
							generatorPositionX = this.positionX - 2;
							generatorPositionY = this.positionY + (int)(Math.random() * (this.height - 2 - 1));
							isPlaced = true;
						}
						break;
					case 2:
						// If we've some place for the right
						if ((this.positionX + this.width) <= this.mapWidth - 2)
						{
							generatorPositionX = this.positionX + this.width + 2;
							generatorPositionY = this.positionY + (int)(Math.random() * (this.height - 2 - 1));
							isPlaced = true;
						}
						break;
					case 3:
						// If we've some place for the bottom
						if ((this.positionY + this.height) <= this.mapHeight - 2)
						{
							generatorPositionX = this.positionX + (int)(Math.random() * (this.width - 2 - 1));
							generatorPositionY = this.positionY + this.height + 2;
							isPlaced = true;
						}
						break;
				}
			}
			
			this.shieldGenerator = new Generator(map.getBox(generatorPositionX, generatorPositionY), this);
		}
		
		return this.shieldGenerator;
	}
	
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
		
		for(int row = 0; row < height; row++)
		{
			for(int col = 0; col < width; col++)
			{
				this.images[row][col] = ImageShop.loadAtlantisImage(row, col, false);
				if(images[row][col] != null && occupied[row][col] != null)
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
	 * Set the X and Y position of the Atlantis.
	 * Used when Atlantis is retrieved by other player
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shoot(Box target)
	{
		log.info("Shoot on atlantis");
		if(destroyable)
			Settings.PANEL_PLAY.endGame(true, false);
		else
			target.setImage(ImageShop.loadAtlantisImage(target.getCoordY() - positionY, target.getCoordX() - positionX, false));
	}

}
