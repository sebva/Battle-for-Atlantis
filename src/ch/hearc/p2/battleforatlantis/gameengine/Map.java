package ch.hearc.p2.battleforatlantis.gameengine;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

import ch.hearc.p2.battleforatlantis.gameengine.ShipControl.ShipControlType;
import ch.hearc.p2.battleforatlantis.ui.PanelPrepare;
import ch.hearc.p2.battleforatlantis.utils.Settings;

public class Map extends JPanel implements JSONString
{
	/**
	 * Number of columns
	 */
	private int width;

	/**
	 * Number of lines
	 */
	private int height;

	/**
	 * Size of boxes
	 */
	private int sizeBox;

	/**
	 * Map type (surface, submarine, atlantis)
	 */
	private MapType type;

	/**
	 * Array of boxes which compose the map, in order [line, column]
	 */
	private Box[][] boxes;

	/**
	 * Listener for click on boxes in preparation panel
	 */
	private MouseListener preparationListener;

	/**
	 * Listener for click on boxes in game panel
	 */
	private MouseListener gameListener;

	/**
	 * Internal panel
	 */
	private InternalPanel internalPanel;

	/**
	 * Indicated if this map is a local map. For Atlantis, it should set to true
	 */
	private boolean isLocal;

	/**
	 * Internal panel for resizing abilities
	 */
	private class InternalPanel extends JPanel
	{
		public InternalPanel()
		{
			setLayout(new GridLayout(height, width, 0, 0));
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Map.this.resizeComponent();
		}
	}

