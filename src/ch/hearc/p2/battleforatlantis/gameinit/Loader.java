package ch.hearc.p2.battleforatlantis.gameinit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ch.hearc.p2.battleforatlantis.gameengine.Map;
import ch.hearc.p2.battleforatlantis.gameengine.MapType;
import ch.hearc.p2.battleforatlantis.gameengine.Ship;
import ch.hearc.p2.battleforatlantis.gameengine.ShipType;
import ch.hearc.p2.battleforatlantis.utils.Settings;

/**
 * This class loads the game's configuration from an XML file. The job is done when load() is called. The loaded elements can be be retrieved with the get
 * methods.
 * 
 * @author Sébastien Vaucher
 * 
 */
public class Loader extends DefaultHandler
{
	/** Path to the XML file */
	private String xml;
	/** Path to the XSD file */
	private String xsd;
	/** SHA-1 hash of the config file */
	private String hash = null;
	/** List of the maps described in the XML file */
	private List<Map> maps;
	/** List of the ships described in the XML file */
	private List<Ship> ships;
	/** Atlantis **/
	private Map atlantis;

	/** Current Level tag being treated */
	private MapType currentLevel = null;
	/** Current ship id */
	private int shipId = 1;

	/** Path to the default configuration file (in JAR) */
	private static final String kDefaultConfigFile = "config/config.default.xml";
	/** Path to the XML Schema (in JAR) */
	private static final String kXsdConfigFile = "config/config.xsd";
	/** Path to the user's XML file (in current directory */
	private static final String kOverrideConfigFile = "config.xml";

	/**
	 * Create a Loader with the default file paths.
	 * 
	 * @throws Exception Thrown if the config file cannot be opened.
	 */
	public Loader() throws Exception
	{
		maps = new ArrayList<Map>();
		ships = new ArrayList<Ship>();

		// If the overriding file exists, we load it, else we load the default file
		File overrideFile = new File(kOverrideConfigFile);
		if (overrideFile.exists())
			this.xml = kOverrideConfigFile;
		else
			this.xml = kDefaultConfigFile;

		this.xsd = kXsdConfigFile;
	}

	/**
	 * Starts the XML parser which will fill this object's attributes.
	 * 
	 * @throws ParserConfigurationException Impossible to find a SAX parser
	 * @throws SAXException Malformed XML file
	 * @throws IOException Error while manipulating the file
	 * @throws Exception The XML file does not conform to the XML Schema
	 */
	public void load() throws ParserConfigurationException, SAXException, IOException, Exception
	{
		// The XML file is not confirming to our XML Schema
		if (!validateXmlWithXsd())
			throw new Exception("XML File not conforming to XSD");

		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		// parse() blocks until the whole XML file is parsed
		sp.parse(ClassLoader.getSystemResourceAsStream(xml), this);
	}

	/**
	 * Validate the XML file using an XSD file
	 * 
	 * Source: http://docs.oracle.com/javase/7/docs/api/javax/xml/validation/package-summary.html
	 * 
	 * @return True if the document is valid
	 * @throws ParserConfigurationException Impossible to find a SAX parser
	 * @throws SAXException Malformed XML file
	 * @throws IOException Error while manipulating the file
	 */
	private boolean validateXmlWithXsd() throws ParserConfigurationException, SAXException, IOException
	{
		// Parse the XML document into a DOM tree
		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = parser.parse(ClassLoader.getSystemResourceAsStream(xml));

		// Create a SchemaFactory capable of understanding WXS schemas
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		// Load a WXS schema, represented by a Schema instance
		Source schemaFile = new StreamSource(ClassLoader.getSystemResourceAsStream(xsd));
		Schema schema = factory.newSchema(schemaFile);

		// Create a Validator instance, which can be used to validate an instance document
		Validator validator = schema.newValidator();

		// Validate the DOM tree
		try
		{
			validator.validate(new DOMSource(document));
		}
		catch (SAXException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		switch (qName)
		{
			case "Level":
			{
				int height = Integer.parseInt(attributes.getValue("height"));
				int width = Integer.parseInt(attributes.getValue("width"));
				MapType type = MapType.valueOf(attributes.getValue("type").toUpperCase());
				currentLevel = type;

				Map map = null;
				try
				{
					if (this.currentLevel == MapType.ATLANTIS)
					{
						map = new Map(width, height, type, false);
						AtlantisCreator.setMap(map);
						this.atlantis = map;
					}
					else
						map = new Map(width, height, type, true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

				maps.add(map);

				break;
			}
			case "Ship":
			{
				int length = Integer.parseInt(attributes.getValue("length"));
				int amount = Integer.parseInt(attributes.getValue("amount"));

				ShipType type = null;
				if (currentLevel.equals(MapType.SURFACE))
					type = ShipType.SHIP;
				else if (currentLevel.equals(MapType.SUBMARINE))
					type = ShipType.SUBMARINE;

				assert (type != null);

				for (int i = 0; i < amount; i++)
					ships.add(new Ship(length, type, shipId++));

				break;
			}
			case "City":
			{
				int height = Integer.parseInt(attributes.getValue("height"));
				int width = Integer.parseInt(attributes.getValue("width"));

				Settings.ATLANTIS_WIDTH = width;
				Settings.ATLANTIS_HEIGHT = height;

				break;
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		if ("Level".equals(qName))
			currentLevel = null;
	}

	/**
	 * Get the SHA-1 hash of the loaded XML file
	 * 
	 * @return The SHA-1 hash
	 * @throws NoSuchAlgorithmException SHA-1 cannot be found
	 * @throws IOException Error while manipulating the file
	 */
	public String getHash() throws NoSuchAlgorithmException, IOException
	{
		if (hash != null)
			return hash;

		MessageDigest md = MessageDigest.getInstance("SHA1");

		DigestInputStream dis = new DigestInputStream(ClassLoader.getSystemResourceAsStream(xml), md);
		BufferedInputStream bis = new BufferedInputStream(dis);

		// Reading the BufferedInputStream fills the MessageDigest
		while (true)
		{
			int b = bis.read();
			if (b == -1)
				break;
		}

		BigInteger bi = new BigInteger(md.digest());
		bis.close();
		dis.close();
		// Hexadecimal
		return bi.toString(16);
	}

	/**
	 * Get the ships loaded from the XML file
	 * 
	 * @return An array of Ship. The ships have their type attribute correctly set
	 */
	public Ship[] getShips()
	{
		Ship[] toRet = new Ship[ships.size()];
		for (int i = 0; i < ships.size(); i++)
			toRet[i] = ships.get(i);

		return toRet;
	}

	/**
	 * Get the maps loaded from the XML, without the ones marked as type="atlantis"
	 * 
	 * @return An array of Map. The maps have their type attribute correctly set
	 */
	public Map[] getMapsWithoutAtlantis()
	{
		Map[] toRet = new Map[maps.size() - 1];
		for (int i = 0; i < maps.size(); i++)
		{
			if (maps.get(i).getType() != MapType.ATLANTIS)
				toRet[i] = maps.get(i);
		}

		return toRet;
	}

	/**
	 * Get the maps loaded from the XML
	 * 
	 * @return An array of Map. The maps have their type attribute correctly set
	 */
	public Map[] getMapsWithAtlantis()
	{
		Map[] toRet = new Map[maps.size()];
		for (int i = 0; i < maps.size(); i++)
			toRet[i] = maps.get(i);

		return toRet;
	}

	/**
	 * Get the atlantis generated
	 * 
	 * @return An Atlantis Object.
	 */
	public Map getAtlantis()
	{
		return this.atlantis;
	}
}
