package ch.hearc.p2.battleforatlantis.net;

import org.json.JSONObject;

/**
 * Static generation for standard network messages
 */
public class NetworkMessages
{
	/**
	 * Create a network message to try connection to host
	 * 
	 * @param uuid UUID of local host
	 * @param hashConfig Hash of configuration file
	 * @param playerName Name of local player
	 * @return Network message ready to be sent
	 */
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

	/**
	 * Create a network message to respond to a connexion attempt
	 * 
	 * @param uuid UUID of local host
	 * @param accepted True if connection is accepted, else False
	 * @param playerName Name of local player
	 * @return Network message ready to be sent
	 */
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

	/**
	 * Create a network message to indicate that the placement is finished
	 * 
	 * @param uuid UUID of local host
	 * @return Network message ready to be sent
	 */
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
