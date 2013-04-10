package ch.hearc.p2.battleforatlantis.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Auto-generated class providing i18n
 * 
 * @author Sébastien Vaucher
 * 
 */
public class Messages
{
	private static final String BUNDLE_NAME = "strings.messages";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private Messages()
	{
	}

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
