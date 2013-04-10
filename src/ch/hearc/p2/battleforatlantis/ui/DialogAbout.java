package ch.hearc.p2.battleforatlantis.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import sun.security.krb5.internal.Ticket;

import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.ImageShop.ShipType;
import ch.hearc.p2.battleforatlantis.utils.Messages;

public class DialogAbout extends JDialog
{
	private static final int kHgap = 30;
	private static final int kVgap = 15;

	public DialogAbout(Frame parent)
	{
		super(parent, true);
		setTitle(Messages.getString("DialogAbout.Title"));
		setSize(500, 350);
		setResizable(false);
		setLayout(new BorderLayout(kHgap, kVgap));
		
		JLabel img = new JLabel(new ImageIcon(ImageShop.loadShipImage(ShipType.SHIP, 2, 1, false)));
		JLabel text = new JLabel(Messages.getString("DialogAbout.Text"));
		JButton btn = new JButton(Messages.getString("DialogAbout.ButtonText"));
		
		btn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				DialogAbout.this.dispose();
			}
		});
		
		add(img, BorderLayout.WEST);
		add(text, BorderLayout.CENTER);
		add(btn, BorderLayout.SOUTH);
		
		setVisible(true);
	}

}
