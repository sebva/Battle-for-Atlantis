package ch.hearc.p2.battleforatlantis.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.Settings;

/**
 * Custom Button for the Game Menu
 */
public class CustomButton extends JButton
{
	/** Text of button */
	private String text;

	/** Dimension of button */
	private Dimension dimension;

	/** Font for text on button */
	private Font font;

	/** Indicator of mouse hovering */
	private boolean isHover;

	/**
	 * Constructor of the Menu Custom Button
	 * 
	 * @param text : Text to show in the button
	 */
	public CustomButton(String text)
	{
		this.text = text;
		this.dimension = new Dimension(300, 55);
		this.font = new Font("Helvetica", Font.BOLD, 16);
		this.isHover = false;

		setPreferredSize(this.dimension);
		setMinimumSize(this.dimension);
		setMaximumSize(this.dimension);

		setBorderPainted(false);
		setContentAreaFilled(false);

		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseExited(MouseEvent e)
			{
				CustomButton.this.isHover = false;
				CustomButton.this.repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				CustomButton.this.isHover = true;
				CustomButton.this.repaint();
			}
		});
	}

	/**
	 * Draw the custom button in the graphic context
	 * 
	 * @param g : Graphic Context
	 */
	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		if (this.isHover)
		{
			g2d.drawImage(ImageShop.UI_BUTTON_HOVER, 0, 0, null);
		}
		else
		{
			g2d.drawImage(ImageShop.UI_BUTTON, 0, 0, null);
		}
		g2d.setColor(Settings.MENU_BORDER_COLOR);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setFont(this.font);
		g2d.drawString(this.text, 50, 30);
	}

}
