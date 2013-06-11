package ch.hearc.p2.battleforatlantis.ui;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import ch.hearc.p2.battleforatlantis.utils.Messages;

/**
 * Dialog for wait time during ships placement
 */
public class DialogWait extends JDialog
{
	/**
	 * Display the wait dialog to user
	 * 
	 * @param waitMessage Message to display
	 * @param parent Parent frame to assocy with dialog
	 */
	public DialogWait(String waitMessage, Frame parent)
	{
		super(parent, Messages.getString("DialogWait.PleaseWait"), true);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		setSize(350, 200);

		setLayout(new BorderLayout());
		add(new JLabel(waitMessage, SwingConstants.CENTER), BorderLayout.CENTER);
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		add(progressBar, BorderLayout.SOUTH);
	}
}
