package ch.hearc.p2.battleforatlantis.gameengine;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Logger;

import javax.swing.JPanel;

import ch.hearc.p2.battleforatlantis.ui.FrameMain;
import ch.hearc.p2.battleforatlantis.ui.PanelPrepare;

public class Map extends JPanel
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
	 * Create a map of specified dimensions
	 * 
	 * @param width Number of rows
	 * @param height Number of columns
	 * @param type Type of map (surface, submarine, atlantis)
	 */
	public Map(int width, int height, MapType type)
	{
		// Input fields
		this.width = width;
		this.height = height;
		this.type = type;

		// Prepare boxes lists (internal and display)
		boxes = new Box[height][width];
		setLayout(new GridLayout(height, width, 0, 0));

		// Create and add boxes
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				boxes[i][j] = new Box(this, this.type, j, i);
				add(boxes[i][j]);
			}
		}

		// Instanciate listeners
		instanciatePreparationListener();
		instanciateGameListener();
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
		if ((x>=0) && (x<this.width) && (y>=0) && (y<this.height))
		{
			return this.boxes[y][x];
		}
		else
		{
			return null;
		}
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
				panel.place((Box)e.getComponent());
			}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				PanelPrepare panel = FrameMain.getPanelPrepare();
				switch(e.getButton())
				{
					// left button pressed
					case MouseEvent.BUTTON1:
						panel.validatePlacement();
						break;
						
					// Right button pressed
					case MouseEvent.BUTTON3:
						panel.rotate();
						break;
				}
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
}
