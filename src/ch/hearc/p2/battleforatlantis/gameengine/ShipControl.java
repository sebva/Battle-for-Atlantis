package ch.hearc.p2.battleforatlantis.gameengine;

import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.Settings;

public class ShipControl extends MapElement
{
	/**
	 * Ship to control
	 */
	private Ship ship;

	/**
	 * Type of ship control
	 */
	private ShipControlType type;

	/**
	 * Center of ship to control
	 */
	private Box center;

	/**
	 * List of types
	 */
	public enum ShipControlType
	{
		PLACE_FORWARD, PLACE_BACKWARD
	}

	/**
	 * Default constructor
	 * @param ship Ship to control
	 * @param box Center of ship to control
	 * @param type Type of control to do
	 */
	public ShipControl(Ship ship, Box box, ShipControlType type)
	{
		super(1);

		this.ship = ship;
		this.type = type;
		this.center = box;

		super.occupied[0] = box;
		ShipOrientation imageOrientation = ship.getOrientation();
		if (type == ShipControlType.PLACE_BACKWARD)
			imageOrientation = ShipOrientation.opposite(imageOrientation);
		super.images[0] = ImageShop.loadPlaceArrow(imageOrientation);

		center.setOccupier(this, ImageShop.loadPlaceArrow(imageOrientation));
	}

	/**
	 * Execute the control
	 */
	public void execute()
	{
		Settings.PANEL_PLAY.place(ship, type == ShipControlType.PLACE_FORWARD);
	}

	/**
	 * Get ship controlled
	 * @return Ship controlled
	 */
	public Ship getAssociatedShip()
	{
		return ship;
	}

	@Override
	public void shoot(Box target)
	{
		// Nothing
	}

	@Override
	protected void setCurrentSize(int width, int height)
	{
		// Nothing
	}

}
