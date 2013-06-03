package ch.hearc.p2.battleforatlantis.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import ch.hearc.p2.battleforatlantis.gameengine.Player;
import ch.hearc.p2.battleforatlantis.net.NetworkAutodiscover.NetworkAutodiscoverListener;
import ch.hearc.p2.battleforatlantis.utils.Messages;
import ch.hearc.p2.battleforatlantis.utils.Settings;

public class NetworkManager
{
	/** UDP and TCP port on which to listen */
	public static final int NETWORK_PORT = 28526;
	/** Length of the buffer for incoming UDP and TCP packet. For the moment, it MUST be bigger than the biggest packet */
	public static final int BUFFER_LENGTH = 3000;
	/** How long should we try to open the TCP socket in ms */
	public static final int TCP_SOCKET_OPENING_TRYING_DURATION = 10000;
	/** Host object representing ourselves */
	public final Host localhost;

	/** Singleton instance */
	private static NetworkManager instance = null;
	/** Associated NetworkAutodiscover object */
	protected NetworkAutodiscover autodiscover;
	/** Associated ActionManager object */
	private ActionManager actionManager;
	/** Host object representing the other player (after successful connection) */
	private Host distantHost;
	/** Inbound UDP socket */
	private DatagramSocket udpInSocket;
	/** Outbound UDP socket (also for broadcasts) */
	private DatagramSocket udpOutSocket;
	/** TCP Socket (null until opened */
	private Socket tcpSocket = null;
	/** TCP abstract OutputStream */
	private OutputStream tcpOutStream;
	/** TCP abstract InputStream */
	private InputStream tcpInStream;
	/** Logger object */
	private static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	/** List of directed IP broadcast addresses of this machine */
	private List<InetAddress> bcastAddresses;
	/** Flag to stop UDP and TCP receiving threads */
	private boolean receiving = false;
	private boolean tcpReceiving = false;

	private NetworkManager()
	{
		actionManager = new ActionManager(this);
		localhost = new Host(Settings.FRAME_MAIN.getPlayerName());

		bcastAddresses = getDirectedBroadcastAddresses();

		log.info("Calculated broadcast addresses: " + bcastAddresses.toString());

		try
		{
			udpInSocket = new DatagramSocket(NETWORK_PORT);
		}
		catch (SocketException e)
		{
			log.severe("Impossible to open the input UDP socket\n" + e.toString());
			fatalError(e.getLocalizedMessage());
		}
		try
		{
			udpOutSocket = new DatagramSocket();
			udpOutSocket.setBroadcast(true);
		}
		catch (SocketException e)
		{
			log.severe("Impossible to open the output UDP socket\n" + e.toString());
			fatalError(e.getLocalizedMessage());
		}

		autodiscover = new NetworkAutodiscover(this);
		startUdpReception();
	}

	/**
	 * Get a list of all the IPv4 directed broadcast addresses of this machine
	 * 
	 * @return All the directed broadcast addresses of the current computer
	 */
	public static List<InetAddress> getDirectedBroadcastAddresses()
	{
		List<InetAddress> addr = new LinkedList<InetAddress>();
		try
		{
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

			while (networkInterfaces.hasMoreElements())
			{
				// FIXME: IPv6
				Iterator<InterfaceAddress> interfaceAddressesIterator = networkInterfaces.nextElement().getInterfaceAddresses().iterator();
				while (interfaceAddressesIterator.hasNext())
				{
					InterfaceAddress address = interfaceAddressesIterator.next();
					if (address.getBroadcast() != null)
						addr.add(address.getBroadcast());
				}
			}
		}
		catch (SocketException e1)
		{
			e1.printStackTrace();
		}

		return addr;
	}

	/**
	 * Get the singleton instance of this class
	 * 
	 * @return The unique instance of NetworkManager
	 */
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
		if (receiving)
			return;

