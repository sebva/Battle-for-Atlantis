package ch.hearc.p2.battleforatlantis.utils;

import java.awt.Image;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import ch.hearc.p2.battleforatlantis.gameengine.ShipType;

public final class ImageShop
{
	private static final String IMAGES_FOLDER = "img/";
	private static final String GAMEITEMS_FOLDER = IMAGES_FOLDER + "gameitems/";
	private static final String UI_FOLDER = IMAGES_FOLDER + "ui/";
	private static final String BACKGOUNDS_FOLDER = GAMEITEMS_FOLDER + "backgrounds/";
	private static final String SHIPS_FOLDER = GAMEITEMS_FOLDER + "ships/";
	private static final String SUBMARINES_FOLDER = GAMEITEMS_FOLDER + "submarines/";

	private static final String KEYWORD_SHIP = "bateau";
	private static final String KEYWORD_SUBMARINE = "sousmarin";
	private static final String KEYWORD_BROKEN = "cassé";
	private static final String KEYWORD_INTACT = "entier";

	private static final String EXTENSION = ".png";
	private static final String SEPARATOR = "_";

	public static final Image BACKGROUND_ATLANTIS = loadImage(BACKGOUNDS_FOLDER + "case_fond_atlantide.png");
	public static final Image BACKGROUND_SUBMARINE = loadImage(BACKGOUNDS_FOLDER + "case_fond_sousmarin.png");
	public static final Image BACKGROUND_SURFACE = loadImage(BACKGOUNDS_FOLDER + "case_fond_surface.png");

	public static final Image UI_BUTTON = loadImage(UI_FOLDER + "bouton.png");
	public static final Image UI_MENU_BACKGROUND = loadImage(UI_FOLDER + "image_menu.png");
	public static final Image UI_LOGO = loadImage(UI_FOLDER + "logo.png");

	public static Image loadShipImage(ShipType ship, int shipNumber, int partNumber, boolean broken)
	{
		StringBuilder filename = new StringBuilder();
		switch (ship)
		{
			case SHIP:
				filename.append(SHIPS_FOLDER);
				filename.append(KEYWORD_SHIP);
				break;
			case SUBMARINE:
				filename.append(SUBMARINES_FOLDER);
				filename.append(KEYWORD_SUBMARINE);
				break;
		}

		filename.append(SEPARATOR);
		filename.append(shipNumber);
		filename.append(SEPARATOR);
		filename.append(partNumber);
		filename.append(SEPARATOR);
		filename.append(broken ? KEYWORD_BROKEN : KEYWORD_INTACT);
		filename.append(EXTENSION);

		return loadImage(filename.toString());
	}

	public static Image loadImage(String filename)
	{
		try
		{
			return ImageIO.read(ClassLoader.getSystemResource(filename));
		}
		catch (IOException e)
		{
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning("IOException while loading Image at: " + filename + "\nDetails: " + e.getLocalizedMessage());
			return null;
		}
	}
}
