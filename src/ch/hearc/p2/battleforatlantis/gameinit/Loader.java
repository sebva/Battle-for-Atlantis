package ch.hearc.p2.battleforatlantis.gameinit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
import ch.hearc.p2.battleforatlantis.utils.ImageShop.ShipType;

public class Loader extends DefaultHandler
{
	private String xml;
	private String xsd;
	private String hash = null;
	private Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public List<Map> maps;

	private static final String kDefaultConfigFile = "config/config.default.xml";
	private static final String kXsdConfigFile = "config/config.xsd";
	private static final String kOverrideConfigFile = "config.xml";

	public Loader() throws URISyntaxException, IOException
	{
		maps = new ArrayList<Map>();
		
		File overrideFile = new File(kOverrideConfigFile);
		if (overrideFile.exists())
			this.xml = kOverrideConfigFile;
		else
			this.xml = kDefaultConfigFile;

		this.xsd = kXsdConfigFile;
	}

	public void load() throws ParserConfigurationException, SAXException, IOException, Exception
	{
		if (!validateXmlWithXsd())
			throw new Exception("XML File not conforming to XSD");

		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		sp.parse(ClassLoader.getSystemResourceAsStream(xml), this);
	}

	/**
	 * Validate the XML file using an XSD file
	 * 
	 * Source: http://docs.oracle.com/javase/7/docs/api/javax/xml/validation/package-summary.html
	 * 
	 * @return True if the document is valid
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private boolean validateXmlWithXsd() throws ParserConfigurationException, SAXException, IOException
	{
		// parse an XML document into a DOM tree
		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = parser.parse(ClassLoader.getSystemResourceAsStream(xml));

		// create a SchemaFactory capable of understanding WXS schemas
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		// load a WXS schema, represented by a Schema instance
		Source schemaFile = new StreamSource(ClassLoader.getSystemResourceAsStream(xsd));
		Schema schema = factory.newSchema(schemaFile);

		// create a Validator instance, which can be used to validate an instance document
		Validator validator = schema.newValidator();

		// validate the DOM tree
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
		switch(qName)
		{
			case "Level":
			{
				int height = Integer.parseInt(attributes.getValue("height"));
				int width = Integer.parseInt(attributes.getValue("width"));
				MapType type = MapType.valueOf(attributes.getValue("type").toUpperCase());
				maps.add(new Map(width, height, type));
				break;
			}
			case "Ship":
			{
				int length = Integer.parseInt(attributes.getValue("length"));
				int amount = Integer.parseInt(attributes.getValue("amount"));
				
				// TODO: parse ship type (ship, submarine) from file
				new Ship(ShipType.SHIP, length);
				break;
			}
			case "City":
			{
				String shape = attributes.getValue("shape");
				int height = Integer.parseInt(attributes.getValue("height"));
				int width = Integer.parseInt(attributes.getValue("width"));
				break;
			}
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		log.info("endElement : " + localName + " : " + qName);
	}

	public void characters(char ch[], int start, int length) throws SAXException
	{
		String value = new String(ch, start, length).trim();
		if (value.length() == 0)
			return; // ignore white space

		log.info("characters : " + new String(ch));
	}

	public String getHash() throws NoSuchAlgorithmException, IOException
	{
		if(hash != null)
			return hash;
		
		//Change SHA1 to Algorithm of your choice according to java Specification  
		MessageDigest md = MessageDigest.getInstance("SHA1");

		//reads the file path & file name As a argument
		DigestInputStream dis = new DigestInputStream(ClassLoader.getSystemResourceAsStream(xml), md);
		BufferedInputStream bis = new BufferedInputStream(dis);

		//Read the bis so SHA1 is auto calculated at dis
		while (true) {
			int b = bis.read();
			if (b == -1)
				break;
		}

		BigInteger bi = new BigInteger(md.digest());
		bis.close();
		dis.close();
		return bi.toString(16);
	}

}