		receiving = true;

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (receiving)
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
		}, "UDP Reception Thread").start();
	}

	/**
	 * Start receiving from UDP. The packet are then forwarded to ActionManager. tcpInputStream MUST be initialized before calling this method.
	 */
	private void startTcpReception()
	{
		if (tcpReceiving)
			return;

		tcpReceiving = true;

		new Thread(new Runnable()
		{
			byte[] buffer = new byte[BUFFER_LENGTH];

			@Override
			public void run()
			{
				while (tcpReceiving)
				{
					try
					{
						int length = tcpInStream.read(buffer);
						if (length < 1)
							continue;

						String string = new String(buffer, 0, length);
						JSONObject jo = new JSONObject(string);
						actionManager.executeAction(jo, tcpSocket.getInetAddress());
					}
					catch (JSONException e)
					{
						log.warning("Non-JSON packet received\n" + e.toString());
					}
					catch (IOException e)
					{	
						log.severe("Connexion perdue !");
						if(tcpReceiving)
							fatalError(e.getLocalizedMessage());
						break;
					}
				}
			}
		}, "TCP Reception Thread").start();
	}
	
	public void closeTcpConnection()
	{
		if(tcpSocket != null)
		{
			tcpReceiving = false;
			
			try
			{
				tcpInStream.close();
				tcpOutStream.close();
				tcpSocket.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				tcpInStream = null;
				tcpOutStream = null;
				tcpSocket = null;
			}
		}
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
		return send(message, distantHost.getAddress());
	}

	public boolean send(NetworkMessage message, InetAddress ip)
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
				DatagramPacket dp = new DatagramPacket(data, data.length, ip, NETWORK_PORT);
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

		boolean result = true;

		for (InetAddress addr : bcastAddresses)
		{
			DatagramPacket dp = new DatagramPacket(data, data.length, addr, NETWORK_PORT);
			try
			{
				udpOutSocket.send(dp);
			}
			catch (IOException e)
			{
				log.warning(e.toString());
				result = false;
			}
		}

		return result;
	}

	/**
	 * Transforms a JSONObject into byte[]
	 * 
	 * Also verifies that the required fields are present and set the UUID.
	 * 
	 * @param j
	 *            A message that should be sent over the network
	 * @return The message as byte[] ready to be sent over the network
	 */
	private byte[] prepareJsonObjectForTransfer(JSONObject j)
	{
		if (!j.has("action"))
			return null;
		j.put("uuid", localhost.getUuid().toString());
		return j.toString().getBytes();
	}

	/**
	 * Set the listener that should be called when a distant host has been detected/has left.
	 * 
	 * @param al
	 *            The object that should be notified.
	 */
	public void setAutodiscoverListener(NetworkAutodiscoverListener al)
	{
		autodiscover.setListener(al);
	}

	/**
	 * Remove the autodiscover listener
	 * 
	 * @param al
	 *            The listener to remove
	 */
	public void removeAutodiscoverListener(NetworkAutodiscoverListener al)
	{
		autodiscover.removeListener(al);
	}

	/**
	 * Send a tryConnect packet to the remote host
	 * 
	 * @param ip
	 *            The InetAddress to which this packet should be sent
	 * @param hashConfig
	 *            The cryptographic hash of the config file
	 */
	public void tryConnect(InetAddress ip, final String hashConfig)
	{
		NetworkMessage networkMessage = NetworkMessages.tryConnect(localhost.getUuid().toString(), hashConfig, localhost.getName());
		send(networkMessage, ip);
	}

	/**
	 * Send a tryConnect packet to the remote host. If the connection is accepted, PanelPrepare will be shown to the user.
	 * 
	 * @param accepted
	 *            True if the connection is accepted (the game should start)
	 * @param h
	 *            The Host to which this packet should be sent
	 */
	public void connectionResponse(boolean accepted, Host h)
	{
		NetworkMessage networkMessage = NetworkMessages.connectionResponse(localhost.getUuid().toString(), accepted, localhost.getName());
		send(networkMessage, h.getAddress());
		if (accepted)
		{
			Settings.FRAME_MAIN.placeShips(h);
			startTcpConnection(h);
		}
	}

	/**
	 * Initialize the TCP connection with a remote host. This method has to be called by the remote host at the same time.
	 * 
	 * @param h
	 *            The remote Host
	 */
	protected void startTcpConnection(Host h)
	{
		distantHost = h;
		switch (localhost.getUuid().compareTo(h.getUuid()))
		{
		// We are slave
			case -1:
				tcpConnect();
				Settings.FRAME_MAIN.setFirstPlayerToPlay(Player.DISTANT);
				break;
			// We are master
			case 1:
				listenToTcpConnection();
				Settings.FRAME_MAIN.setFirstPlayerToPlay(Player.LOCAL);
				break;
			// UUIDs are equal !
			case 0:
				log.severe("The UUIDs are equal !");
				fatalError(Messages.getString("The UUIDs are equal"));
				return;
		}
	}

	/**
	 * Open the TCP socket as slave.
	 */
	private void tcpConnect()
	{
		NetworkMessage message = NetworkMessages.readyToPlay(localhost.getUuid().toString());

		boolean succeded = false;
		while (!succeded)
		{
			try
			{
				tcpSocket = new Socket(distantHost.getAddress(), NETWORK_PORT);
				tcpInStream = tcpSocket.getInputStream();
				tcpOutStream = tcpSocket.getOutputStream();

				send(message);
				startTcpReception();
				succeded = true;
			}
			catch (IOException e)
			{
				log.info("Connection refused. Retrying...");
				try
				{
					Thread.sleep(200);
				}
				catch (InterruptedException e1)
				{
					e1.printStackTrace();
				}
			}
		}

		log.info("TCP Connection successful");
	}

	/**
	 * Open the TCP socket as master.
	 */
	private void listenToTcpConnection()
	{
		try
		{
			ServerSocket serverSocket = new ServerSocket(NETWORK_PORT);
			// serverSocket.setSoTimeout(5000);
			tcpSocket = serverSocket.accept();
			log.info("ServerSocket.accept()");
			serverSocket.close();

			tcpInStream = tcpSocket.getInputStream();
			tcpOutStream = tcpSocket.getOutputStream();

			startTcpReception();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Host getDistantHost()
	{
		return distantHost;
	}
	
	private void fatalError(String message)
	{
		JOptionPane.showMessageDialog(Settings.FRAME_MAIN, Messages.getString("NetworkManager.FatalErrorMessage") + "\n\n" + message, Messages.getString("NetworkManager.FatalErrorTitle"), JOptionPane.ERROR_MESSAGE);
		System.exit(-1);
	}
}
