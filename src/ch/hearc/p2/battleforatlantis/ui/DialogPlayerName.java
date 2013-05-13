package ch.hearc.p2.battleforatlantis.ui;

import java.awt.Component;

import javax.swing.JOptionPane;

import ch.hearc.p2.battleforatlantis.utils.Messages;

public class DialogPlayerName
{
	/**
	 * Prompt the user for a new name
	 * @param parent The parent Component a.k.a the context in which this dialog will appear
	 * @param oldName The name to display in the box
	 * @return The new name on validation, null otherwise
	 */
	public static String promptUserForName(Component parent, String oldName)
	{
		String newName = (String) JOptionPane.showInputDialog(
				parent,
				Messages.getString("DialogPlayerName.Message"),
				Messages.getString("DialogPlayerName.Title"),
				JOptionPane.QUESTION_MESSAGE,
				null, null,
				oldName);
		if(newName == null || "".equals(newName))
			return oldName;
		else
			return newName;
	}

}
