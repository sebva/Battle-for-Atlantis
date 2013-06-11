package ch.hearc.p2.battleforatlantis.gameengine;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import org.json.JSONObject;
import org.json.JSONString;

import ch.hearc.p2.battleforatlantis.ui.PanelPlay;
import ch.hearc.p2.battleforatlantis.ui.PanelPrepare;
import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.Settings;

/**
 * Single box for maps construction
 */
public class Box extends JPanel implements JSONString
{
	/** Discover marker, true when box has been shot by ennemy */
	private boolean discovered = false;

	/** Image for displaying box content (water) */
	private Image imageBox;

	/** Image for displaying correct part of boat in box */
	private Image imageOccupier = null;

	/** Image for war fog */
	private Image imageOverlay = null;

	/** Map to which this box belong */
	private Map map;

	/** Occupier of this box, or null if not */
	private MapElement occupier;

	/** Type of box (surface, submarine, atlantis) */
	private MapType type;

	/** Preferred dimension of box */
	private Dimension sizePreferred;

	/** Minimal dimension of box */
	private Dimension sizeMinimal;

	/** Registered dimension */
	private int size;

	/** Horizontal coordinate of box in map (left-side is 0) */
	private int x;

	/** Vertical coordinate of box in map (upper-side is 0) */
	private int y;

	/**
	 * Default constructor for box-in-map instanciation
	 * 
	 * @param map Map to which current box belongs
	 * @param type Type of box to display (surface, submarine, atlantis)
	 * @param x Horizontal coordinate of box in map (left-side is 0)
	 * @param y Vertical coordinate of box in map (upper-side is 0)
	 */
	public Box(final Map map, MapType type, int x, int y)
	{
		// Input fields
		this.type = type;
		this.map = map;
		this.x = x;
		this.y = y;

		// Get water image depending on map type
		switch (type)
		{
			case SURFACE:
				this.imageBox = ImageShop.BACKGROUND_SURFACE;
				break;
			case SUBMARINE:
				this.imageBox = ImageShop.BACKGROUND_SUBMARINE;
				break;
			case ATLANTIS:
				this.imageBox = ImageShop.BACKGROUND_ATLANTIS;
				break;
		}

		if (!map.isLocal() || map.getType() == MapType.ATLANTIS)
			imageOverlay = ImageShop.STATE_NOT_DISCOVERED;

		// Set size of box to display on screen
		this.sizePreferred = new Dimension(60, 60);
		this.sizeMinimal = new Dimension(30, 30);
		setPreferredSize(this.sizePreferred);
		setMinimumSize(this.sizeMinimal);
		setMaximumSize(this.sizePreferred);
		this.size = 60;

		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				PanelPlay panelPlay = Settings.PANEL_PLAY;
				if (panelPlay == null)
				{
					PanelPrepare panelPrepare = Settings.PANEL_PREPARE;
					switch (e.getButton())
					{
					// left button pressed
						case MouseEvent.BUTTON1:
							if (occupier == null)
								return;
							if (occupier.getClass() == Ship.class)
								Settings.PANEL_PREPARE.shipClick((Ship) occupier);
							break;

						// Right button pressed
						case MouseEvent.BUTTON3:
							panelPrepare.rotate();
							break;

					}
				}
				else
				{
					if (!map.isLocal())
					{
						if (e.getButton() == MouseEvent.BUTTON1)
							panelPlay.shoot(Box.this);
					}
					else
					{
						if (occupier instanceof Ship)
							Settings.PANEL_PLAY.select((Ship) occupier, e.getButton());
						else if (occupier instanceof ShipControl)
							((ShipControl) occupier).execute();
					}
				}
			}
		});

		addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{

			}
		});
	}

	/**
	 * Set size of box from map size
	 * 
	 * @param size New size of box
	 */
	public void setSizeFromMap(int size)
	{
		this.size = size;
		this.sizePreferred = new Dimension(size, size);
		this.setPreferredSize(this.sizePreferred);
		this.setMaximumSize(this.sizePreferred);
	}

	/**
	 * External call method for ennemy's shoot on this box
	 */
	public void shoot()
	{
		if (discovered)
			return;
		discovered = true;

		if (occupier != null)
		{
			imageOverlay = ImageShop.STATE_SHOT;
			occupier.shoot(this);
		}
		else
			imageOverlay = null;

		repaint();
	}

	/**
	 * Set the current occupier of the box
	 * 
	 * @param occupier Element that really occupied the box
	 * @param image Correct image to display in the box to represent part of occupier
	 */
	public void setOccupier(MapElement occupier, Image image)
	{
		this.occupier = occupier;
		this.imageOccupier = image;
		update(getGraphics());
	}

	/**
	 * Get the current occupier of the box
	 * 
	 * @return MapElement occupier
	 */
	public MapElement getOccupier()
	{
		return this.occupier;
	}

	/**
	 * Getter for horizontal coordinate
	 * 
	 * @return Horizontal coordinate (left-side is 0)
	 */
	public int getCoordX()
	{
		return this.x;
	}

	/**
	 * Getter for vertical coordinate
	 * 
	 * @return Vertical coordinate (top-side is 0)
	 */
	public int getCoordY()
	{
		return this.y;
	}

	/**
	 * Getter for current box type
	 * 
	 * @return Type of box displayed (surface, submarine, atlantis)
	 */
	public MapType getMapType()
	{
		return this.type;
	}

	/**
	 * Getter for map in which this box is placed
	 */
	public Map getMap()
	{
		return this.map;
	}

	/**
	 * Set the image for displaying occupier of box
	 * 
	 * @param img New image
	 */
	protected void setImage(Image img)
	{
		this.imageOccupier = img;
		update(getGraphics());
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(this.imageBox, 0, 0, this.size + 1, this.size + 1, null);

		if (map.isLocal() && map.getType() != MapType.ATLANTIS)
			g2d.drawImage(this.imageOccupier, 0, 0, this.size + 1, this.size + 1, null);
		else
		{
			if (discovered && this.imageOccupier != null)
			{
				if (occupier instanceof Ship && occupier.getRemainingSize() > 0)
					g2d.drawImage(this.imageOverlay, 0, 0, this.size + 1, this.size + 1, null);
				else
					g2d.drawImage(this.imageOccupier, 0, 0, this.size + 1, this.size + 1, null);
			}
			else
				g2d.drawImage(this.imageOverlay, 0, 0, this.size + 1, this.size + 1, null);
		}
	}

	@Override
	public String toJSONString()
	{
		JSONObject jo = new JSONObject();
		jo.put("x", x);
		jo.put("y", y);
		return jo.toString();
	}

	/**
	 * Check if this box has been discovered
	 * 
	 * @return True if box is discovered, else false
	 */
	public boolean isDiscovered()
	{
		return discovered;
	}

}
