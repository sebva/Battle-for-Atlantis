package ch.hearc.p2.battleforatlantis.net;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.json.JSONObject;

public class NetworkAutodiscover
{
	public interface NetworkAutodiscoverListener
	{
		public void hostAppeared(Host host);

		public void hostDisappeared(Host host);
	}

	private class AutodiscoverMessage implements NetworkMessage
	{
		private final JSONObject jo;

		public AutodiscoverMessage()
		{
			jo = new JSONObject();
			jo.put("action", "autodiscover");
			refreshName();
		}

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

	private final AutodiscoverMessage kAutoDiscoverMessage;
	public static final int DELAY = 3000;
	private NetworkAutodiscoverListener listener = null;
	private Map<Host, Integer> knownHosts;
	private boolean discovering = false;
	private NetworkManager nw;

	public NetworkAutodiscover(NetworkManager nw)
	{
		this.nw = nw;
		kAutoDiscoverMessage = new AutodiscoverMessage();
		knownHosts = new HashMap<Host, Integer>();
	}

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

	private void stopDiscovering()
	{
		discovering = false;
	}

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

	protected void setListener(NetworkAutodiscoverListener nal)
	{
		listener = nal;
		// Not necessary if the only listener is PanelConnection
		// knownHosts.clear();
		startDiscovering();
	}

	protected void removeListener(NetworkAutodiscoverListener nal)
	{
		if (nal == listener)
			listener = null;
		stopDiscovering();
	}

	protected void refreshPlayerName()
	{
		kAutoDiscoverMessage.refreshName();
	}

	public Host[] getKnownHosts()
	{
		return knownHosts.keySet().toArray(null);
	}
}
