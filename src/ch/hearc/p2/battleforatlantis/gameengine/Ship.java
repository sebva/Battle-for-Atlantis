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
	private int id;
	/**
	 * Type of ship (ship, submarine)
	 */
	private ShipType type;

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
	 * Default constructor for ship instanciation
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
		// FIXME: FAIL: Network received boats are put on local maps !
		//Map map = null;
		/*
		switch (box.getMapType())
		{
			case SURFACE:
				map = FrameMain.getPanelPrepare().getMapSurface();
				break;
			case SUBMARINE:
				map = FrameMain.getPanelPrepare().getMapSubmarine();
				break;
		}*/
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
		{
			min = height;
		}
		
		this.currentSize = new Dimension(min * wholeSize, min);
	}
	
	public void setBackgroundColor(Color color)
	{
		this.backgroundColor = color;
		repaint();
	}

}
