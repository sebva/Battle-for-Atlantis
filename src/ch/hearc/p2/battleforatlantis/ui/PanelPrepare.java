package ch.hearc.p2.battleforatlantis.ui;

import javax.swing.JPanel;

import ch.hearc.p2.battleforatlantis.gameengine.Box;
import ch.hearc.p2.battleforatlantis.gameengine.Ship;

public class PanelPrepare extends JPanel
{
	private FrameMain rootFrame;
	private Ship selectedShip;

	public PanelPrepare(FrameMain rootFrame)
	{
		this.rootFrame = rootFrame;
	}

	public void select(Ship ship)
	{

	}

	public void place(Box location)
	{

	}

	public void rotate()
	{

	}

	public void start()
	{
		rootFrame.startGame();
	}

}
