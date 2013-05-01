package ch.hearc.p2.battleforatlantis.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.net.NetworkAutodiscover.NetworkAutodiscoverListener;

public class NetworkManager
{
	public static final int NETWORK_PORT = 28526;
	public static final int BUFFER_LENGTH = 1000;
	public final Host localhost;

	private static NetworkManager instance = null;
	protected NetworkAutodiscover autodiscover;
	private ActionManager actionManager;
	private Host distantHost;
	private DatagramSocket udpInSocket;
	private DatagramSocket udpOutSocket;
	private Socket tcpSocket = null;
	private OutputStream tcpOutStream;
	private InputStream tcpInStream;
	private Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private InetAddress bcastAddress;
	private boolean receiving = false;

	private NetworkManager()
	{
		actionManager = new ActionManager(this);
		// TODO: Replace the name with the real one
		localhost = new Host("Toto");

		try
		{
			// FIXME: IPv6
			bcastAddress = InetAddress.getByName("255.255.255.255");
		}
		catch (UnknownHostException e)
		{
			// Should NEVER happen
			log.severe("The broadcast address is not recognized !");
		}

		try
		{
			udpInSocket = new DatagramSocket(NETWORK_PORT);
		}
		catch (SocketException e)
		{
			log.severe("Impossible to open the input UDP socket\n" + e.toString());
			// TODO: The best thing to do is close the program
		}
		try
		{
			udpOutSocket = new DatagramSocket();
			udpOutSocket.setBroadcast(true);
		}
		catch (SocketException e)
		{
			log.severe("Impossible to open the output UDP socket\n" + e.toString());
			// TODO: The best thing to do is close the program
		}
		
		autodiscover = new NetworkAutodiscover(this);
		startUdpReception();
	}

	public static NetworkManager getInstance()
	{
		if (instance == null)
			instance = new NetworkManager();

		return instance;
	}
	
	/**
	 * Start receiving from UDP. The packet are then forwarded to ActionManager.
	 */
	private void startUdpReception()
	{
		if(receiving)
			return;

		receiving = true;
		
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while(receiving)
				{
					DatagramPacket dp = new DatagramPacket(new byte[BUFFER_LENGTH], BUFFER_LENGTH);
					try
					{
						// Blocks until something arrives
						udpInSocket.receive(dp);
						JSONObject jo = new JSONObject(new String(dp.getData()));
						actionManager.executeAction(jo, dp.getAddress());
					}
					catch (JSONException e)
					{
						log.warning("Non-JSON packet received\n" + e.toString());
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * Send a message to the player set using the setHost method. The method chooses the right socket (TCP/UDP) automatically.
	 * 
	 * The attribute uuid of the message is automatically put. The action attribute is mandatory.
	 * 
	 * @param message
	 * @return true if everything went as expected
	 */
	public boolean send(NetworkMessage message)
	{
		byte[] data = prepareJsonObjectForTransfer(message.getJson());
		if (data == null)
			return false;
		try
		{
			if (tcpSocket != null)
				tcpOutStream.write(data);
			else
			{
				DatagramPacket dp = new DatagramPacket(data, data.length, distantHost.getAddress(), NETWORK_PORT);
				udpOutSocket.send(dp);
			}
			return true;
		}
		catch (IOException e)
		{
			log.warning("Unable to send packet\n" + e.toString());
			return false;
		}
	}

	/**
	 * Send a message to everyone.
	 * 
	 * The attribute uuid of the message is automatically put. The action attribute is mandatory.
	 * 
	 * @param message
	 * @return true if everything went as expected
	 */
	public boolean broadcast(NetworkMessage message)
	{
		byte[] data = prepareJsonObjectForTransfer(message.getJson());
		if (data == null)
			return false;

		DatagramPacket dp = new DatagramPacket(data, data.length, bcastAddress, NETWORK_PORT);
		try
		{
			udpOutSocket.send(dp);
			return true;
		}
		catch (IOException e)
		{
			log.warning(e.toString());
			return false;
		}
	}

	/**
	 * Transforms the JSONObject into byte[]
	 * 
	 * Also verifies that the required fields are present and set the UUID.
	 * @param j A message that should be sent over the network
	 * @return The message as byte[] ready to be sent over the network
	 */
	private byte[] prepareJsonObjectForTransfer(JSONObject j)
	{
		if (!j.has("action"))
			return null;
		j.put("uuid", localhost.getUuid().toString());
		return j.toString().getBytes();
	}

	public void setAutodiscoverListener(NetworkAutodiscoverListener al)
	{
		autodiscover.setListener(al);
	}

	public void removeAutodiscoverListener(NetworkAutodiscoverListener al)
	{
		autodiscover.removeListener(al);
	}

	public void setHost(Host host)
	{
		distantHost = host;
	}
}
