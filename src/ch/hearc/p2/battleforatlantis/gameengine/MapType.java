package ch.hearc.p2.battleforatlantis.gameengine;

import java.awt.Image;

import ch.hearc.p2.battleforatlantis.utils.ImageShop;

/**
 * Enumeration of map types available in game
 */
public enum MapType
{
	SURFACE, SUBMARINE, ATLANTIS;

	/**
	 * Get the background of box corresponding to the given map type
	 * 
	 * @return Image to put as box background
	 */
	public Image getBackground()
	{
		switch (name())
		{
			default:
			case "SURFACE":
				return ImageShop.BACKGROUND_SURFACE;
			case "SUBMARINE":
				return ImageShop.BACKGROUND_SUBMARINE;
			case "ATLANTIS":
				return ImageShop.BACKGROUND_ATLANTIS;
		}
	}
}
