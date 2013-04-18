package ch.hearc.p2.battleforatlantis.gameengine;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Box extends JPanel
{
	private boolean discovered = false;
	private Image image;
	private MapElement mapElement = null;
	private Map map;
	private MapType type;
	private Dimension size;

	public Box(Map map)
	{
		this.map = map;
		
		// TODO: Remove test code below
		setLayout(new BorderLayout(0,0));
		
		JLabel img = new JLabel(new ImageIcon(map.getType().getBackground()));
		add(img, BorderLayout.CENTER);

		
		this.size = new Dimension(60, 60);
		setPreferredSize(this.size);
		setMinimumSize(this.size);
		setMaximumSize(this.size);
	}

	public void shoot()
	{

	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(this.image, 0, 0, null);
	}

}
