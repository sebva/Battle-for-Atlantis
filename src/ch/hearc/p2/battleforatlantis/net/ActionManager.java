package ch.hearc.p2.battleforatlantis.net;

import java.net.InetAddress;
import java.util.UUID;
import java.util.logging.Logger;

import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.action.AtlantisTransmissionAction;
import ch.hearc.p2.battleforatlantis.action.EndGameAction;
import ch.hearc.p2.battleforatlantis.action.MoveAction;
import ch.hearc.p2.battleforatlantis.action.NextLevelAction;
import ch.hearc.p2.battleforatlantis.action.ShootAction;
import ch.hearc.p2.battleforatlantis.action.StartGameAction;
import ch.hearc.p2.battleforatlantis.gameengine.Atlantis;
import ch.hearc.p2.battleforatlantis.gameinit.AtlantisCreator;
import ch.hearc.p2.battleforatlantis.utils.Settings;

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
	 * 
	 * @param jo A JSONObject containing at least the action and uuid fields
	 * @param from The IP address from which this action originates
	 */
	public void executeAction(JSONObject jo, InetAddress from)
	{
		if (!jo.has("action") || !jo.has("uuid"))
		{
			log.warning("The JSONObject received does not have the action or uuid attribute");
			return;
		}
		// We sent that packet
		if (UUID.fromString(jo.getString("uuid")).equals(nw.localhost.getUuid()))
			return;

		log.info("Received : " + jo.toString(2));
		// Routing the packet to the correct object
		switch (jo.getString("action"))
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
			case "tryconnect":
				if (jo.getString("hashConfig").equals(Settings.FRAME_MAIN.getHashConfig()))
				{
					Host h = new Host(jo.getString("playerName"), from, UUID.fromString(jo.getString("uuid")));
					Settings.FRAME_MAIN.connectionAttempt(h);
				}
				break;
			case "connectionResponse":
				Host h = new Host(jo.getString("playerName"), from, UUID.fromString(jo.getString("uuid")));
				if (jo.getBoolean("connectionAccepted"))
				{
					Settings.FRAME_MAIN.placeShips(h);
					nw.startTcpConnection(h);
				}
				else
					Settings.FRAME_MAIN.connectionRefused(h);
				break;
			case "readyToPlay":
				try
				{
					Atlantis atlantis = AtlantisCreator.generateAtlantis(Settings.ATLANTIS_WIDTH, Settings.ATLANTIS_HEIGHT);
					AtlantisTransmissionAction atlantisTransmissionAction = new AtlantisTransmissionAction(atlantis);
					atlantisTransmissionAction.execute();
					nw.send(atlantisTransmissionAction);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				break;
			case "atlantisTransmission":
				AtlantisTransmissionAction.createFromJson(jo).execute();
				break;
			case "shipsPlacement":
				StartGameAction.createFromJson(jo).execute();
				break;
			case "fire":
				ShootAction.createFromJson(jo).execute();
				break;
			case "move":
				MoveAction.createFromJson(jo).execute();
				break;
			case "nextLevel":
				NextLevelAction.createFromJson(jo).execute();
				break;
			case "endGame":
				EndGameAction.createFromJson(jo).execute();
				break;
		}
	}
}
