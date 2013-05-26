package ch.hearc.p2.battleforatlantis.gameengine;

import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.Settings;

public class ShipControl extends MapElement
{
	private Ship ship;
	private ShipControlType type;
	private Box center;

	public enum ShipControlType
	{
		PLACE_FORWARD, PLACE_BACKWARD
	}

	public ShipControl(Ship ship, Box box, ShipControlType type)
	{
		super(1);
		
		this.ship = ship;
		this.type = type;
		this.center = box;
		
		super.occupied[0] = box;
		ShipOrientation imageOrientation = ship.getOrientation();
		if(type == ShipControlType.PLACE_BACKWARD)
			imageOrientation = ShipOrientation.opposite(imageOrientation);
		super.images[0] = ImageShop.loadPlaceArrow(imageOrientation);
		
		center.setOccupier(this, ImageShop.loadPlaceArrow(imageOrientation));
	}
	
	public void execute()
	{
		Settings.PANEL_PLAY.place(ship, type == ShipControlType.PLACE_FORWARD);
	}
	
	public Ship getAssociatedShip()
	{
		return ship;
	}

	@Override
	public void shoot(Box target)
	{
		// Niet
	}

	@Override
	protected void setCurrentSize(int width, int height)
	{
		// Niet
	}

}
