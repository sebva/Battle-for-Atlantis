package ch.hearc.p2.battleforatlantis.gameengine;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class Map extends JPanel
{
	private int width;
	private int height;

	Box[][] boxes;

	/**
	 * Create a map of specified dimensions
	 * @param width Number of rows
	 * @param height Number of columns
	 */
	public Map(int width, int height)
	{
		this.width = width;
		this.height = height;

		boxes = new Box[width][height];
		setLayout(new GridLayout(height, width));
		
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				boxes[i][j] = new Box();
				add(boxes[i][j]);
			}
		}
		
		
		
	}

}
