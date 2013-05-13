package ch.hearc.p2.battleforatlantis.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.logging.Logger;

public class Host
{
	private String name;
	private InetAddress address;
	private UUID uuid;
	
	/**
	 * Create a new Host object representing a player
	 * @param name The player's name
	 * @param address His IP address
	 * @param uuid His assigned UUID
	 */
	public Host(String name, InetAddress address, UUID uuid)
	{
		this.name = name;
		this.address = address;
		this.uuid = uuid;
	}
	
	/**
	 * Creates a Host object representing the local host.
	 * @param name The local player's name
	 */
	public Host(String name)
	{
		this.name = name;
		try
		{
			this.address = InetAddress.getLocalHost();
		}
		catch (UnknownHostException e)
		{
			Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
			log.severe("No localhost address !\n" + e.toString());
		}
		this.uuid = UUID.randomUUID();
	}

	public String getName()
	{
		return name;
	}
	
	// Only for local player !
	public void setName(String name)
	{
		if(name != null && !"".equals(name))
		{
			this.name = name;
			NetworkManager.getInstance().autodiscover.refreshPlayerName();
		}
	}

	public InetAddress getAddress()
	{
		return address;
	}

	public UUID getUuid()
	{
		return uuid;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Host)
			return equals((Host)obj);
		else
			return false;
	}

	public boolean equals(Host h)
	{
		return uuid.equals(h.uuid);
	}
	
	@Override
	public int hashCode()
	{
		return uuid.hashCode();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Host [name=");
		builder.append(name);
		builder.append(", address=");
		builder.append(address);
		builder.append(", uuid=");
		builder.append(uuid);
		builder.append("]");
		return builder.toString();
	}
}
