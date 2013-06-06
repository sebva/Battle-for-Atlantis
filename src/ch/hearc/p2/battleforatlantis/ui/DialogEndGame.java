package ch.hearc.p2.battleforatlantis.ui;

import java.awt.Component;

import javax.swing.JOptionPane;

import ch.hearc.p2.battleforatlantis.sound.SoundManager;
import ch.hearc.p2.battleforatlantis.utils.Messages;

public class DialogEndGame
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
			SoundManager.getInstance().playVoice(SoundManager.Voice.VICTORY);
		}
		else
		{
			title = Messages.getString("DialogEndGame.DefeatTitle");
			message = Messages.getString("DialogEndGame.DefeatMessage");
			SoundManager.getInstance().playVoice(SoundManager.Voice.DEFEAT);
		}

		JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

}
