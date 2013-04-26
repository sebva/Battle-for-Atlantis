package ch.hearc.p2.battleforatlantis.gameengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Icon;

import ch.hearc.p2.battleforatlantis.utils.ImageShop;

public class Atlantis extends MapElement
{
	// Dimensions of Atlantis
	private int width, height;
	
	// Destroyable attribute
	private boolean destroyable;
	
	// Initial images
	private Icon[] initialImages;
	
	// Generator of the shield
	private Generator shieldGenerator = null;
	
	// Positions of Atlantis
	private int positionX;
	private int positionY;
	
	// Dimensions of the Map
	private int mapWidth;
	private int mapHeight;

	/**
	 * Generate a new Atlantis game element
	 * 
	 * @param width Width of the Atlantis
	 * @param height Height of the Atlantis
	 * @param mapWidth Map width
	 * @param mapHeight Map height
	 * @throws Exception Invalid Size Map
	 */
	public Atlantis(int width, int height, int mapWidth, int mapHeight) throws Exception
	{
		super(width * height);
		
		// Size of Atlantis
		this.width = width;
		this.height = height;
		
		// Position of Atlantis
		positionX = (int)(Math.random() * (mapWidth - this.width));
		positionY = (int)(Math.random() * (mapHeight - this.height));
		
		// Size of map
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		
		// Some verification for Generator
		if (width < mapWidth - 2 && height < mapHeight - 2)
			throw new Exception("Can't create Atlantis with this size on this map. Generator can't be created.");
		
		// Display settings
		this.displaySize = new Dimension(width * 60, height * 60);
		setLayout(new GridLayout(height, width, 0, 0));
		this.setPreferredSize(this.displaySize);
		this.setMinimumSize(this.displaySize);
		this.setMaximumSize(this.displaySize);
		this.setBackground(Color.WHITE);

		// Load images corresponding to boat parts
		for (int row = 0; row < width; row++)
		{
			for (int col = 0; col < height; col++)
			{
				this.images[(row * width) + col] = ImageShop.loadAtlantisImage(row, col, true);
				
				// FIXME: There is a bug in one of the two following lines
				// this.initialImages[(row * width) + col] = new ImageIcon(this.images[(row * width) + col]);
				// add(new JLabel(this.initialImages[(row * width) + col]));
			}
		}
		
		// By default, the Atlantis is not destroyable (shield generator is running)
		this.destroyable = false;
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
			boolean isPlaced = false;
			
			while (isPlaced == false)
			{
				// Choose position
				switch ((int)(Math.random() * 3))
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
			
			this.shieldGenerator = new Generator(generatorPositionX, generatorPositionY);
		}
		
		return this.shieldGenerator;
	}

	/**
	 * Indicate that the generator is destroyed, the Atlantis is destroyable !
	 */
	public void generatorDestroyed()
	{
		this.destroyable = true;
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

}
