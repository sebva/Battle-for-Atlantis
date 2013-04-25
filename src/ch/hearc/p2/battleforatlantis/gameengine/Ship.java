package ch.hearc.p2.battleforatlantis.gameengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import ch.hearc.p2.battleforatlantis.ui.FrameMain;
import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.Settings;

public class Ship extends MapElement
{
	/**
	 * Type of ship (ship, submarine)
	 */
	private ShipType type;

	private Icon[] initialImages;

	/**
	 * Box considered as ship's center
	 */
	private Box center = null;

	/**
	 * Ship orientation, default EAST (front looks on screen's right)
	 */
	private ShipOrientation orientation = ShipOrientation.EAST;

	/**
	 * Default constructor for ship instanciation
	 * 
	 * @param size
	 *            Length of ship, in boxes number
	 * @param type
	 *            Type of ship (ship, submarine)
	 */
	public Ship(int size, ShipType type)
	{
		// Call to parent constructor for standard settings
		super(size);

		// Input fields
		this.type = type;
		this.initialImages = new Icon[size];

		// Set size of boat and display settings
		this.displaySize = new Dimension(size * 60, 60);
		setLayout(new GridLayout(1, size, 0, 0));
		this.setPreferredSize(this.displaySize);
		this.setMinimumSize(this.displaySize);
		this.setMaximumSize(this.displaySize);
		this.setBackground(Color.BLACK);

		// Load images corresponding to boat parts
		for (int i = 0; i < size; i++)
		{
			this.images[i] = ImageShop.loadShipImage(type, size, i + 1, false);
			this.initialImages[i] = new ImageIcon(ImageShop.loadShipImage(type, size, i + 1, false));
			add(new JLabel(this.initialImages[i]));
		}

		// Add listener for ship selection
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (center == null)
					Settings.PANEL_PREPARE.shipClick(Ship.this);
			}
		});
	}

	/**
	 * External call for ship rotation in clockwise direction
	 */
	public void rotate()
	{
		// Cancel ship rotation if ship is not currently displayed
		if (this.center == null)
		{
			return;
		}

		// Clean currently occupied boxes
		moveOut();

		rotateImage();

		// Execute rotation with new orientation value, depending on old value
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

	private void rotateImage()
	{
		for (BufferedImage image : images)
			ImageShop.inplaceImageRotation(image);
	}

	/**
	 * External call for clearance of all boxes that are no longer occupied by the boat
	 */
	public void moveOut()
	{
		if (this.center != null)
		{
			for (Box ancient : this.occupied)
			{
				ancient.setOccupier(null, null);
			}
		}
	}

	/**
	 * External call for ship movement
	 * 
	 * @param box
	 *            New box considered as boat center
	 * @param orientation
	 *            New orientation of boat
	 */
	@SuppressWarnings("incomplete-switch")
	public void move(Box box, ShipOrientation orientation)
	{
		// Conserve current orientation if arg null
		if (orientation == null)
		{
			orientation = this.orientation;
		}

		// Conserve current center position if arg null
		if (box == null)
		{
			box = this.center;
		}

		// Assign new center
		this.center = box;

		// Compute size of boat rear for boxes alignment
		int rearSize = this.wholeSize / 2;

		// Get the map concerned by movement
		// TODO: modify and adapt when maps and ships are used on panel play (static method no longer good idea)
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

		// Compute positions and gather boxes hosting boat
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

		// Apply new orientation
		this.orientation = orientation;

		// Cancel and exit if ship is out of map or if a box is already occupied
		for (int i = 0; i < this.wholeSize; i++)
		{
			if (this.occupied[i] == null)
			{
				this.center = null;
				return;
			}
			if ((this.occupied[i].getOccupier() != this) && (this.occupied[i].getOccupier() != null))
			{
				this.center = null;
				return;
			}
		}

		// Set current ship as new occupier for all occupied boxes
		for (int i = 0; i < this.wholeSize; i++)
		{
			this.occupied[i].setOccupier(this, this.images[i]);
		}
	}

	/**
	 * Get current ship type
	 * 
	 * @return Type of ship (ship, submarine)
	 */
	public ShipType getType()
	{
		return type;
	}

	public Box getCenter()
	{
		return center;
	}

}
