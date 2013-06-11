package ch.hearc.p2.battleforatlantis.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import ch.hearc.p2.battleforatlantis.utils.ImageShop;

/**
 * Custom progress bar for the players progression
 */
public class CustomProgress extends JComponent
{
	/** Minimum value of progress bar (when empty) */
	private int minimum;

	/** Maximum value of progress bar (when full) */
	private int maximum;

	/** Current value of progress bar */
	private int current;

	/** Interval available between min and max */
	private int interval;

	/** Preferred and standard size */
	private Dimension preferredSize;

	/** Minmum size for correct display */
	private Dimension minimumSize;

	/**
	 * Default constructor
	 * 
	 * @param minimum Minimum value of bar
	 * @param maximum Maximum value of bar
	 */
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

	/**
	 * Set the current value of progress
	 * 
	 * @param current New progress value
	 */
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

		// Compute the positions for painting
		int width = this.getWidth();
		int filled = this.current - this.minimum;
		double percent = filled / (double) this.interval;
		int position = (int) (width * percent);

		// Paint the bar
		for (int i = 0; i < width - 27; i++)
		{
			if (i < position)
			{
				g2d.drawImage(ImageShop.UI_PROGRESS_YES, i, 0, null);
			}
			else
			{
				g2d.drawImage(ImageShop.UI_PROGRESS_NO, i, 0, null);
			}
		}
	}
}
