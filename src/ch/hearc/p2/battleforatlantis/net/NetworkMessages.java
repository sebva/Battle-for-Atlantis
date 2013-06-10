package ch.hearc.p2.battleforatlantis.net;

import org.json.JSONObject;

public class NetworkMessages
{
	public static NetworkMessage tryConnect(final String uuid, final String hashConfig, final String playerName)
	{
		return new NetworkMessage()
		{

			@Override
			public JSONObject getJson()
			{
				JSONObject jo = new JSONObject();
				jo.put("action", "tryconnect");
				jo.put("uuid", uuid);
				jo.put("hashConfig", hashConfig);
				jo.put("playerName", playerName);

				return jo;
			}
		};
	}

	public static NetworkMessage connectionResponse(final String uuid, final boolean accepted, final String playerName)
	{
		return new NetworkMessage()
		{

			@Override
			public JSONObject getJson()
			{
				JSONObject jo = new JSONObject();
				jo.put("action", "connectionResponse");
				jo.put("uuid", uuid);
				jo.put("connectionAccepted", accepted);
				jo.put("playerName", playerName);

				return jo;
			}
		};
	}

	public static NetworkMessage readyToPlay(final String uuid)
	{
		return new NetworkMessage()
		{

			@Override
			public JSONObject getJson()
			{
				JSONObject jo = new JSONObject();
				jo.put("action", "readyToPlay");
				jo.put("uuid", uuid);

				return jo;
			}
		};
	}
}
