/*
 * Copyright 2016 devemux86
 *
 * This file is part of the OpenScienceMap project (http://www.opensciencemap.org).
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

import okhttp3.Cache;
import okhttp3.OkHttpClient;

import java.io.File;
import java.util.UUID;

import org.oscim.gdx.GdxMapApp;
import org.oscim.layers.tile.bitmap.BitmapTileLayer;
import org.oscim.renderer.MapRenderer;
import org.oscim.tiling.TileSource;
import org.oscim.tiling.source.OkHttpEngine;
import org.oscim.tiling.source.bitmap.DefaultSources;
import org.oscim.tiling.source.oscimap4.OSciMap4TileSource;

public class BitmapTileTest extends GdxMapApp {

   private BitmapTileLayer mLayer = null;
   private BitmapTileLayer mShaded = null;
   private BitmapTileLayer _layer_HillShadingLayer = null;
   private BitmapTileLayer cyclingLayer = null;

   @Override
   protected boolean onKeyDown(int keycode) {
      if (keycode == Input.Keys.NUM_1) {
         mMap.layers().remove(mShaded);
         mShaded = null;
         mMap.layers().remove(mLayer);
         mLayer = new BitmapTileLayer(mMap, DefaultSources.OPENSTREETMAP.build(), 4000000);
         mMap.layers().add(mLayer);
         mMap.clearMap();

         return true;
      } else if (keycode == Input.Keys.NUM_2) {
         mMap.layers().remove(mShaded);
         mShaded = null;
         mMap.layers().remove(mLayer);
         mLayer = new BitmapTileLayer(mMap, DefaultSources.STAMEN_TONER.build(), 4000000);
         mMap.layers().add(mLayer);
         mMap.clearMap();
         return true;
      } else if (keycode == Input.Keys.NUM_3) {
         if (mShaded != null) {
            mMap.layers().remove(mShaded);
            mShaded = null;
         } else {
            mShaded = new BitmapTileLayer(mMap, DefaultSources.HIKEBIKE_HILLSHADE.build(), 4000000);
            mMap.layers().add(mShaded);
         }
         mMap.clearMap();
         return true;
      } else if (keycode == Input.Keys.NUM_4) {
         if (mShaded != null) {
            mMap.layers().remove(mShaded);
            mShaded = null;
         } else {
            TileSource cycling_ts = OSciMap4TileSource.builder()
                  .httpFactory(new OkHttpEngine.OkHttpFactory())
                  .url("https://tile.waymarkedtrails.org/cycling")
                  .tilePath("/{Z}/{X}/{Y}.png")
                  .build();
            cyclingLayer = new BitmapTileLayer(mMap, cycling_ts, 4000000);
            mMap.layers().add(cyclingLayer);
            
            mShaded = new BitmapTileLayer(mMap, DefaultSources.HIKEBIKE_HILLSHADE.build(), 4000000);
            mMap.layers().add(mShaded);
         }
         mMap.clearMap();
         return true;
      }

      return false;
   }

   @Override
   public void createLayers() {

      OkHttpClient.Builder builder = new OkHttpClient.Builder();
      //File cacheDirectory = new File(System.getProperty("java.io.tmpdir") + "vtm-cache" , UUID.randomUUID().toString());
      File cacheDirectory = new File(System.getProperty("java.io.tmpdir") + "vtm-cache");
      System.out.println("caching to: " + cacheDirectory.toString());
      int cacheSize = 32 * 1024 * 1024; // 32 MB
      Cache cache = new Cache(cacheDirectory, cacheSize);
      builder.cache(cache);
      OkHttpEngine.OkHttpFactory factory = new OkHttpEngine.OkHttpFactory(builder);
      TileSource hillshadingSource =  DefaultSources.HIKEBIKE_HILLSHADE
            .httpFactory(factory)
            .zoomMin(1)
            .zoomMax(16)
            .build();
      
      MapRenderer.setBackgroundColor(0xff888888);
      _layer_HillShadingLayer = new BitmapTileLayer(mMap, hillshadingSource, 4000000);
      
      mMap.layers().add(_layer_HillShadingLayer);


      //MapRenderer.setBackgroundColor(0xff888888);
      //mLayer = new BitmapTileLayer(mMap, DefaultSources.OPENSTREETMAP.build(), 4000000);
      //mMap.layers().add(mLayer);

   }

   public static void main(String[] args) {
      GdxMapApp.init();
      GdxMapApp.run(new BitmapTileTest(), null, 256);
   }
}
