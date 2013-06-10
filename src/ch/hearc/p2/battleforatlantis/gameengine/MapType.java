package ch.hearc.p2.battleforatlantis.gameengine;

import java.awt.Image;

import ch.hearc.p2.battleforatlantis.utils.ImageShop;

public enum MapType
{
	SURFACE, SUBMARINE, ATLANTIS;

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
