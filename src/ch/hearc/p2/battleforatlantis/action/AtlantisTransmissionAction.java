package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.gameengine.Atlantis;
import ch.hearc.p2.battleforatlantis.gameengine.Box;
import ch.hearc.p2.battleforatlantis.gameengine.Generator;
import ch.hearc.p2.battleforatlantis.gameengine.Map;
import ch.hearc.p2.battleforatlantis.gameengine.MapType;
import ch.hearc.p2.battleforatlantis.gameengine.Player;
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
		Map atlantisMap = Settings.FRAME_MAIN.getMapByType(MapType.ATLANTIS, Player.LOCAL);
		
		// Row number to load Atlantis parts images
		int row = 0;
		
		// Run through the atlantis width
		for (int y = this.atlantis.getPositionY(); y < this.atlantis.getPositionY() + Settings.ATLANTIS_HEIGHT; y++)
		{
			// Column number to load Atlantis parts images
			int col = 0;
			
			// Run through the atlantis height
			for (int x = this.atlantis.getPositionX(); x < this.atlantis.getPositionX() + Settings.ATLANTIS_WIDTH; x++)
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
	 * Make the AtlantisTransmission action from a request received by the player
	 * 
	 * @param jo JSON Object received by the player
	 * @return The AtlantisTransmissionAction corresponding to the request
	 */
	public static AtlantisTransmissionAction createFromJson(JSONObject jo) 
	{
		// Check if it's the good packet to compute
		assert "atlantisTransmission".equals(jo.get("action")) : "Packet wrongly routed";
		
		// Get global JSON object for retrieving Atlantis configuration
		JSONObject atlantisInformation = jo.getJSONObject("atlantis");
		
		// Get position X and Y for Atlantis in the Map
		int positionX = atlantisInformation.getJSONObject("northWest").getInt("x");
		int positionY = atlantisInformation.getJSONObject("northWest").getInt("y");
		
		try
		{
			// Create Atlantis with user game configuration
			Atlantis atlantis = new Atlantis(
					Settings.ATLANTIS_WIDTH, 
					Settings.ATLANTIS_HEIGHT, 
					AtlantisCreator.getMap()
			);
			
			// Set position with retrieved JSON information
			atlantis.setPosition(positionX, positionY);
			
			Generator generator = new Generator(AtlantisCreator.getMap().getBox(atlantisInformation.getJSONObject("generator")), atlantis);
			atlantis.setGenerator(generator);
			
			
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
	 * Create a JSON Object to communicate the Atlantis to the opposing player
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
		generator.put("y", atlantis.getGenerator().getPositionY());
		
		// Add the coordinates to the message to transmit
		atlantisJo.put("northWest", northWest);
		atlantisJo.put("generator", generator);
		
		jo.put("atlantis", atlantisJo);
		return jo;
	}

}
