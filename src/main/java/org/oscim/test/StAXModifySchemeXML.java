package org.oscim.test;
/*https://www.tutorialspoint.com/java_xml/java_stax_modify_document.htm
 * example:
<?xml version = "1.0"?>
<class>
   <student rollno = "393">
      <firstname>dinkar</firstname>
      <lastname>kad</lastname>
      <nickname>dinkar</nickname>
      <marks>85</marks>
   </student>
   
   <student rollno = "493">
      <firstname>Vaneet</firstname>
      <lastname>Gupta</lastname>
      <nickname>vinni</nickname>
      <marks>95</marks>
   </student>
   
   <student rollno = "593">
      <firstname>jasvir</firstname>
      <lastname>singh</lastname>
      <nickname>jazz</nickname>
      <marks>90</marks>
   </student>
</class>
 * 
 */

import java.io.File;
   import java.io.FileNotFoundException;
   import java.io.FileReader;
   import java.io.IOException;
   import java.util.Iterator;
   import java.util.List;

   import javax.xml.stream.XMLEventReader;
   import javax.xml.stream.XMLInputFactory;
   import javax.xml.stream.XMLStreamConstants;
   import javax.xml.stream.XMLStreamException;
   import javax.xml.stream.events.Attribute;
   import javax.xml.stream.events.StartElement;
   import javax.xml.stream.events.XMLEvent;

   import org.jdom2.Document;
   import org.jdom2.Element;
   import org.jdom2.JDOMException;
   import org.jdom2.input.SAXBuilder;
   import org.jdom2.output.Format;
   import org.jdom2.output.XMLOutputter;

   public class StAXModifySchemeXML {

      public static void main(String[] args) {

         try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(
               new FileReader("input.txt"));
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(new File("input.txt"));
            Element rootElement = document.getRootElement();
            List<Element> studentElements = rootElement.getChildren("student");

            while(eventReader.hasNext()) {
               XMLEvent event = eventReader.nextEvent();
               
               switch(event.getEventType()) {
                  case XMLStreamConstants.START_ELEMENT:
                  StartElement startElement = event.asStartElement();
                  String qName = startElement.getName().getLocalPart();

                  if (qName.equalsIgnoreCase("student")) {
                     Iterator<Attribute> attributes = startElement.getAttributes();
                     String rollNo = attributes.next().getValue();
                     
                     if(rollNo.equalsIgnoreCase("393")) {
                        //get the student with roll no 393

                        for(int i = 0;i < studentElements.size();i++) {
                           Element studentElement = studentElements.get(i);

                           if(studentElement.getAttribute(
                              "rollno").getValue().equalsIgnoreCase("393")) {
                              studentElement.removeChild("marks");
                              studentElement.addContent(new Element("marks").setText("80"));
                           }
                        }
                     }
                  }
                  break;
               }
            }
            XMLOutputter xmlOutput = new XMLOutputter();

            // display xml
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(document, System.out);
         } catch (FileNotFoundException e) {
            e.printStackTrace();
         } catch (XMLStreamException e) {
            e.printStackTrace();
         } catch (JDOMException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }
