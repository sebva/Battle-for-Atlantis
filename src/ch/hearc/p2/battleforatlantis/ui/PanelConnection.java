package ch.hearc.p2.battleforatlantis.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import ch.hearc.p2.battleforatlantis.net.Host;
import ch.hearc.p2.battleforatlantis.net.NetworkAutodiscover.NetworkAutodiscoverListener;
import ch.hearc.p2.battleforatlantis.net.NetworkManager;
import ch.hearc.p2.battleforatlantis.utils.Messages;

public class PanelConnection extends JPanel implements NetworkAutodiscoverListener
{
	private static final int kHgap = 30;
	private static final int kVgap = 0;
	
	private FrameMain rootFrame;
	private NetworkManager networkManager;
	private Map<Host, PanelPlayer> players;
	private Box boxPlayers;
	
	private class PanelPlayer extends JPanel
	{
		public PanelPlayer(final Host player)
		{
			setLayout(new FlowLayout(FlowLayout.LEFT));
			add(new JLabel(player.getName()));
			final JButton btnConnect = new JButton(Messages.getString("PanelConnection.Connect"));

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
					btnConnect.setVisible(false);
				}
				
				@Override
				public void mouseEntered(MouseEvent e)
				{
					btnConnect.setVisible(true);
				}
			};
			addMouseListener(mouseAdapter);
			btnConnect.addMouseListener(mouseAdapter);
			add(btnConnect);
		}
	}
	
	private class PanelMenu extends JPanel
	{
		private JButton btnDirectConnect;
		private JButton btnBack;

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
	
	private class PanelUp extends JPanel
	{
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
	
	public PanelConnection(FrameMain rootFrame)
	{
		this.rootFrame = rootFrame;
		networkManager = NetworkManager.getInstance();
		
		setLayout(new BorderLayout(kHgap, kVgap));
		players = new HashMap<Host, PanelPlayer>();
		
		boxPlayers = Box.createVerticalBox();
		boxPlayers.add(new JSeparator(SwingConstants.HORIZONTAL));
		
		add(boxPlayers, BorderLayout.CENTER);
		add(new PanelMenu(), BorderLayout.EAST);
		add(new PanelUp(), BorderLayout.NORTH);
	}

	public void connect(Host host)
	{
		rootFrame.placeShips();
		networkManager.removeAutodiscoverListener(this);
	}

	public void directConnect()
	{
		// TODO Do something with the address
		InetAddress addr = DialogDirectConnect.promptUserForAddress(this);
		if(addr != null)
		{
			rootFrame.placeShips();
			networkManager.removeAutodiscoverListener(this);
		}
	}

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
		
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("New player : " + host);
	}

	@Override
	public void hostDisappeared(Host host)
	{
		PanelPlayer panelPlayer = players.get(host);
		boxPlayers.remove(panelPlayer);
		players.remove(host);
		
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Player left : " + host);
	}

}
