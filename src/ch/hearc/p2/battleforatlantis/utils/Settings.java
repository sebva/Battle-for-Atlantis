package ch.hearc.p2.battleforatlantis.utils;

import java.awt.Color;

import ch.hearc.p2.battleforatlantis.ui.FrameMain;
import ch.hearc.p2.battleforatlantis.ui.PanelConnection;
import ch.hearc.p2.battleforatlantis.ui.PanelHome;
import ch.hearc.p2.battleforatlantis.ui.PanelPlay;
import ch.hearc.p2.battleforatlantis.ui.PanelPrepare;

public class Settings 
{
	public static final String PATH_RESSOURCES = "res/";
	public static final String PATH_CONFIGURATION = PATH_RESSOURCES + "config/";
	public static final String PATH_IMAGES = PATH_RESSOURCES + "img/";
	public static final String PATH_TEXTS = PATH_RESSOURCES + "strings/";
	public static final String PATH_GAMEITEMS = PATH_IMAGES + "gameitems/";
	public static final String PATH_UI = PATH_IMAGES + "ui/";
	public static final String PATH_BACKGROUNDS = PATH_GAMEITEMS + "backgrounds/";
	public static final String PATH_SHIPS = PATH_GAMEITEMS + "ships/";
	public static final String PATH_SUBMARINES = PATH_GAMEITEMS + "submarines/";

	public static FrameMain FRAME_MAIN;
	
	public static PanelHome PANEL_HOME;
	public static PanelConnection PANEL_CONNECTIONS;
	public static PanelPrepare PANEL_PREPARE;
	public static PanelPlay PANEL_PLAY;
	
	public static Color MENU_BORDER_COLOR = new Color(0, 188, 224);
	
	/**
	 * Activate specific settings 
	 */
	public static void apply()
	{
		// Activating Antialiasing
		System.setProperty("awt.useSystemAAFontSettings","on");
		System.setProperty("swing.aatext", "true");
	}
}
