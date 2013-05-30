package ch.hearc.p2.battleforatlantis.gameengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import org.json.JSONObject;
import org.json.JSONString;

import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.Settings;

public class Ship extends MapElement implements JSONString
{
	/**
	 * ID of the ship. Used to identify the ships of the player
	 */
	private int id;

	/**
	 * Type of ship (ship, submarine)
	 */
	private ShipType type;

	/**
	 * Images list of the ship
	 */
	private Image[] initialImages;

	/**
	 * Box considered as ship's center
	 */
	private Box center = null;

	/**
	 * Ship orientation, default EAST (front looks on screen's right)
	 */
	private ShipOrientation orientation = ShipOrientation.EAST;

	/**
	 * Color of background
	 */
	private Color backgroundColor;

	/**
	 * Default constructor for ship instantiation
	 * 
	 * @param size
	 *            Length of ship, in boxes number
	 * @param type
	 *            Type of ship (ship, submarine)
	 */
	public Ship(int size, ShipType type, int id)
	{
		// Call to parent constructor for standard settings
		super(size);

		// Input fields
		this.type = type;
		this.initialImages = new Image[size];
		this.id = id;

		// Set size of boat and display settings
		this.setInitialSize(size * 60, 60);
		this.backgroundColor = new Color(0, 0, 0);

		// Load images corresponding to boat parts
		for (int i = 0; i < size; i++)
		{
			this.images[i] = ImageShop.loadShipImage(type, size, i + 1, false);
			this.initialImages[i] = ImageShop.loadShipImage(type, size, i + 1, false);
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

		// Add listener for ship resizing
		this.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				setCurrentSize(getWidth(), getHeight());
			}
		});
	}

	/**
	 * External call for ship rotation
	 * 
	 * @param clockwise
	 *            Indicates if it's a clockwise rotation
	 */
	public void rotate(boolean clockwise)
	{
		// Cancel ship rotation if ship is not currently displayed
		if (this.center == null)
		{
			return;
		}

		// Clean currently occupied boxes
		moveOut();

		if (clockwise)
		{
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
		else
		{
			// Execute rotation with new orientation value, depending on old value
			switch (this.orientation)
			{
				case EAST:
					move(null, ShipOrientation.NORTH);
					break;
				case SOUTH:
					move(null, ShipOrientation.EAST);
					break;
				case WEST:
					move(null, ShipOrientation.SOUTH);
					break;
				case NORTH:
					move(null, ShipOrientation.WEST);
					break;
			}
		}
	}

	/**
	 * Call for ship rotation
	 * 
	 * @param oldOrientation
	 *            Old orientation of the ship
	 * @param newOrientation
	 *            New orientation of the ship
	 */
	private void rotateImage(ShipOrientation oldOrientation, ShipOrientation newOrientation)
	{
		if (oldOrientation.equals(newOrientation))
			return;

		int iterations = 0;

		if (oldOrientation.equals(ShipOrientation.NORTH) && newOrientation.equals(ShipOrientation.SOUTH) || oldOrientation.equals(ShipOrientation.SOUTH)
				&& newOrientation.equals(ShipOrientation.NORTH) || oldOrientation.equals(ShipOrientation.EAST) && newOrientation.equals(ShipOrientation.WEST)
				|| oldOrientation.equals(ShipOrientation.WEST) && newOrientation.equals(ShipOrientation.EAST))
			iterations = 2;
		else if (oldOrientation.equals(ShipOrientation.NORTH) && newOrientation.equals(ShipOrientation.EAST) || oldOrientation.equals(ShipOrientation.EAST)
				&& newOrientation.equals(ShipOrientation.SOUTH) || oldOrientation.equals(ShipOrientation.SOUTH) && newOrientation.equals(ShipOrientation.WEST)
				|| oldOrientation.equals(ShipOrientation.WEST) && newOrientation.equals(ShipOrientation.NORTH))
			iterations = 1;
		else if (oldOrientation.equals(ShipOrientation.NORTH) && newOrientation.equals(ShipOrientation.WEST) || oldOrientation.equals(ShipOrientation.WEST)
				&& newOrientation.equals(ShipOrientation.SOUTH) || oldOrientation.equals(ShipOrientation.SOUTH) && newOrientation.equals(ShipOrientation.EAST)
				|| oldOrientation.equals(ShipOrientation.EAST) && newOrientation.equals(ShipOrientation.NORTH))
			iterations = 3;

		// Make rotation for each image
		for (BufferedImage image : images)
		{
			// Make the number of rotations indicated in iteration variable
			for (int i = 1; i <= iterations; i++)
				ImageShop.inplaceImageRotation(image);
		}
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

		// Image rotation
		if (!this.orientation.equals(orientation))
		{
			rotateImage(this.orientation, orientation);
		}

		// Assign new center
		this.center = box;

		// Compute size of boat rear for boxes alignment
		int rearSize = this.wholeSize / 2;

		// Get the map concerned by movement
		Map map = box.getMap();

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
			if ((this.occupied[i].getOccupier() != this) && (this.occupied[i].getOccupier() instanceof Ship))
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
	 * Get ID of the ship
	 * 
	 * @return ID of the ship
	 */
	public int getId()
	{
		return id;
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

	/**
	 * Get center box of the ship
	 * 
	 * @return Center box
	 */
	public Box getCenter()
	{
		return center;
	}

	/**
	 * Create a JSON Object to communicate the ship to the opposing player
	 */
	@Override
	public String toJSONString()
	{
		JSONObject jo = new JSONObject();
		jo.put("shipId", id);
		jo.put("shipType", type.toString());
		jo.put("size", wholeSize);
		JSONObject center = new JSONObject();
		center.put("x", this.center.getCoordX());
		center.put("y", this.center.getCoordY());
		jo.put("center", center);
		jo.put("direction", orientation);
		return jo.toString();
	}

	/**
	 * Make the ship from a request received by the player
	 * 
	 * @param jo
	 *            JSON Object received by the player
	 * @return The ship corresponding to the request
	 */
	public static Ship createFromJSONObject(JSONObject jo, Box center)
	{
		int size = jo.getInt("size");
		ShipType shipType = ShipType.valueOf(jo.getString("shipType"));
		int id = jo.getInt("shipId");

		Ship ship = new Ship(size, shipType, id);
		ShipOrientation orientation = ShipOrientation.valueOf(jo.getString("direction"));
		ship.move(center, orientation);

		return ship;
	}

	/**
	 * Paint method for displaying ship in menus
	 */
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		int w = currentSize.width;
		int h = currentSize.height;

		g2d.setColor(this.backgroundColor);
		g2d.fillRect(getWidth() - w, 0, w, h);

		for (int i = 0; i < wholeSize; i++)
		{
			g2d.drawImage(this.initialImages[i], getWidth() - w + i * w / wholeSize, 0, w / wholeSize, h, null);
		}
	}

	@Override
	protected void setCurrentSize(int width, int height)
	{
		int min = width / wholeSize;

		if (height < min)
			min = height;

		this.currentSize = new Dimension(min * wholeSize, min);
	}

	public void setBackgroundColor(Color color)
	{
		this.backgroundColor = color;
		repaint();
	}

	public void place(boolean forward)
	{
		int x = center.getCoordX();
		int y = center.getCoordY();

		int modifier = forward ? 1 : -1;

		switch (orientation)
		{
			case EAST:
				x += modifier;
				break;
			case NORTH:
				y -= modifier;
				break;
			case SOUTH:
				y += modifier;
				break;
			case WEST:
				x -= modifier;
				break;
		}

		move(center.getMap().getBox(x, y), orientation);
	}

	/**
	 * Get orientation of the ship
	 * 
	 * @return
	 */
	public ShipOrientation getOrientation()
	{
		return orientation;
	}

	/**
	 * Shoot on the ship
	 */
	@Override
	public void shoot(Box target)
	{
		touchedSize++;
		
		// Get whole size of the ship
		int size = this.getWholeSize();
		
		// Get size before the center box of the ship
		int beforeSize = Integer.valueOf(size / 2);
		
		// Get X coordinates of the target and the center of the ship
		int targetX = target.getCoordX();
		int centerX = center.getCoordX();
		
		// Calculate the coordinate of the first box of the ship
		int baseX = centerX - beforeSize;
		
		// Get the difference between the target and the first box of the ship
		int deltaTarget = targetX - baseX;
		
		// Calculate the part number of the ship destroyed image
		int partNumber = 1 + deltaTarget;
		
		// Variable used to know how many rotation we have to made in function of the ship orientation
		int iterations = 0;
		
		// Choose the good orientation of the ship destroyed image
		if (orientation.equals(ShipOrientation.NORTH))
			iterations = 1;
		
		else if (orientation.equals(ShipOrientation.WEST))
			iterations = 2;
		
		else if (orientation.equals(ShipOrientation.SOUTH))
			iterations = 3;
		
		// Get the ship destroyed image
		BufferedImage img = ImageShop.loadShipImage(type, initialImages.length, partNumber, true);
		
		// Make the number of rotations indicated in iteration variable
		for (int i = 1; i <= iterations; i++)
			ImageShop.inplaceImageRotation(img);
		
		// Set image to the ship
		target.setImage(img);
	}

	public boolean rotationPossible(boolean clockwise)
	{
		// Get map
		Map map = this.center.getMap();

		// Compute size of boat rear for boxes alignment
		int rearSize = this.wholeSize / 2;

		// Get the new orientation
		ShipOrientation newOrientation = ShipOrientation.next(this.orientation, clockwise);

		// Return false if any box of the new position is already occupied
		for (int i = 0; i < this.wholeSize; i++)
		{
			Box currentBox = null;
			switch (newOrientation)
			{
				case EAST:
					currentBox = map.getBox(this.center.getCoordX() - rearSize + i, this.center.getCoordY());
					if (currentBox == null)
						return false;
					if (currentBox.getOccupier() != null && currentBox.getOccupier() != this)
						return false;
					break;
				case WEST:
					currentBox = map.getBox(this.center.getCoordX() + rearSize - i, this.center.getCoordY());
					if (currentBox == null)
						return false;
					if (currentBox.getOccupier() != null && currentBox.getOccupier() != this)
						return false;
					break;
				case NORTH:
					currentBox = map.getBox(this.center.getCoordX(), this.center.getCoordY() + rearSize - i);
					if (currentBox == null)
						return false;
					if (currentBox.getOccupier() != null && currentBox.getOccupier() != this)
						return false;
					break;
				case SOUTH:
					currentBox = map.getBox(this.center.getCoordX(), this.center.getCoordY() - rearSize + i);
					if (currentBox == null)
						return false;
					if (currentBox.getOccupier() != null && currentBox.getOccupier() != this)
						return false;
					break;
			}
		}
		
		return true;
	}
}
