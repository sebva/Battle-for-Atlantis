package ch.hearc.p2.battleforatlantis;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import ch.hearc.p2.battleforatlantis.ui.FrameMain;

public class Main
{

	/**
	 * Point d'entrée de l'application
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Configuration du Logger
		Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		log.setLevel(Level.FINEST);
		log.setFilter(new Filter()
		{
			@Override
			public boolean isLoggable(LogRecord _arg0)
			{
				// Aucun filtrage
				return true;
			}
		});

		new FrameMain();
	}

}
