package ch.hearc.p2.battleforatlantis.ui;

import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import ch.hearc.p2.battleforatlantis.utils.Messages;

public class DialogEndGame extends JDialog
{
	/**
	 * Announce the game result to the user
	 * @param parent The JOptionPane's parent
	 * @param isWinner true if the local player won the game
	 */
	public static void announceGameResult(Component parent, boolean isWinner)
	{
		String title, message;
		if (isWinner)
		{
			title = Messages.getString("DialogEndGame.VictoryTitle");
			message = Messages.getString("DialogEndGame.VictoryMessage");
		}
		else
		{
			title = Messages.getString("DialogEndGame.DefeatTitle");
			message = Messages.getString("DialogEndGame.DefeatMessage");
		}

		JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

}