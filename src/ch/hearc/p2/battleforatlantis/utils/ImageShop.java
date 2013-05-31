package ch.hearc.p2.battleforatlantis.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import ch.hearc.p2.battleforatlantis.gameengine.ShipOrientation;
import ch.hearc.p2.battleforatlantis.gameengine.ShipType;

public final class ImageShop
{
	private static final String IMAGES_FOLDER = "img/";
	private static final String GAMEITEMS_FOLDER = IMAGES_FOLDER + "gameitems/";
	private static final String UI_FOLDER = IMAGES_FOLDER + "ui/";
	private static final String BACKGOUNDS_FOLDER = GAMEITEMS_FOLDER + "backgrounds/";
	private static final String SHIPS_FOLDER = GAMEITEMS_FOLDER + "ships/";
	private static final String SUBMARINES_FOLDER = GAMEITEMS_FOLDER + "submarines/";
	private static final String ATLANTIS_FOLDER = GAMEITEMS_FOLDER + "atlantis/";
	private static final String ARROWS_FOLDER = GAMEITEMS_FOLDER + "arrows/";

	private static final String KEYWORD_ATLANTIS = "atlantis";
	private static final String KEYWORD_SHIELD = "bouclier";
	private static final String KEYWORD_GENERATOR = "generateur";
	private static final String KEYWORD_GENERATOR_DESTROYED = "cassé";
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

	public static final Image UI_ARROW_DARK = loadImage(UI_FOLDER + "connect_arrow_dark.png");
	public static final Image UI_ARROW_LIGHT = loadImage(UI_FOLDER + "connect_arrow_light.png");

	public static final Image UI_PLAYERNAME_YES = loadImage(UI_FOLDER + "playername_yes.png");
	public static final Image UI_PLAYERNAME_NO = loadImage(UI_FOLDER + "playername_no.png");
	public static final Image UI_PROGRESS_YES = loadImage(UI_FOLDER + "progress_yes.png");
	public static final Image UI_PROGRESS_NO = loadImage(UI_FOLDER + "progress_no.png");

	public static BufferedImage loadShipImage(ShipType ship, int shipNumber, int partNumber, boolean broken)
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

	public static BufferedImage loadAtlantisImage(int row, int col, boolean shieldActivated)
	{
		StringBuilder filename = new StringBuilder();

		filename.append(ATLANTIS_FOLDER);
		filename.append(KEYWORD_ATLANTIS);
		filename.append(SEPARATOR);
		filename.append(row);
		filename.append(SEPARATOR);
		filename.append(col);
		if (shieldActivated)
		{
			filename.append(SEPARATOR);
			filename.append(KEYWORD_SHIELD);
		}
		filename.append(EXTENSION);

		return loadImage(filename.toString());
	}

	public static BufferedImage loadGeneratorImage(boolean destroyed)
	{
		StringBuilder filename = new StringBuilder();

		filename.append(ATLANTIS_FOLDER);
		filename.append(KEYWORD_GENERATOR);
		if (destroyed)
		{
			filename.append(SEPARATOR);
			filename.append(KEYWORD_GENERATOR_DESTROYED);
		}
		filename.append(EXTENSION);

		return loadImage(filename.toString());
	}

	public static BufferedImage loadImage(String filename)
	{
		try
		{
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(filename);
			return ImageIO.read(ClassLoader.getSystemResource(filename));
		}
		catch (Exception e)
		{
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning("IOException while loading Image at: " + filename + "\nDetails: " + e.getLocalizedMessage());
			return null;
		}
	}

	/**
	 * Rotates a squared BufferedImage clockwise. The algorithm rotates the image in-place.
	 * 
	 * @param image The image that will be rotated
	 */
	public static void inplaceImageRotation(BufferedImage image)
	{
		assert (image.getHeight() == image.getWidth());

		final int N = image.getHeight();
		for (int n = 0; n < N - 1; n++)
		{
			for (int m = n + 1; m < N; m++)
			{
				int temp = image.getRGB(n, m);
				image.setRGB(n, m, image.getRGB(m, n));
				image.setRGB(m, n, temp);
			}
		}
		final int lines = image.getHeight();
		for (int line = 0; line < lines; line++)
		{
			int column1 = 0, column2 = image.getWidth() - 1;
			while (column1 < column2)
			{
				int temp = image.getRGB(column1, line);
				image.setRGB(column1, line, image.getRGB(column2, line));
				image.setRGB(column2, line, temp);
				column1++;
				column2--;
			}
		}
	}

	public static BufferedImage loadPlaceArrow(ShipOrientation imageOrientation)
	{
		StringBuilder filename = new StringBuilder();
		filename.append(ARROWS_FOLDER);
		filename.append(imageOrientation.toString().toLowerCase());
		filename.append(EXTENSION);

		String string = filename.toString();
		return loadImage(string);
	}

}
