package ch.hearc.p2.battleforatlantis.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import ch.hearc.p2.battleforatlantis.net.Host;
import ch.hearc.p2.battleforatlantis.net.NetworkAutodiscover.NetworkAutodiscoverListener;
import ch.hearc.p2.battleforatlantis.net.NetworkManager;
import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.Messages;

/**
 * Main panel for displaying connections options
 */
public class PanelConnection extends JPanel implements NetworkAutodiscoverListener
{
	/** Default horizontal gap in layouts */
	private static final int kHgap = 30;

	/** Default vertical gap in layouts */
	private static final int kVgap = 0;

	/** Preferred standard dimension */
	private static final Dimension preferredSize = new Dimension(800, 30);

	/** Minimal dimension for correct display */
	private static final Dimension minimalSize = new Dimension(300, 30);

	/** Size of buttons */
	private static final Dimension buttonSize = new Dimension(140, 24);

	/** Color of active border */
	private static final Color colorBorderActive = new Color(112, 112, 112);

	/** Color of grey backgrounds active */
	private static final Color colorGreyActive = new Color(67, 67, 67);

	/** Color of inactive border */
	private static final Color colorBorderNone = new Color(50, 50, 50);

	/** Color of inactive grey background */
	private static final Color colorGreyNone = new Color(24, 24, 24);

	/** Color of standard blue */
	private static final Color colorBlue = new Color(0, 159, 190);

	/** Main frame containing the panel */
	private FrameMain rootFrame;

	/** Network manager used for communications */
	private NetworkManager networkManager;

	/** List of players available in autodiscover */
	private Map<Host, PanelPlayer> players;

	/** Box layout for displaying players available */
	private Box boxPlayers;

	/**
	 * Private class for displaying a player
	 */
	private class PanelPlayer extends JPanel
	{
		/** Indicates if the mouse is hovering the player line */
		private boolean isHover;

