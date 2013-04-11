package ch.hearc.p2.battleforatlantis;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import ch.hearc.p2.battleforatlantis.ui.FrameMain;

public class Main
{

	/**
	 * Unique entry point of the application
	 * 
	 * @param args Ignored
	 */
	public static void main(String[] args)
	{
		// Logger configuration
		Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		log.setLevel(Level.FINEST);
		log.setFilter(new Filter()
		{
			@Override
			public boolean isLoggable(LogRecord _arg0)
			{
				// No filtering applied
				return true;
			}
		});

		FrameMain frameMain = new FrameMain();
		
		// Use the system look and feel
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		SwingUtilities.updateComponentTreeUI(frameMain);
	}

}
