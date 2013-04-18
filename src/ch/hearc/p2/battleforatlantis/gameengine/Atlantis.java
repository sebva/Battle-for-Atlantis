package ch.hearc.p2.battleforatlantis.gameengine;

public class Atlantis extends MapElement
{

	private String images;
	private boolean destroyable;

	public Atlantis()
	{
		// TODO: set correct size
		super(1);
	}

	public void generatorDestroyed()
	{

	}

	public boolean isDestroyable()
	{
		return false;
	}

}
