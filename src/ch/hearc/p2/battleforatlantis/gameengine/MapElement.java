package ch.hearc.p2.battleforatlantis.gameengine;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JPanel;

public abstract class MapElement extends JPanel
{

	protected Image images[];
	protected Box center = null;
	protected Box[] occupied;
	protected final int wholeSize;
	protected int touchedSize;
	protected Dimension displaySize;

	public MapElement(int wholeSize)
	{
		this.wholeSize = wholeSize;
		this.touchedSize = 0;
		this.images = new Image[wholeSize];
		this.occupied = new Box[wholeSize];
	}

	public void shoot()
	{

	}

}
