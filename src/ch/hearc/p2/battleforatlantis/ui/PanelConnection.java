package ch.hearc.p2.battleforatlantis.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;

import ch.hearc.p2.battleforatlantis.net.Host;
import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.Messages;

public class PanelConnection extends JPanel
{
	private static final int kHgap = 30;
	private static final int kVgap = 0;
	
	private FrameMain rootFrame;
	
	private class PanelMenu extends JPanel
	{
		private JButton btnDirectConnect;
		private JButton btnBack;

		public PanelMenu()
		{
			setLayout(new BorderLayout());
			Box box = Box.createVerticalBox();
			
			btnDirectConnect = new JButton(Messages.getString("PanelConnection.DirectConnect"), new ImageIcon(ImageShop.UI_BUTTON));
			btnBack = new JButton(Messages.getString("PanelConnection.Back"), new ImageIcon(ImageShop.UI_BUTTON));
			
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
		
		setLayout(new BorderLayout(kHgap, kVgap));
		JTable players = new JTable(7, 2);
		
		add(players, BorderLayout.CENTER);
		add(new PanelMenu(), BorderLayout.EAST);
		add(new PanelUp(), BorderLayout.NORTH);
	}

	public void connect(Host host)
	{
		rootFrame.placeShips();
	}

	public void directConnect()
	{

	}

	public void backToMenu()
	{
		rootFrame.endGame();
	}

}
