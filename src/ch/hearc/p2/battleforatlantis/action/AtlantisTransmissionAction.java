package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.gameengine.Atlantis;
import ch.hearc.p2.battleforatlantis.gameengine.Box;
import ch.hearc.p2.battleforatlantis.gameengine.Map;
import ch.hearc.p2.battleforatlantis.gameengine.MapType;
import ch.hearc.p2.battleforatlantis.gameinit.AtlantisCreator;
import ch.hearc.p2.battleforatlantis.utils.ImageShop;
import ch.hearc.p2.battleforatlantis.utils.Settings;

public class AtlantisTransmissionAction extends Action
{
	/**
	 * Atlantis received by network
	 */
	private Atlantis atlantis;
	
	/**
	 * Action name to find the good transmission in network
	 */
	public static final String ACTION_NAME = "atlantisTransmission";
	
	/**
	 * New transmission of the Atlantis Object
	 * 
	 * @param atlantis Atlantis object received
	 */
	public AtlantisTransmissionAction(Atlantis atlantis)
	{
		this.atlantis = atlantis;
	}

	/**
	 * Execute the action after received the Atlantis
	 */
	@Override
	public void execute()
	{
		// Retrieve Atlantis Map
		Map atlantisMap = Settings.FRAME_MAIN.getMapByType(MapType.ATLANTIS, true);
		
		// Row and column number to load Atlantis parts images
		int row = 0;
		int col = 0;
		
		// Run through the atlantis width
		for (int x = this.atlantis.getPositionX(); x < this.atlantis.getPositionX() + Settings.ATLANTIS_WIDTH; x++)
		{
			// Run through the atlantis height
			for (int y = this.atlantis.getPositionY(); y < this.atlantis.getPositionY() + Settings.ATLANTIS_HEIGHT; y++)
			{
				// Get box and set atlantis as occupier
				Box actualBox = atlantisMap.getBox(x, y);
				actualBox.setOccupier(atlantis, ImageShop.loadAtlantisImage(row, col, true));
				
				// Next column
				col++;
			}
			
			// Next line
			row++;
		}
	}

	/**
	 * Create new AtlantisTransmissionAction with Atlantis received
	 * 
	 * @param jo JSON Object
	 * @return AtlantisTransmissionAction Object
	 */
	public static AtlantisTransmissionAction createFromJson(JSONObject jo) 
	{
		// Check if it's the good packet to compute
		assert "atlantisTransmission".equals(jo.get("action")) : "Packet wrongly routed";
		
		// Get global JSON object for retrieving Atlantis configuration
		JSONObject atlantisInformation = jo.getJSONObject("atlantis");
		
		// Get position X and Y for Atlantis in the Map
		int positionX = atlantisInformation.getJSONObject("northWest").getInt("x");
		int positionY = atlantisInformation.getJSONObject("northWest").getInt("y");;
		
		Atlantis atlantis;
		try
		{
			// Create Atlantis with user game configuration
			atlantis = new Atlantis(
					Settings.ATLANTIS_WIDTH, 
					Settings.ATLANTIS_HEIGHT, 
					AtlantisCreator.getMap().getWidth(), 
					AtlantisCreator.getMap().getHeight()
			);
			
			// Set position with retrieved JSON information
			atlantis.setPosition(positionX, positionY);
			
			// Return this Action class with Atlantis configured
			return new AtlantisTransmissionAction(atlantis);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * @return 
	 */
	@Override
	public JSONObject getJson()
	{
		// Create a JSON object for Atlantis Transmission
		JSONObject jo = new JSONObject();
		jo.put("action", "atlantisTransmission");
		JSONObject atlantisJo = new JSONObject();
		
		// Indicates coordinates for Atlantis
		JSONObject northWest = new JSONObject();
		northWest.put("x", atlantis.getPositionX());
		northWest.put("y", atlantis.getPositionY());
		
		// Indicates coordinates for the Shield Generator
		JSONObject generator = new JSONObject();
		generator.put("x", atlantis.getGenerator().getPositionX());
		generator.put("x", atlantis.getGenerator().getPositionY());
		
		// Add the coordinates to the message to transmit
		atlantisJo.put("northWest", northWest);
		atlantisJo.put("generator", generator);
		
		jo.put("atlantis", atlantisJo);
		return jo;
	}

}
