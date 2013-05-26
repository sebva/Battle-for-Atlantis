package ch.hearc.p2.battleforatlantis.gameengine;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

import ch.hearc.p2.battleforatlantis.gameengine.ShipControl.ShipControlType;
import ch.hearc.p2.battleforatlantis.ui.FrameMain;
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
	 * Preferred dimension of map in pixels
	 */
	private Dimension sizePreferred;

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
	 * Debug logger
	 */
	private Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

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
	}

	/**
	 * Create a map of specified dimensions
	 * 
	 * @param width
	 *            Number of rows
	 * @param height
	 *            Number of columns
	 * @param type
	 *            Type of map (surface, submarine, atlantis)
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
		addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				int numberY = Map.this.height;
				int numberX = Map.this.width;

				int sizeBoxX = Map.this.getWidth() / numberX;
				int sizeBoxY = Map.this.getHeight() / numberY;

				if (sizeBoxX <= Map.this.sizeBox || sizeBoxY <= Map.this.sizeBox)
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
		});

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
	 * @param x
	 *            Horizontal coordinate, starting at 0 on left side
	 * @param y
	 *            Vertical coordinate, starting at 0 on upper side
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
				PanelPrepare panel = FrameMain.getPanelPrepare();
				panel.place(null);
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				PanelPrepare panel = FrameMain.getPanelPrepare();
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
			public void mouseClicked(MouseEvent e)
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
	
	public void addShipControls()
	{
		Set<Ship> ships = new HashSet<>();
		
		for (Box[] line : boxes)
		{
			for (Box box : line)
			{
				MapElement mapElement = box.getOccupier();
				if(mapElement instanceof Ship)
					ships.add((Ship) mapElement);
				else if(mapElement instanceof ShipControl)
				{
					box.setOccupier(null, null);
					mapElement = null;
				}
			}
		}
		
		for (Ship ship : ships)
		{
			if(ship.isTouched())
				continue;
			
			Box[] occupied = ship.getOccupied();
			
			Box first = occupied[0];
			Box last = occupied[occupied.length -1];
			
			Box forwardControl = null, backwardControl = null;
			
			switch(ship.getOrientation())
			{
				case EAST:
					forwardControl = getBox(last.getCoordX() +1, last.getCoordY());
					backwardControl = getBox(first.getCoordX() -1, first.getCoordY());
					break;
				case NORTH:
					forwardControl = getBox(last.getCoordX(), last.getCoordY() -1);
					backwardControl = getBox(first.getCoordX(), first.getCoordY() +1);
					break;
				case SOUTH:
					forwardControl = getBox(last.getCoordX(), last.getCoordY() +1);
					backwardControl = getBox(first.getCoordX(), first.getCoordY() -1);
					break;
				case WEST:
					forwardControl = getBox(last.getCoordX() -1, last.getCoordY());
					backwardControl = getBox(first.getCoordX() +1, first.getCoordY());
					break;
			}
			
			if(forwardControl != null && forwardControl.getOccupier() == null)
				new ShipControl(ship, forwardControl, ShipControlType.PLACE_FORWARD);
			if(backwardControl != null && backwardControl.getOccupier() == null)
				new ShipControl(ship, backwardControl, ShipControlType.PLACE_BACKWARD);
		}
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
			if(localmap.type.equals(type))
				map = new Map(localmap.width, localmap.height, type, false);
		}
		
		JSONArray ships = jo.getJSONArray("ships");
		for(int i = 0; i < ships.length(); i++)
		{
			JSONObject jsonShip = ships.getJSONObject(i);
			JSONObject jsonCenter = jsonShip.getJSONObject("center");
			int x = jsonCenter.getInt("x");
			int y = jsonCenter.getInt("y");
			
			Settings.FRAME_MAIN.getDistantShips().add(Ship.createFromJSONObject(jsonShip, map.boxes[y][x]));
		}
		
		return map;
	}
}
