package org.oscim.test;

public class ext_Item {
   private String id;
   private String enabled;
   private String language;
   private String xmlLayer;

   
   public String getXmlLayer() {
      return xmlLayer;
  }  
   public void setXmlLayer(String xmlLayer) {
      this.xmlLayer = xmlLayer;
  } 

   @Override
   public String toString() {
       return "Item [xmlLAyer=" + xmlLayer + "]";
   }
}