		/**
		 * Default constructor
		 * 
		 * @param player Player linked to host
		 */
		public PanelPlayer(final Host player)
		{
			setLayout(new BorderLayout(0, 0));

			JLabel labelPlayer = new JLabel(player.getName());
			labelPlayer.setBorder(BorderFactory.createEmptyBorder(0, 7, 5, 0));
			final JButton btnConnect = new ButtonConnect(Messages.getString("PanelConnection.Connect"));

			this.isHover = false;
			btnConnect.setVisible(false);

			setPreferredSize(PanelConnection.preferredSize);
			setMaximumSize(PanelConnection.preferredSize);
			setMinimumSize(PanelConnection.minimalSize);

			btnConnect.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					connect(player);
				}
			});

			MouseAdapter mouseAdapter = new MouseAdapter()
			{

				@Override
				public void mouseExited(MouseEvent e)
				{
					isHover = false;
					btnConnect.setVisible(false);
					repaint();
				}

				@Override
				public void mouseEntered(MouseEvent e)
				{
					isHover = true;
					btnConnect.setVisible(true);
					repaint();
				}
			};

			addMouseListener(mouseAdapter);
			btnConnect.addMouseListener(mouseAdapter);

			add(labelPlayer, BorderLayout.WEST);
			add(btnConnect, BorderLayout.EAST);
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;

			// draw the border
			if (isHover)
			{
				g2d.setColor(PanelConnection.colorBorderActive);
			}
			else
			{
				g2d.setColor(PanelConnection.colorBorderNone);
			}
			g2d.drawRect(0, 0, this.getWidth() - 140, 23);

			// draw the content
			if (isHover)
			{
				g2d.setColor(PanelConnection.colorGreyActive);
			}
			else
			{
				g2d.setColor(PanelConnection.colorGreyNone);
			}
			g2d.fillRect(1, 1, this.getWidth() - 141, 22);

			// draw the arrow
			if (isHover)
			{
				g2d.drawImage(ImageShop.UI_ARROW_DARK, this.getWidth() - 145, 6, null);
			}
		}
	}

	/**
	 * Private class for displaying the connection button of a player
	 */
	private class ButtonConnect extends JButton
	{
		/**
		 * Default constructor
		 * 
		 * @param text Text to display on button
		 */
		public ButtonConnect(String text)
		{
			super(text);

			setPreferredSize(PanelConnection.buttonSize);
			setMinimumSize(PanelConnection.buttonSize);
			setMaximumSize(PanelConnection.buttonSize);

			setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			// super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(PanelConnection.colorBlue);
			g2d.fillRect(5, 0, 134, 24);

			g2d.drawImage(ImageShop.UI_ARROW_LIGHT, 1, 7, null);

			g2d.setColor(Color.WHITE);
			g2d.drawString(this.getText(), 30, 17);
		}
	}

	/**
	 * Private class for right menu
	 */
	private class PanelMenu extends JPanel
	{
		/** Button for access the direct connection dialog */
		private JButton btnDirectConnect;

		/** Button for going back to the menu */
		private JButton btnBack;

		/**
		 * Default constructor
		 */
		public PanelMenu()
		{
			setLayout(new BorderLayout());
			Box box = Box.createVerticalBox();

			btnDirectConnect = new CustomButton(Messages.getString("PanelConnection.DirectConnect"));
			btnBack = new CustomButton(Messages.getString("PanelConnection.Back"));

			configButtonListeners();

			box.add(btnDirectConnect);
			box.add(btnBack);

			add(box, BorderLayout.CENTER);
		}

		/**
		 * Configure listeners for the buttons
		 */
		private void configButtonListeners()
		{
			btnDirectConnect.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					PanelConnection.this.directConnect();
				}
			});

			btnBack.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					PanelConnection.this.backToMenu();
				}
			});
		}
	}

	/**
	 * Private class for top panel display
	 */
	private class PanelUp extends JPanel
	{
		/**
		 * Default constructor
		 */
		public PanelUp()
		{
			setLayout(new FlowLayout(FlowLayout.LEFT));
			JLabel lblSearchForPlayer = new JLabel(Messages.getString("PanelConnection.SearchForPlayer"));
			JProgressBar progressBar = new JProgressBar();
			progressBar.setValue(33);

			add(lblSearchForPlayer);
			add(progressBar);
		}
	}

	/**
	 * Default constructor
	 * 
	 * @param rootFrame Frame in which the panel is displayed
	 */
	public PanelConnection(FrameMain rootFrame)
	{
		this.rootFrame = rootFrame;
		networkManager = NetworkManager.getInstance();

		setLayout(new BorderLayout(kHgap, kVgap));
		players = new HashMap<Host, PanelPlayer>();

		boxPlayers = Box.createVerticalBox();
		// boxPlayers.add(new JSeparator(SwingConstants.HORIZONTAL));

		add(boxPlayers, BorderLayout.CENTER);
		add(new PanelMenu(), BorderLayout.EAST);
		add(new PanelUp(), BorderLayout.NORTH);
	}

	/**
	 * Connect to a given host
	 * 
	 * @param host Host to join
	 */
	public void connect(Host host)
	{
		networkManager.tryConnect(host.getAddress(), rootFrame.getHashConfig());
	}

	/**
	 * Open the dialog for direct connection and join given host
	 */
	public void directConnect()
	{
		InetAddress addr = DialogDirectConnect.promptUserForAddress(this);
		if (addr != null)
			networkManager.tryConnect(addr, rootFrame.getHashConfig());
	}

	/**
	 * Go back to main menu
	 */
	public void backToMenu()
	{
		rootFrame.endGame();
		networkManager.removeAutodiscoverListener(this);
	}

	@Override
	public void hostAppeared(Host host)
	{
		PanelPlayer panelPlayer = new PanelPlayer(host);
		boxPlayers.add(panelPlayer);
		players.put(host, panelPlayer);

		validate();

		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("New player : " + host);
	}

	@Override
	public void hostDisappeared(Host host)
	{
		PanelPlayer panelPlayer = players.get(host);
		boxPlayers.remove(panelPlayer);
		players.remove(host);

		validate();

		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Player left : " + host);
	}

}
