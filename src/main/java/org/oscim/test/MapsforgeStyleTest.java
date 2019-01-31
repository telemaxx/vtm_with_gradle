/*
 * Copyright 2016-2017 devemux86
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.oscim.test;

import com.badlogic.gdx.Input;

import org.oscim.gdx.GdxMapApp;
import org.oscim.theme.ExternalRenderTheme;
import org.oscim.theme.XmlRenderThemeMenuCallback;
import org.oscim.theme.XmlRenderThemeStyleLayer;
import org.oscim.theme.XmlRenderThemeStyleMenu;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MapsforgeStyleTest extends MapsforgeTest {

	//public static final String mapFile = "C:\\OfflineMaps\\mapfiles\\download.mapsforge.org\\switzerland_V5.map";
   public static final String mapFile = "C:\\Users\\top\\BTSync\\oruxmaps\\mapfiles\\Germany_North_ML.map";
	//public static final String mapFile = "C:\\OfflineMaps\\mapfiles\\www.openandromaps.org\\Switzerland_ML.map";
	public static final String themeFile = "C:\\Users\\top\\BTSync\\oruxmaps\\mapstyles\\ELV4\\Elevate.xml";
	//public static final String multithemeString = "elv-mtb,elv-hiking,elv-cycling";
	public static final String multithemeString = "elv-cycling";	
	
    private MapsforgeStyleTest(File mapFile) {
        super(mapFile);
    }

  
    @Override
    protected void loadTheme(final String styleId) {

   	 mMap.setTheme(new ExternalRenderTheme(themeFile, new XmlRenderThemeMenuCallback() {
   		 @Override
   		 public Set<String> getCategories(XmlRenderThemeStyleMenu renderThemeStyleMenu) {
   			 // Use the selected style or the default
   			 String style = styleId != null ? styleId : renderThemeStyleMenu.getDefaultValue();
   			 System.out.println("#### load theme  default style: " + renderThemeStyleMenu.getDefaultValue());
   			 // Retrieve the layer from the style id
   			 XmlRenderThemeStyleLayer renderThemeStyleLayer = renderThemeStyleMenu.getLayer(style);
   			// renderThemeStyleMenu.
   			 System.out.println("renderthemeStyleLayers: " + renderThemeStyleLayer.getTitles());
   			 
   			 Map<String, XmlRenderThemeStyleLayer> themeLayers =  renderThemeStyleMenu.getLayers();
   			 System.out.println("themeLayers: " + themeLayers);
   			 //System.out.println("renderthemeStyleLayers: " + renderThemeStyleMenu.getLayers());


   			/*for (Map.Entry<String,XmlRenderThemeStyleLayer> entry : themeLayers.entrySet()) {
   				 String key = entry.getKey();
   				 XmlRenderThemeStyleLayer value = entry.getValue();
   				 //System.out.println("#### key, value: " + key + " " + value.getTitle("de"));
   				 System.out.println("#### key, value: " + key + " " + value.getTitles());
   				 //value.getId()
   				 // do stuff gives key values in any language
   			 }*/

   			 //System.out.println("renderthemeStyleLayer: " + renderThemeStyleLayer.getTitles());
   			 if (renderThemeStyleLayer == null) {
   				 System.err.println("Invalid style " + style);
   				 return null;
   			 }
   			 
   			 System.out.println("Renderstyle: " + renderThemeStyleLayer.getTitle(Locale.getDefault().getLanguage()));

   			 // First get the selected layer's categories that are enabled together
   			 Set<String> categories = renderThemeStyleLayer.getCategories();
   			 System.out.println("Categories: " + categories.toString());
   			 
   			 // Then add the selected layer's overlays that are enabled individually
   			 // Here we use the style menu, but users can use their own preferences
   			 
   			 for (XmlRenderThemeStyleLayer overlay : renderThemeStyleLayer.getOverlays()) {
   				 if (overlay.isEnabled())
   					 //System.out.println("Overlay: " + overlay.getTitle("de"));
   					 categories.addAll(overlay.getCategories());
   			 }
	 
   			 // This is the whole categories set to be enabled
   			 return categories;
   		 }
   	 }));
   	 
   	 
    }
    
    public void parser(String multithemes) {
		 if (multithemes == null) {
			 System.err.println("empty theme " + multithemes);
			 return;
		 }
   	 String[] a = multithemes.split(",");
   	 System.out.println("laenge: " + a.length);
   	 for (String oneTheme : a) {
   		 System.out.println("onetheme: " + oneTheme);
   	 }
   	 
    }

    @Override
    protected boolean onKeyDown(int keycode) {	 
        switch (keycode) {
            case Input.Keys.NUM_1:
                loadTheme("elv-mtb");
                //parser(multithemeString);
                mMap.clearMap();
                return true;
            case Input.Keys.NUM_2:
                loadTheme("elv-hiking");
                mMap.clearMap();
                return true;
            case Input.Keys.NUM_3:
               loadTheme("elv-city");
               mMap.clearMap();
               return true;
           case Input.Keys.NUM_4:
               loadTheme("elv-cycling");
               mMap.clearMap();
               return true;          
       }
        
        return super.onKeyDown(keycode);
    }

    public static void main(String[] args) {
        GdxMapApp.init();
        GdxMapApp.run(new MapsforgeStyleTest(getMapFile(mapFile)));
    }
}


