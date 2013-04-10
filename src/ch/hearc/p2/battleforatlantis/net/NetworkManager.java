package ch.hearc.p2.battleforatlantis.net;

public class NetworkManager
{

	private static NetworkManager instance;
	private Host host;
	private NetworkMessage networkMessage;

	private NetworkManager()
	{

	}

	public static NetworkManager getInstance()
	{
		return null;
	}

	public boolean send(NetworkMessage message)
	{
		return false;
	}

	public void setHost(Host host)
	{

	}

}
