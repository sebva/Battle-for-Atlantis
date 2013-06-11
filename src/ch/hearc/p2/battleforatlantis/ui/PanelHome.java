package ch.hearc.p2.battleforatlantis.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.hearc.p2.battleforatlantis.net.NetworkManager;
import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.Messages;

/**
 * Main panel for displaying the main menu (home page)
 */
public class PanelHome extends JPanel
{
	/** Main frame in which the panel is displayed */
	private FrameMain rootFrame;

	/**
	 * Private panel for right menu display
	 */
	private class PanelMenu extends JPanel
	{
		/** Button for going to connection panel */
		private JButton btnSearchForPlayer;

		/** Button for setting player name */
		private JButton btnSettings;

		/** Button for opening about dialog */
		private JButton btnAbout;

		/**
		 * Default constructor
		 */
		public PanelMenu()
		{
			Box box = Box.createVerticalBox();

			btnSearchForPlayer = new CustomButton(Messages.getString("PanelHome.SearchForPlayer"));
			btnSettings = new CustomButton(Messages.getString("PanelHome.Settings"));
			btnAbout = new CustomButton(Messages.getString("PanelHome.About"));

			configButtonListeners();

			box.add(btnSearchForPlayer);
			box.add(btnSettings);
			box.add(btnAbout);

			add(box, BorderLayout.CENTER);
		}

		/**
		 * Set the buttons listeners
		 */
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

	/**
	 * Default constructor
	 * 
	 * @param rootFrame Frame in which the panel is displayed
	 */
	public PanelHome(FrameMain rootFrame)
	{
		this.rootFrame = rootFrame;

		setLayout(new BorderLayout());

		JLabel labelMenuImage = new JLabel(new ImageIcon(ImageShop.UI_MENU_BACKGROUND));

		Box boxImage = Box.createVerticalBox();
		boxImage.add(labelMenuImage);
		boxImage.add(Box.createVerticalGlue());

		add(new PanelMenu(), BorderLayout.EAST);
		add(boxImage, BorderLayout.WEST);
	}

	/**
	 * Go to the connection panel
	 */
	public void searchPlayer()
	{
		rootFrame.searchPlayer();
	}

	/**
	 * Ask for the player name and apply new given value
	 */
	public void settings()
	{
		String newName = DialogPlayerName.promptUserForName(this, rootFrame.getPlayerName());
		rootFrame.setPlayerName(newName);
		NetworkManager.getInstance().localhost.setName(newName);
	}

	/**
	 * Open the about dialog
	 */
	public void about()
	{
		new DialogAbout(rootFrame);
	}

}
