package ch.hearc.p2.battleforatlantis.net;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.json.JSONObject;

/**
 * Autodiscover management for network connections
 */
public class NetworkAutodiscover
{
	/**
	 * Interface for listeners available
	 */
	public interface NetworkAutodiscoverListener
	{
		public void hostAppeared(Host host);

		public void hostDisappeared(Host host);
	}

	/**
	 * Message sent for autodiscover requests
	 */
	private class AutodiscoverMessage implements NetworkMessage
	{
		/** JSON object to send */
		private final JSONObject jo;

		/**
		 * Default contructor
		 */
		public AutodiscoverMessage()
		{
			jo = new JSONObject();
			jo.put("action", "autodiscover");
			refreshName();
		}

		/**
		 * Refresh the name of player
		 */
		public void refreshName()
		{
			jo.put("playerName", nw.localhost.getName());
		}

		@Override
		public JSONObject getJson()
		{
			return jo;
		}
	}

	/** Autodiscover type message */
	private final AutodiscoverMessage kAutoDiscoverMessage;

	/** Delay between two autodiscovers */
	public static final int DELAY = 3000;

	/** Listener for reception of autodiscover responses */
	private NetworkAutodiscoverListener listener = null;

	/** List of already discovered hosts */
	private Map<Host, Integer> knownHosts;

	/** Indicator of discover state */
	private boolean discovering = false;

	/** Manager used to communicate over network */
	private NetworkManager nw;

	/**
	 * Default constructor
	 * 
	 * @param nw Network manager used for communications
	 */
	public NetworkAutodiscover(NetworkManager nw)
	{
		this.nw = nw;
		kAutoDiscoverMessage = new AutodiscoverMessage();
		knownHosts = new HashMap<Host, Integer>();
	}

	/**
	 * Starts the discovering thread
	 */
	private void startDiscovering()
	{
		if (discovering)
			return;

		discovering = true;
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				while (discovering)
				{
					nw.broadcast(kAutoDiscoverMessage);
					try
					{
						Thread.sleep(DELAY);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}, "Autodiscover sending thread").start();
		// Starting the cleaning Thread
		new Thread(new Runnable()
		{
			private static final int CLEANING_DELAY = 2100;

			@Override
			public void run()
			{
				while (discovering)
				{
					for (Entry<Host, Integer> entry : knownHosts.entrySet())
					{
						int newValue = entry.getValue() - CLEANING_DELAY;
						if (newValue > 0)
							entry.setValue(newValue);
						else
						{
							Host removed = entry.getKey();
							knownHosts.remove(removed);
							listener.hostDisappeared(removed);
						}
					}

					try
					{
						Thread.sleep(CLEANING_DELAY);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}, "Autodiscover cleaning thread").start();
	}

	/**
	 * Stop the discovering thread
	 */
	private void stopDiscovering()
	{
		discovering = false;
	}

	/**
	 * Reception of response packet
	 * 
	 * @param jo JSON object received
	 * @param from INET address from sender
	 * @throws Exception Packet is not correct
	 */
	protected void packetReceived(JSONObject jo, InetAddress from) throws Exception
	{
		// If no one listens, we don't need to do anything
		if (listener == null)
			return;

		// Checking data integrity
		assert "autodiscover".equals(jo.get("action")) : "Packet wrongly routed";
		if (!jo.has("playerName"))
			throw new Exception("Data missing in packet");

		String name = jo.getString("playerName");
		UUID uuid = UUID.fromString(jo.getString("uuid"));
		final Host h = new Host(name, from, uuid);

		if (!knownHosts.containsKey(h))
		{
			listener.hostAppeared(h);
		}
		knownHosts.put(h, DELAY * 2);
	}

	/**
	 * Set the listener for listening to responses
	 * 
	 * @param nal Listener to set
	 */
	protected void setListener(NetworkAutodiscoverListener nal)
	{
		listener = nal;
		// Not necessary if the only listener is PanelConnection
		// knownHosts.clear();
		startDiscovering();
	}

	/**
	 * Remove the listener
	 * 
	 * @param nal Listener to remove
	 */
	protected void removeListener(NetworkAutodiscoverListener nal)
	{
		if (nal == listener)
			listener = null;
		stopDiscovering();
	}

	/**
	 * Refresh player name in discovering messages
	 */
	protected void refreshPlayerName()
	{
		kAutoDiscoverMessage.refreshName();
	}

	/**
	 * Get the list of discovered hosts
	 * 
	 * @return List of hosts
	 */
	public Host[] getKnownHosts()
	{
		return knownHosts.keySet().toArray(null);
	}
}
