package org.oscim.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * reading Mapsforge theme. after making instance call readXML
 * @author telemaxx
 * @see http://www.vogella.com/tutorials/JavaXML/article.html
 * 
 */
public class MapsforgeStyleParser {
	static final String ID = "id";
	static final String XML_LAYER = "layer";
	static final String STYLE_MENU = "stylemenu";
	static final String VISIBLE = "visible";
	static final String NAME = "name";
	static final String LANG = "lang";
	static final String DEFAULTLANG = "defaultlang";
	static final String DEFAULTSTYLE = "defaultvalue";
	static final String VALUE = "value";
	static  Boolean Style = false;
	String na_language = "";
	String na_value = "";
	String defaultlanguage = "";
	String defaultstyle = "";

	/**
	 * reading mapsforgetheme and return a list mit selectable layers
	 * @param xmlFile
	 * @return a list with availible, visible layers
	 */
	@SuppressWarnings({ "unchecked", "null" })
	public List<Style> readXML(String xmlFile) {

		List<Style> items = new ArrayList<Style>();
		try {
			// First, create a new XMLInputFactory
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			// Setup a new eventReader
			InputStream in = new FileInputStream(xmlFile);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			// read the XML document
			Style item = null;

			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					// if stylemenue, getting the defaults
					if (startElement.getName().getLocalPart().equals(STYLE_MENU)) {
						Iterator<Attribute> sm_attributes = startElement.getAttributes();
						while (sm_attributes.hasNext()) { //in the same line as <layer>
							Attribute sm_attribute = sm_attributes.next();
							if (sm_attribute.getName().toString().equals(DEFAULTLANG)) {
								defaultlanguage = sm_attribute.getValue();
							}
							if (sm_attribute.getName().toString().equals(DEFAULTSTYLE)) {
								defaultstyle =  sm_attribute.getValue();
								System.out.println("default style.: " + defaultstyle);
							}
						}
					}					
					// If we have an item(layer) element, we create a new item
					if (startElement.getName().getLocalPart().equals(XML_LAYER)) {
						Style = false;
						item = new Style();
						Iterator<Attribute> attributes = startElement.getAttributes();
						while (attributes.hasNext()) { //in the same line as <layer>
							Attribute attribute = attributes.next();
							if (attribute.getName().toString().equals(ID)) {
								item.setXmlLayer(attribute.getValue());
							}
							if (attribute.getName().toString().equals(VISIBLE)) {
								if(attribute.getValue().equals("true")){
									Style = true;
								}
							}
						}
					}
               if (event.isStartElement()) {
                  if (event.asStartElement().getName().getLocalPart().equals(NAME)) {
                  	Iterator<Attribute> name_attributes = startElement.getAttributes();
   						while (name_attributes.hasNext()) { //in the same line as <layer>
   							Attribute name_attribute = name_attributes.next();
   							if (name_attribute.getName().toString().equals(LANG)){
   								na_language = name_attribute.getValue();
   							}
   							if (name_attribute.getName().toString().equals(VALUE)){
   								na_value = name_attribute.getValue();
   							} 							
   						}  
   						if (Style) {
   							item.setName(na_language, na_value);
   						}
                      event = eventReader.nextEvent();
                      continue;
                  }
              }
				}
				// If we reach the end of an item element, we add it to the list
				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart().equals(XML_LAYER) && Style) {
						item.setDefaultLanguage(defaultlanguage);
						items.add(item);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return items;
	} //end ReadConfig
	
	public String getDefaultLanguage() {
		return defaultlanguage;
	}
	
	public String getDefaultStyle() {
		return defaultstyle;
	}

	/**
	 * just for test purposes
	 * @param args
	 */
	public static void main(String args[]) {
		MapsforgeStyleParser mapStyleParser = new MapsforgeStyleParser();
		List<Style> styles = mapStyleParser.readXML("/home/top/oruxmaps/mapstyles/ELV4/Elevate.xml");
		//List<Style> styles = mapStyleParser.readXML("C:\\Users\\top\\BTSync\\oruxmaps\\mapstyles\\ELV4\\Elevate.xml");
		System.out.println("Stylecount: " + styles.size());
		System.out.println("Defaultlanguage: " + mapStyleParser.getDefaultLanguage());
		System.out.println("Defaultstyle:    " + mapStyleParser.getDefaultStyle());
		//System.out.println("Defaultstylename de:" + styles.);
		for (Style style : styles) {
			System.out.println(style);
			System.out.println("local Name: " + style.getName(""));
		}
	}
}

/**
 * Bean Style containes a visible Style
 * @author telemaxx
 *
 */

class Style {
	private Map<String, String> name = new HashMap<String, String>();
   private String xmlLayer;
   private String defaultlanguage = "de";

   public String getDefaultLaguage() {
      return defaultlanguage;
  }
	public void setDefaultLanguage(String language) {
      this.defaultlanguage = language;
  }   
	
   /**
    * get the style name like
    * @return String containing the stylename like "elv-mtb"
    */ 
   public String getXmlLayer() {
      return xmlLayer;
  }
	public void setXmlLayer(String xmlLayer) {
      this.xmlLayer = xmlLayer;
  } 
	
	/**
	 * set the style name with a given language
	 * @param language
	 * @param name
	 */	
   public void setName(String language, String name) {
   	System.out.println("setname: " + language + " name: " + name);
		this.name.put(language, name);
	}
   
   /**
    * getting a local name of the mapstyle
    * @param language string like "en"
    * @return a String with the local name like "hiking"
    */
   public String getName(String language) {
   	if(language.equals("default")){
   		return name.get(defaultlanguage);
   	} else
   	if(name.containsKey(language)){
   		return name.get(language);
   	} else {
   		return name.get(defaultlanguage);
   	}
   }
   
   /**
    * getting the name as map with all localizations
    * @return Map<String language,String name>
    */  
   public Map<String, String> getName() {
   	return name;
   }  
   
   @Override
   public String toString() {
       return "Item [xmlLayer=" + xmlLayer + " Name= " + name.get(defaultlanguage) + "]";
   }
}