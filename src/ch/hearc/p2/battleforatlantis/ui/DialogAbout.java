package ch.hearc.p2.battleforatlantis.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.Messages;

public class DialogAbout extends JDialog
{
	private static final int kHgap = 30;
	private static final int kVgap = 15;

	/**
	 * Create and display the about dialog The dialog is modal
	 * 
	 * @param parent The JDialog's parent
	 */
	public DialogAbout(FrameMain parent)
	{
		super(parent, true);
		setTitle(Messages.getString("DialogAbout.Title"));
		setSize(530, 310);
		setResizable(false);
		setLayout(new BorderLayout(kHgap, kVgap));

		JLabel hearc = new JLabel(new ImageIcon(ImageShop.UI_HE_ARC));
		JLabel img = new JLabel(new ImageIcon(ImageShop.UI_LOGO));
		JLabel text = new JLabel(Messages.getString("DialogAbout.Text"));
		JButton btn = new JButton(Messages.getString("DialogAbout.ButtonText"));

		btn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});

		add(hearc, BorderLayout.NORTH);
		add(img, BorderLayout.WEST);
		add(text, BorderLayout.CENTER);
		add(btn, BorderLayout.SOUTH);

		setVisible(true);
	}

}
