package ch.hearc.p2.battleforatlantis.gameengine;

import ch.hearc.p2.battleforatlantis.utils.ImageShop;

public class Generator extends MapElement
{
	private String image;
	int positionX;
	int positionY;
	
	public Generator(int positionX, int positionY)
	{
		super(1);
		
		this.positionX = positionX;
		this.positionY = positionY;
		
		this.images[0] = ImageShop.loadGeneratorImage(false);
	}

	public int getPositionX()
	{
		return this.positionX;
	}
	
	public int getPositionY()
	{
		return this.positionY;
	}
}
