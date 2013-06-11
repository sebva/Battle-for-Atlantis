package ch.hearc.p2.battleforatlantis.utils;

import java.awt.Color;

import ch.hearc.p2.battleforatlantis.ui.FrameMain;
import ch.hearc.p2.battleforatlantis.ui.PanelConnection;
import ch.hearc.p2.battleforatlantis.ui.PanelHome;
import ch.hearc.p2.battleforatlantis.ui.PanelPlay;
import ch.hearc.p2.battleforatlantis.ui.PanelPrepare;

/**
 * Static holder for running application settings
 */
public class Settings
{
	// Basic paths
	public static final String PATH_RESSOURCES = "res/";
	public static final String PATH_CONFIGURATION = PATH_RESSOURCES + "config/";
	public static final String PATH_IMAGES = PATH_RESSOURCES + "img/";
	public static final String PATH_TEXTS = PATH_RESSOURCES + "strings/";
	public static final String PATH_GAMEITEMS = PATH_IMAGES + "gameitems/";
	public static final String PATH_UI = PATH_IMAGES + "ui/";
	public static final String PATH_BACKGROUNDS = PATH_GAMEITEMS + "backgrounds/";
	public static final String PATH_SHIPS = PATH_GAMEITEMS + "ships/";
	public static final String PATH_SUBMARINES = PATH_GAMEITEMS + "submarines/";

	/** Main frame */
	public static FrameMain FRAME_MAIN;

	/** Panel for home menu */
	public static PanelHome PANEL_HOME;

	/** Panel for connections */
	public static PanelConnection PANEL_CONNECTIONS;

	/** Panel for ships placement */
	public static PanelPrepare PANEL_PREPARE;

	/** Panel for playing game */
	public static PanelPlay PANEL_PLAY;

	/** Width of Atlantis */
	public static int ATLANTIS_WIDTH;

	/** Height of Atlantis */
	public static int ATLANTIS_HEIGHT;

	/** Color of menu border */
	public static Color MENU_BORDER_COLOR = new Color(0, 188, 224);

	/**
	 * Activate specific settings
	 */
	public static void apply()
	{
		// Activating Antialiasing
		System.setProperty("awt.useSystemAAFontSettings", "on");
		System.setProperty("swing.aatext", "true");
	}
}
