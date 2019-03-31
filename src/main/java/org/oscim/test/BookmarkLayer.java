/*
 * Copyright 2016-2018 devemux86
 * Copyright 2017 nebular
 * Copyright 2019 telemaxx
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

import org.oscim.backend.CanvasAdapter;
import org.oscim.backend.canvas.Bitmap;
import org.oscim.backend.canvas.Color;
import org.oscim.backend.canvas.Paint;
import org.oscim.core.GeoPoint;
import org.oscim.gdx.GdxMapApp;
import org.oscim.layers.marker.ClusterMarkerRenderer;
import org.oscim.layers.marker.ItemizedLayer;
import org.oscim.layers.marker.MarkerItem;
import org.oscim.layers.marker.MarkerLayer;
import org.oscim.layers.marker.MarkerRenderer;
import org.oscim.layers.marker.MarkerRendererFactory;
import org.oscim.layers.marker.MarkerSymbol;
import org.oscim.layers.marker.MarkerSymbol.HotspotPlace;
import org.oscim.layers.tile.bitmap.BitmapTileLayer;
import org.oscim.tiling.TileSource;
import org.oscim.tiling.source.OkHttpEngine;
import org.oscim.tiling.source.bitmap.DefaultSources;

//import org.oscim.test.BookmarkLayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Path2D;

public class BookmarkLayer extends GdxMapApp implements ItemizedLayer.OnItemGestureListener<MarkerItem> {// extends MarkerLayerTest {

   static final boolean BILLBOARDS = true;
   MarkerSymbol mFocusMarker;
   ItemizedLayer<MarkerItem> mMarkerLayer;
   private static final int COUNT = 5;
   private static final float STEP = 100f / 110000f; // roughly 100 meters

   @Override
   public void createLayers() {
       try {
           // Map events receiver
           //mMap.layers().add(new MapEventsReceiver(mMap));

           TileSource tileSource = DefaultSources.OPENSTREETMAP
                   .httpFactory(new OkHttpEngine.OkHttpFactory())
                   .build();
           mMap.layers().add(new BitmapTileLayer(mMap, tileSource));

           mMap.setMapPosition(53.08, 8.83, 1 << 15);
           
           // pink dot
           Bitmap bitmapPoi;
           String markerRecource = "/res/marker_poi.png--";
           if (getClass().getResourceAsStream(markerRecource) != null) {
              bitmapPoi = CanvasAdapter.decodeBitmap(getClass().getResourceAsStream(markerRecource));
           } else {
              int DefaultIconSize = 20;
              
              final Paint fillPainter = CanvasAdapter.newPaint();
              fillPainter.setStyle(Paint.Style.FILL);
              fillPainter.setColor(0xFFFFFF00); // 100percent yellow
              
              final Paint textPainter = CanvasAdapter.newPaint();
              textPainter.setStyle(Paint.Style.FILL);
              textPainter.setColor(0xFFFF69B4);// 100percent pink
              
              bitmapPoi = CanvasAdapter.newBitmap(DefaultIconSize, DefaultIconSize, 0);
              org.oscim.backend.canvas.Canvas defaultMarkerCanvas = CanvasAdapter.newCanvas();  
              defaultMarkerCanvas.setBitmap(bitmapPoi);
              
              //defaultMarkerCanvas.drawCircle(DefaultIconSize/2, DefaultIconSize/2, DefaultIconSize/2, fillPainter);

              float half = DefaultIconSize/2;

              fillPainter.setStrokeWidth(2);

              defaultMarkerCanvas.drawLine(half * 0.1f  , half * 0.65f, half * 1.9f  , half * 0.65f, fillPainter);
              defaultMarkerCanvas.drawLine(half * 1.9f , half * 0.65f , half * 0.40f , half * 1.65f, fillPainter);
              defaultMarkerCanvas.drawLine(half * 0.40f , half * 1.65f, half         ,   0         , fillPainter);
              defaultMarkerCanvas.drawLine(half         ,   0         , half * 1.60f , half * 1.65f, fillPainter);
              defaultMarkerCanvas.drawLine(half * 1.60f , half * 1.65f, half * 0.1f  , half * 0.65f, fillPainter);
           }

           final MarkerSymbol symbol;
           if (BILLBOARDS)
               symbol = new MarkerSymbol(bitmapPoi, MarkerSymbol.HotspotPlace.BOTTOM_CENTER);
           else
               symbol = new MarkerSymbol(bitmapPoi, MarkerSymbol.HotspotPlace.CENTER, false);

           Bitmap bitmapFocus = CanvasAdapter.decodeBitmap(getClass().getResourceAsStream("/res/marker_focus.png"));
           if (BILLBOARDS)
               mFocusMarker = new MarkerSymbol(bitmapFocus, HotspotPlace.BOTTOM_CENTER);
           else
               mFocusMarker = new MarkerSymbol(bitmapFocus, HotspotPlace.CENTER, false);

           
           MarkerRendererFactory markerRendererFactory = new MarkerRendererFactory() {
               @Override
               public MarkerRenderer create(MarkerLayer markerLayer) {
                  //return new ClusterMarkerRenderer(markerLayer, symbol, null) {
                   return new ClusterMarkerRenderer(markerLayer, symbol, new ClusterMarkerRenderer.ClusterStyle(Color.WHITE, Color.BLUE)) {
                       @Override
                       protected Bitmap getClusterBitmap(int size) {
                           // Can customize cluster bitmap here
                          return super.getClusterBitmap(size);
                       }
                   };
               }
           };
           
           mMarkerLayer = new ItemizedLayer<>(
                   mMap,
                   new ArrayList<MarkerItem>(),
                   markerRendererFactory,
                   this);
           mMap.layers().add(mMarkerLayer);
           

           // Create some markers spaced STEP degrees
           //Berlin: 52.513452, 13.363791
           List<MarkerItem> pts = new ArrayList<>();
           GeoPoint center = mMap.getMapPosition().getGeoPoint();
           for (int x = -COUNT; x < COUNT; x++) {
               for (int y = -COUNT; y < COUNT; y++) {
                   double random = STEP * Math.random() * 2;
                   MarkerItem item = new MarkerItem(y + ", " + x, "Title " + center.getLatitude() + "/" + center.getLongitude(),"Description "  + x + "/" + y,
                           new GeoPoint(center.getLatitude() + y * STEP + random, center.getLongitude() + x * STEP + random)
                   );
                   pts.add(item);
               }
           }
           mMarkerLayer.addItems(pts);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

   
   
   
   
   @Override
   public boolean onItemSingleTapUp(int index, MarkerItem item) {
       if (item.getMarker() == null)
           item.setMarker(mFocusMarker);
       else
           item.setMarker(null);

       System.out.println("Marker tap " + item.getTitle());
       return true;
   }

   @Override
   public boolean onItemLongPress(int index, MarkerItem item) {
       if (item.getMarker() == null)
           item.setMarker(mFocusMarker);
       else
           item.setMarker(null);

       System.out.println("Marker long press " + item.getTitle());
       return true;
   }
   
   public static void main(String[] args) {
       GdxMapApp.init();
       GdxMapApp.run(new BookmarkLayer());
   }
   
  
   
}
