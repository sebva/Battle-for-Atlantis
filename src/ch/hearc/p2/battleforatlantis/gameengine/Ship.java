package ch.hearc.p2.battleforatlantis.gameengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import ch.hearc.p2.battleforatlantis.ui.FrameMain;
import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.ImageShop.ShipType;

public class Ship extends MapElement
{

	// TODO: useful ?
	private String imagesNames[];

	private ShipOrientation orientation = ShipOrientation.EAST;

	public Ship(ShipType type, int size)
	{
		// TODO: verify size validity
		// ...

		super(size);
		this.displaySize = new Dimension(size * 60, 60);
		setLayout(new GridLayout(1, size, 0, 0));
		this.setPreferredSize(this.displaySize);
		this.setMinimumSize(this.displaySize);
		this.setMaximumSize(this.displaySize);
		this.setBackground(Color.LIGHT_GRAY);
		for (int i = 0; i < size; i++)
		{
			this.images[i] = ImageShop.loadShipImage(type, size, i + 1, false);
			add(new JLabel(new ImageIcon(this.images[i])));
		}
	}

	public void rotate()
	{
		switch (this.orientation)
		{
			case EAST:
				move(null, ShipOrientation.SOUTH);
				break;
			case SOUTH:
				move(null, ShipOrientation.WEST);
				break;
			case WEST:
				move(null, ShipOrientation.NORTH);
				break;
			case NORTH:
				move(null, ShipOrientation.EAST);
				break;
		}
	}

	public void move(Box box, ShipOrientation orientation)
	{
		if (this.center != null)
		{
			for (Box ancient : this.occupied)
			{
				ancient.setOccupier(null, null);
			}
		}

		if (orientation == null)
		{
			orientation = this.orientation;
		}

		if (box == null)
		{
			box = this.center;
		}

		this.center = box;

		int rearSize = this.wholeSize / 2;

		Map map = null;
		switch (box.getMapType())
		{
			case SURFACE:
				map = FrameMain.getPanelPrepare().getMapSurface();
				break;
			case SUBMARINE:
				map = FrameMain.getPanelPrepare().getMapSubmarine();
				break;
		}

		for (int i = 0; i < this.wholeSize; i++)
		{
			switch (orientation)
			{
				case EAST:
					this.occupied[i] = map.getBox(box.getCoordX() - rearSize + i, box.getCoordY());
					break;
				case WEST:
					this.occupied[i] = map.getBox(box.getCoordX() + rearSize - i, box.getCoordY());
					break;
				case NORTH:
					this.occupied[i] = map.getBox(box.getCoordX(), box.getCoordY() + rearSize - i);
					break;
				case SOUTH:
					this.occupied[i] = map.getBox(box.getCoordX(), box.getCoordY() - rearSize + i);
					break;
			}
		}

		for (int i = 0; i < this.wholeSize; i++)
		{

			if (this.occupied[i] == null)
			{
				this.center = null;
				return;
			}
		}

		for (int i = 0; i < this.wholeSize; i++)
		{
			this.occupied[i].setOccupier(this, this.images[i]);
		}

		this.orientation = orientation;
	}

}
