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
	private int width;
	private int height;

	private MapType type;

	Box[][] boxes;

	private MouseListener preparationListener;
	private MouseListener gameListener;
	
	private Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * Create a map of specified dimensions
	 * 
	 * @param width
	 *            Number of rows
	 * @param height
	 *            Number of columns
	 */
	public Map(int width, int height, MapType type)
	{
		this.width = width;
		this.height = height;
		this.type = type;

		boxes = new Box[height][width];
		setLayout(new GridLayout(height, width, 0, 0));

		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				boxes[i][j] = new Box(this, this.type, j, i);
				add(boxes[i][j]);
			}
		}

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
				switch(e.getButton())
				{
					// left button pressed
					case MouseEvent.BUTTON1:
						log.info("bouton gauche");
						break;
						
					// Right button pressed
					case MouseEvent.BUTTON3:
						PanelPrepare panel = FrameMain.getPanelPrepare();
						panel.rotate();
						break;
				}
			}
		};

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
	public MapType getType()
	{
		return type;
	}
}
