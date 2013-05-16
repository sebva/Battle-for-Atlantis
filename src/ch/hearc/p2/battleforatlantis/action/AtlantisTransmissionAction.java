package ch.hearc.p2.battleforatlantis.action;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.gameengine.Atlantis;

public class AtlantisTransmissionAction extends Action
{
	private Atlantis atlantis;
	public static final String ACTION_NAME = "atlantisTransmission";
	
	public AtlantisTransmissionAction(Atlantis atlantis)
	{
		this.atlantis = atlantis;
	}

	@Override
	public void execute()
	{

	}

	public static AtlantisTransmissionAction createFromJson(JSONObject jo)
	{
		return null;
	}

	@Override
	public JSONObject getJson()
	{
		JSONObject jo = new JSONObject();
		jo.put("action", "atlantisTransmission");
		JSONObject atlantisJo = new JSONObject();
		
		JSONObject northWest = new JSONObject();
		northWest.put("x", atlantis.getPositionX());
		northWest.put("y", atlantis.getPositionY());
		
		JSONObject generator = new JSONObject();
		generator.put("x", atlantis.getGenerator().getPositionX());
		generator.put("x", atlantis.getGenerator().getPositionY());
		
		atlantisJo.put("northWest", northWest);
		atlantisJo.put("generator", generator);
		
		jo.put("atlantis", atlantisJo);
		return jo;
	}

}
