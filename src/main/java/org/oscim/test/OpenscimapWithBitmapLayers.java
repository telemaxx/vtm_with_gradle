package org.oscim.test;


import org.oscim.gdx.GdxMapApp;
import org.oscim.layers.tile.bitmap.BitmapTileLayer;
import org.oscim.layers.tile.vector.VectorTileLayer;
import org.oscim.layers.tile.vector.labeling.LabelLayer;
import org.oscim.theme.VtmThemes;
import org.oscim.tiling.TileSource;
import org.oscim.tiling.source.OkHttpEngine;
import org.oscim.tiling.source.bitmap.DefaultSources;
import org.oscim.tiling.source.oscimap4.OSciMap4TileSource;


public class OpenscimapWithBitmapLayers extends GdxMapApp {

   private BitmapTileLayer cyclingLayer = null;
   private BitmapTileLayer mtbLayer = null;
   private BitmapTileLayer hikingLayer = null;


   @Override
   public void createLayers() {

      TileSource tileSource = OSciMap4TileSource.builder()
            .httpFactory(new OkHttpEngine.OkHttpFactory())
            .build();
      VectorTileLayer l = mMap.setBaseMap(tileSource);
      mMap.setTheme(VtmThemes.DEFAULT);

      cyclingLayer = new BitmapTileLayer(mMap, DefaultSources.OPENSTREETMAP
            .httpFactory(new OkHttpEngine.OkHttpFactory())
            .url("http://tile.waymarkedtrails.org/cycling")
            .build());

      //mMap.layers().add(cyclingLayer);

      mMap.layers().add(new LabelLayer(mMap, l));

      mMap.setMapPosition(52.25, 9.61, 1 << 13);

   }


   public static void main(String[] args) {
      GdxMapApp.init();
      GdxMapApp.run(new OpenscimapWithBitmapLayers());
   }

}
