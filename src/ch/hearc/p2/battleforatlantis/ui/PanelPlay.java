package ch.hearc.p2.battleforatlantis.ui;

import javax.swing.JPanel;

import ch.hearc.p2.battleforatlantis.gameengine.Box;
import ch.hearc.p2.battleforatlantis.gameengine.Map;
import ch.hearc.p2.battleforatlantis.gameengine.MapType;
import ch.hearc.p2.battleforatlantis.gameengine.Player;
import ch.hearc.p2.battleforatlantis.gameengine.Ship;

public class PanelPlay extends JPanel
{
	private Map levelMe;
	private Map levelOther;
	
	private FrameMain rootFrame;

	public PanelPlay(FrameMain rootFrame)
	{
		this.rootFrame = rootFrame;
	}

	public void shoot(Box location)
	{

	}

	public void select(Ship ship)
	{

	}

	public void rotate(boolean clockwise)
	{

	}

	public void place(boolean forward)
	{

	}

	public void nextLevel()
	{

	}

	public void endGame(boolean isWinner)
	{
		DialogEndGame.announceGameResult(this, isWinner);
		rootFrame.endGame();
	}

	public void setActiveMap(Player pt, MapType map)
	{

	}

}