	/**
	 * Create a map of specified dimensions
	 * 
	 * @param width Number of rows
	 * @param height Number of columns
	 * @param type Type of map (surface, submarine, atlantis)
	 */
	public Map(int width, int height, MapType type, boolean isLocal)
	{
		// Input fields
		this.width = width;
		this.height = height;
		this.type = type;
		this.isLocal = isLocal;

		// Sizes definition
		this.setMaximumSize(new Dimension(60 * width, 60 * height));
		this.setMinimumSize(new Dimension(30 * width, 30 * height));
		this.setPreferredSize(new Dimension(60 * width, 60 * height));
		this.sizeBox = 60;

		this.internalPanel = new InternalPanel();
		setLayout(new BorderLayout(0, 0));
		add(this.internalPanel, BorderLayout.NORTH);
		this.internalPanel.setPreferredSize(new Dimension(60 * width, 60 * height));
		this.internalPanel.setMaximumSize(new Dimension(60 * width, 60 * height));
		this.internalPanel.setMinimumSize(new Dimension(30 * width, 30 * height));

		// Prepare boxes lists (internal and display)
		boxes = new Box[height][width];
		// setLayout(new GridLayout(height, width, 0, 0));

		// Create and add boxes
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				boxes[i][j] = new Box(this, this.type, j, i);
				this.internalPanel.add(boxes[i][j]);
			}
		}

		// Instanciate listeners
		instanciatePreparationListener();
		instanciateGameListener();

		/*
		 * addComponentListener(new ComponentAdapter() {
		 * 
		 * @Override public void componentResized(ComponentEvent e) { resizeComponent(); } });
		 */

	}

	/**
	 * Register boxes for preparation actions
	 */
	public void setPreparationListeners()
	{
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				boxes[i][j].removeMouseListener(gameListener);
				boxes[i][j].addMouseListener(preparationListener);
			}
		}
	}

	/**
	 * Register boxes for game actions
	 */
	public void setGameListeners()
	{
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				boxes[i][j].removeMouseListener(preparationListener);
				boxes[i][j].addMouseListener(gameListener);
			}
		}
	}

	/**
	 * Get the box at specified coordinates (top-left is 0;0)
	 * 
	 * @param x Horizontal coordinate, starting at 0 on left side
	 * @param y Vertical coordinate, starting at 0 on upper side
	 * 
	 * @return Box at location, or null if incorrect location
	 */
	public Box getBox(int x, int y)
	{
		if ((x >= 0) && (x < this.width) && (y >= 0) && (y < this.height))
		{
			return this.boxes[y][x];
		}
		else
		{
			return null;
		}
	}

	/**
	 * Get the box associated by the x and y attributes of the JSONObject
	 * 
	 * @param jo A JSONObject containing x and y
	 * @return Box at location, or null
	 */
	public Box getBox(JSONObject jo)
	{
		return getBox(jo.optInt("x", -1), jo.optInt("y", -1));
	}

	/**
	 * Get the current type of map
	 * 
	 * @return Map type
	 */
	public MapType getType()
	{
		return type;
	}

	/**
	 * Instanciate boxes listener for preparation state
	 */
	private void instanciatePreparationListener()
	{
		preparationListener = new MouseAdapter()
		{
			@Override
			public void mouseExited(MouseEvent e)
			{
				PanelPrepare panel = Settings.PANEL_PREPARE;
				panel.place(null);
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				PanelPrepare panel = Settings.PANEL_PREPARE;
				panel.place((Box) e.getComponent());
			}
		};
	}

	/**
	 * Instanciate boxes listener for game state
	 */
	private void instanciateGameListener()
	{
		gameListener = new MouseAdapter()
		{
			@Override
			public void mouseExited(MouseEvent e)
			{

			}

			@Override
			public void mouseEntered(MouseEvent e)
			{

			}

			@Override
			public void mousePressed(MouseEvent e)
			{

			}
		};
	}

	public int getMapHeight()
	{
		return this.height;
	}

	public int getMapWidth()
	{
		return this.width;
	}

	public boolean isLocal()
	{
		return isLocal;
	}

	private Box[] getControlBoxes(Ship ship)
	{
		Box[] occupied = ship.getOccupied();

		Box first = occupied[0];
		Box last = occupied[occupied.length - 1];

		switch (ship.getOrientation())
		{
			case EAST:
				return new Box[] { getBox(last.getCoordX() + 1, last.getCoordY()), getBox(first.getCoordX() - 1, first.getCoordY()) };
			case NORTH:
				return new Box[] { getBox(last.getCoordX(), last.getCoordY() - 1), getBox(first.getCoordX(), first.getCoordY() + 1) };
			case SOUTH:
				return new Box[] { getBox(last.getCoordX(), last.getCoordY() + 1), getBox(first.getCoordX(), first.getCoordY() - 1) };
			case WEST:
				return new Box[] { getBox(last.getCoordX() - 1, last.getCoordY()), getBox(first.getCoordX() + 1, first.getCoordY()) };
			default:
				return null;
		}
	}

	public void addShipControls(Ship ship)
	{
		if (ship.isTouched())
			return;

		Box[] controlBoxes = getControlBoxes(ship);
		Box forwardControl = controlBoxes[0], backwardControl = controlBoxes[1];

		if (forwardControl != null && forwardControl.getOccupier() == null && !forwardControl.isDiscovered())
			new ShipControl(ship, forwardControl, ShipControlType.PLACE_FORWARD);
		if (backwardControl != null && backwardControl.getOccupier() == null && !backwardControl.isDiscovered())
			new ShipControl(ship, backwardControl, ShipControlType.PLACE_BACKWARD);
	}

	public void removeShipControls(Ship ship)
	{
		Box[] controlBoxes = getControlBoxes(ship);
		Box forwardControl = controlBoxes[0], backwardControl = controlBoxes[1];

		if (forwardControl != null && forwardControl.getOccupier() instanceof ShipControl)
			forwardControl.setOccupier(null, null);
		if (backwardControl != null && backwardControl.getOccupier() instanceof ShipControl)
			backwardControl.setOccupier(null, null);
	}

	@Override
	public String toJSONString()
	{
		JSONObject jo = new JSONObject();
		jo.put("levelName", type.toString());

		Set<Ship> ships = new HashSet<>();
		for (Box[] ext : boxes)
		{
			for (Box box : ext)
			{
				MapElement occupier = box.getOccupier();
				if (occupier instanceof Ship)
					ships.add((Ship) occupier);
			}
		}
		jo.put("ships", new JSONArray(ships));
		return jo.toString();
	}

	public static Map createFromJsonObject(JSONObject jo)
	{
		MapType type = MapType.valueOf(jo.getString("levelName"));

		Map map = null;

		// Determine map size by using local map size
		Map[] localMaps = Settings.FRAME_MAIN.getLocalMaps();
		for (Map localmap : localMaps)
		{
			if (localmap.type.equals(type))
				map = new Map(localmap.width, localmap.height, type, false);
		}

		JSONArray ships = jo.getJSONArray("ships");
		for (int i = 0; i < ships.length(); i++)
		{
			JSONObject jsonShip = ships.getJSONObject(i);
			JSONObject jsonCenter = jsonShip.getJSONObject("center");
			int x = jsonCenter.getInt("x");
			int y = jsonCenter.getInt("y");

			Settings.FRAME_MAIN.getDistantShips().add(Ship.createFromJSONObject(jsonShip, map.boxes[y][x]));
		}

		return map;
	}

	public boolean isFinished()
	{
		int total = 0;

		for (int row = 0; row < height; row++)
		{
			for (int col = 0; col < width; col++)
			{
				MapElement occupier = getBox(col, row).getOccupier();
				if (occupier != null)
					total += occupier.getRemainingSize();
			}
		}

		return total == 0;
	}

	/**
	 * 
	 */
	public void resizeComponent()
	{
		int numberY = Map.this.height;
		int numberX = Map.this.width;

		int sizeBoxX = Map.this.getWidth() / numberX;
		int sizeBoxY = Map.this.getHeight() / numberY;

		if (sizeBoxX <= Map.this.sizeBox || sizeBoxY < Map.this.sizeBox)
		{
			Map.this.sizeBox = (sizeBoxX < sizeBoxY ? sizeBoxX : sizeBoxY);
		}
		else
		{
			Map.this.sizeBox = (sizeBoxX > sizeBoxY ? sizeBoxX : sizeBoxY);
		}

		for (int i = 0; i < numberY; i++)
		{
			for (int j = 0; j < numberX; j++)
			{
				boxes[i][j].setSizeFromMap(Map.this.sizeBox);
			}
		}

		Dimension newDimension = new Dimension(Map.this.sizeBox * numberX, Map.this.sizeBox * numberY);
		Map.this.internalPanel.setPreferredSize(newDimension);
		Map.this.internalPanel.setMaximumSize(newDimension);
	}
}
