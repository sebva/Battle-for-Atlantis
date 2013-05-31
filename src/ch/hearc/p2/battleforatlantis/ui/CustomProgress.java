package ch.hearc.p2.battleforatlantis.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import ch.hearc.p2.battleforatlantis.utils.ImageShop;

public class CustomProgress extends JComponent
{
	private int minimum;
	private int maximum;
	private int current;
	private int interval;

	private Dimension preferredSize;
	private Dimension minimumSize;

	public CustomProgress(int minimum, int maximum)
	{
		setBackground(Color.RED);

		this.minimum = minimum;
		this.maximum = maximum;
		this.current = minimum;
		this.interval = maximum - minimum;

		this.minimumSize = new Dimension(maximum - minimum + 28, 27);
		this.preferredSize = new Dimension(600, 27);

		this.setMinimumSize(minimumSize);
		this.setPreferredSize(preferredSize);
		this.setMaximumSize(preferredSize);
	}

	public void setValue(int current)
	{
		if (current >= this.minimum && current <= this.maximum)
		{
			this.current = current;
			repaint();
		}
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		int width = this.getWidth();
		int filled = this.current - this.minimum;
		double percent = filled / (double) this.interval;
		int position = (int) (width * percent);

		System.out.println("Width : " + width + " / Height : " + getHeight() + " / Filled : " + filled + " / Percent : " + percent + " / Position : "
				+ position);

		for (int i = 0; i < width - 27; i++)
		{
			if (i < position)
			{
				System.out.print("I");
				g2d.drawImage(ImageShop.UI_PROGRESS_YES, i, 0, null);
			}
			else
			{
				System.out.print(".");
				g2d.drawImage(ImageShop.UI_PROGRESS_NO, i, 0, null);
			}
		}
		System.out.println();
	}
}
