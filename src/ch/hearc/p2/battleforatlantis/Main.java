package ch.hearc.p2.battleforatlantis;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import ch.hearc.p2.battleforatlantis.ui.FrameMain;

public class Main
{

	/**
	 * Unique entry point of the application
	 * 
	 * @param args
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

		new FrameMain();
	}

}
