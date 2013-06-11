package ch.hearc.p2.battleforatlantis.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Auto-generated class providing i18n
 */
public class Messages
{
	/** Name of bundle containing the messages */
	private static final String BUNDLE_NAME = "strings.messages";

	/** Bunle ready to be read */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/** Private constructor */
	private Messages()
	{
	}

	/**
	 * Get a string from the messages file
	 * 
	 * @param key Key to gather from file
	 * @return String to display on UI
	 */
	public static String getString(String key)
	{
		try
		{
			return RESOURCE_BUNDLE.getString(key);
		}
		catch (MissingResourceException e)
		{
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Message not found: " + key);
			return '!' + key + '!';
		}
	}
}
