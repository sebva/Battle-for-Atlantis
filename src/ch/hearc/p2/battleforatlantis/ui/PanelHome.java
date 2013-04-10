package ch.hearc.p2.battleforatlantis.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.Messages;

public class PanelHome extends JPanel
{
	private static final int kHgap = 30;
	private static final int kVgap = 0;
	
	private FrameMain rootFrame;
	
	private class PanelMenu extends JPanel
	{
		private JButton btnSearchForPlayer;
		private JButton btnSettings;
		private JButton btnAbout;

		public PanelMenu()
		{
			Box box = Box.createVerticalBox();
			
			btnSearchForPlayer = new JButton(Messages.getString("PanelHome.SearchForPlayer"), new ImageIcon(ImageShop.UI_BUTTON));
			btnSettings = new JButton(Messages.getString("PanelHome.Settings"), new ImageIcon(ImageShop.UI_BUTTON));
			btnAbout = new JButton(Messages.getString("PanelHome.About"), new ImageIcon(ImageShop.UI_BUTTON));
			
			configButtonListeners();
			
			box.add(btnSearchForPlayer);
			box.add(btnSettings);
			box.add(btnAbout);
			
			add(box, BorderLayout.CENTER);
		}

		private void configButtonListeners()
		{
			btnSearchForPlayer.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					PanelHome.this.searchPlayer();
				}
			});
			
			btnSettings.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					PanelHome.this.settings();
				}
			});
			
			btnAbout.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					PanelHome.this.about();
				}
			});
		}
	}
	
	public PanelHome(FrameMain rootFrame)
	{
		this.rootFrame = rootFrame;
		
		setLayout(new BorderLayout(kHgap, kVgap));
		JLabel labelMenuImage = new JLabel(new ImageIcon(ImageShop.UI_MENU_BACKGROUND));
		add(labelMenuImage, BorderLayout.CENTER);
		add(new PanelMenu(), BorderLayout.EAST);
	}

	public void searchPlayer()
	{
		rootFrame.searchPlayer();
	}
	
	public void settings()
	{
		// TODO: Retrieve the player's current name
		String oldName = "Toto";
		// TODO: Store the new player's name
		String newName = DialogPlayerName.promptUserForName(this, oldName);
	}

	public void about()
	{
		new DialogAbout(rootFrame);
	}

	public void quit()
	{

	}

}
