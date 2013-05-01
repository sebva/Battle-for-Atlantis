package ch.hearc.p2.battleforatlantis.net;

import java.net.InetAddress;
import java.util.UUID;
import java.util.logging.Logger;

import org.json.JSONObject;

public class ActionManager
{
	private Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private NetworkManager nw;

	public ActionManager(NetworkManager nw)
	{
		this.nw = nw;
	}

	/**
	 * Execute the action described in the JSONObject
	 * @param jo A JSONObject containing at least the action and uuid fields
	 * @param from The IP address from which this action originates
	 */
	public void executeAction(JSONObject jo, InetAddress from)
	{
		if(!jo.has("action") || !jo.has("uuid"))
		{
			log.warning("The JSONObject received does not have the action or uuid attribute");
			return;
		}
		// We sent that packet
		if(UUID.fromString(jo.getString("uuid")).equals(nw.localhost.getUuid()))
			return;
		
		// Routing the packet to the correct object
		switch(jo.getString("action"))
		{
			case "autodiscover":
				try
				{
					nw.autodiscover.packetReceived(jo, from);
				}
				catch (Exception e)
				{
					log.warning(e.toString());
				}
				break;
		}
	}

}
