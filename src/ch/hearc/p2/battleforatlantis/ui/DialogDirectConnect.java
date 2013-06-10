package ch.hearc.p2.battleforatlantis.ui;

import java.awt.Component;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import ch.hearc.p2.battleforatlantis.utils.Messages;

public class DialogDirectConnect
{
	/**
	 * Display an input field to the user prompting him for an IP address or a host name
	 * 
	 * @param parent The JOptionPane's parent
	 * @return An InetAddress object representing an IP address
	 */
	public static InetAddress promptUserForAddress(Component parent)
	{
		InetAddress addr = null;
		while (addr == null)
		{
			String entry = JOptionPane.showInputDialog(parent, Messages.getString("DialogDirectConnect.Message"),
					Messages.getString("DialogDirectConnect.Title"), JOptionPane.QUESTION_MESSAGE);

			// The user clicked on Cancel
			if (entry == null)
				return null;
			try
			{
				addr = InetAddress.getByName(entry);
			}
			catch (UnknownHostException e)
			{
				addr = null;
			}
		}
		return addr;
	}

}
