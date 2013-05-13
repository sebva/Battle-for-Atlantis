package ch.hearc.p2.battleforatlantis;

import java.util.Properties;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.UIManager;

import ch.hearc.p2.battleforatlantis.ui.FrameMain;
import ch.hearc.p2.battleforatlantis.utils.Settings;

import com.jtattoo.plaf.noire.NoireLookAndFeel;

public class Main
{

	/**
	 * Unique entry point of the application
	 * 
	 * @param args
	 *            Ignored
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		// Logger configuration
		Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		log.setLevel(Level.FINEST);
		log.setFilter(new Filter()
		{
			@Override
			public boolean isLoggable(LogRecord record)
			{
				// No filtering applied
				return true;
			}
		});
		
		Settings.apply();
		setLookAndFeel();
		new FrameMain();
	}

	private static void setLookAndFeel()
	{
		Properties props = new Properties();

		String blueString = Settings.MENU_BORDER_COLOR.getRed() + " " 
				+ Settings.MENU_BORDER_COLOR.getGreen() + " "
				+ Settings.MENU_BORDER_COLOR.getBlue();
		props.put("backgroundColor", "0 0 0");
		props.put("backgroundColorLight", "0 0 0");
		props.put("backgroundColorDark", "0 0 0");
		props.put("focusCellColor", blueString);
		props.put("windowIconRolloverColor", blueString);
		props.put("menuSelectionBackgroundColor", blueString);
		props.put("rolloverColor", blueString);
		props.put("rolloverColorLight", blueString);
		props.put("rolloverColorDark", blueString);
		props.put("selectionForegroundColor", blueString);
		props.put("logoString", "Battle for Atlantis");

		NoireLookAndFeel.setCurrentTheme(props);

		try
		{
			UIManager.setLookAndFeel(NoireLookAndFeel.class.getName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
