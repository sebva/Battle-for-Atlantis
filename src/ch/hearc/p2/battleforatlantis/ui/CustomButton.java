package ch.hearc.p2.battleforatlantis.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;

import ch.hearc.p2.battleforatlantis.utils.ImageShop;

public class CustomButton extends JButton
{
	public CustomButton(String text)
	{
		this.text = text;
		this.dimension = new Dimension(300,55);
		this.font = new Font("Helvetica", Font.PLAIN, 20);
		
		setPreferredSize(this.dimension);
		setMinimumSize(this.dimension);
		setMaximumSize(this.dimension);
		
		setBorderPainted(false);
		setContentAreaFilled(false);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(ImageShop.UI_BUTTON,0,0,null);
		g2d.setColor(Color.CYAN);
		g2d.setFont(this.font);
		g2d.drawString(this.text, 50, 30);
	}
	
	private String text;
	private Dimension dimension;
	private Font font;
}
