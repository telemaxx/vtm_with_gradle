package org.oscim.test;


import org.oscim.backend.CanvasAdapter;
import org.oscim.backend.canvas.Bitmap;
import org.oscim.core.GeoPoint;
import org.oscim.core.MapPosition;
import org.oscim.core.Tile;
import org.oscim.gdx.GdxMapApp;

import org.oscim.layers.tile.buildings.BuildingLayer;
import org.oscim.layers.tile.buildings.S3DBLayer;
import org.oscim.layers.tile.vector.VectorTileLayer;
import org.oscim.layers.tile.vector.labeling.LabelLayer;
import org.oscim.map.Animator;
import org.oscim.renderer.BitmapRenderer;

import org.oscim.renderer.GLViewport;
import org.oscim.scalebar.DefaultMapScaleBar;
import org.oscim.scalebar.ImperialUnitAdapter;
import org.oscim.scalebar.MapScaleBar;
import org.oscim.scalebar.MapScaleBarLayer;
import org.oscim.scalebar.MetricUnitAdapter;

import org.oscim.theme.VtmThemes;
import org.oscim.theme.StreamRenderTheme;
import org.oscim.theme.XmlRenderThemeMenuCallback;
import org.oscim.theme.XmlRenderThemeStyleLayer;
import org.oscim.theme.XmlRenderThemeStyleMenu;

import org.oscim.tiling.source.mapfile.MapFileTileSource;
import org.oscim.tiling.source.mapfile.MapInfo;

import java.io.File;
import java.util.Set;

public class Mapsforge_S3DB_012  extends GdxMapApp {
   
   protected Bitmap myBitmap;
   private File mapFile;
   private boolean s3db;
   float angle = 0;
   private VectorTileLayer l;
   private BuildingLayer buildingLayer;
   private S3DBLayer s3dbLayer;
   private LabelLayer labelLayer;
   private MapScaleBarLayer _mapScaleBarLayer;

   
   Mapsforge_S3DB_012(File mapFile) {
      this(mapFile, false);
      System.out.println("building layer");
   }

   Mapsforge_S3DB_012(File mapFile, boolean s3db) {
      this.mapFile = mapFile;
      this.s3db = s3db;
      System.out.println("s3db layer");
   } 
   
   @Override
   public void createLayers() {
      
      MapFileTileSource tileSource = new MapFileTileSource();
      tileSource.setMapFile(mapFile.getAbsolutePath());
      tileSource.setPreferredLanguage("zh");

      VectorTileLayer l = mMap.setBaseMap(tileSource);
      //l = mMap.setBaseMap(tileSource);
      loadTheme(null);
      //loadTheme("elv-mtb");

      mMap.setMapPosition(47.2266239f, 8.8184374f, 1 << 13);



      if (s3db) {
         System.out.println("adding s3db layer");
         s3dbLayer = new S3DBLayer(mMap, l, true);

         mMap.layers().add(s3dbLayer);}
      else {
         System.out.println("adding building layer");
         buildingLayer = new BuildingLayer(mMap, l, false, true);
         
         mMap.layers().add(buildingLayer);
         }
      
      labelLayer = new LabelLayer(mMap, l);
      mMap.layers().add(labelLayer);

      DefaultMapScaleBar mapScaleBar = new DefaultMapScaleBar(mMap);
      mapScaleBar.setScaleBarMode(DefaultMapScaleBar.ScaleBarMode.BOTH);
      mapScaleBar.setDistanceUnitAdapter(MetricUnitAdapter.INSTANCE);
      mapScaleBar.setSecondaryDistanceUnitAdapter(ImperialUnitAdapter.INSTANCE);
      mapScaleBar.setScaleBarPosition(MapScaleBar.ScaleBarPosition.BOTTOM_LEFT);


      MapScaleBarLayer mapScaleBarLayer = new MapScaleBarLayer(mMap, mapScaleBar);
      BitmapRenderer renderer = mapScaleBarLayer.getRenderer();
      renderer.setPosition(GLViewport.Position.BOTTOM_LEFT);
      renderer.setOffset(5, 0);
      mMap.layers().add(mapScaleBarLayer);
      
      MapPosition pos = new MapPosition(47.2266239, 8.8184374, 1 << 15);
      pos.set(47.2266239, 8.8184374, 1 << 15, 180, 45, 45);
//      MapInfo info = tileSource.getMapInfo();
//      if (pos == null || !info.boundingBox.contains(pos.getGeoPoint())) {
//          pos = new MapPosition();
//          pos.setByBoundingBox(info.boundingBox, Tile.SIZE * 4, Tile.SIZE * 4);
//      }
      mMap.setMapPosition(pos);

      loadTheme(null);
      
   }
   
   @Override
   public void dispose() {
      //MapPreferences.saveMapPosition(mMap.getMapPosition());
      super.dispose();
   }
   
   // static File getMapFile(String[] args) {
   static File getMapFile(String string) {
      /*   if (args.length == 0) {
            throw new IllegalArgumentException("missing argument: <mapFile>");
        }*/

      File file = new File(string);     
      // File file = new File(args[0]);
      if (!file.exists()) {
         throw new IllegalArgumentException("file does not exist: " + file);
      } else if (!file.isFile()) {
         throw new IllegalArgumentException("not a file: " + file);
      } else if (!file.canRead()) {
         throw new IllegalArgumentException("cannot read file: " + file);
      }
      return file;
   }
   
   protected void loadTheme(final String styleId) {
      mMap.setTheme(VtmThemes.DEFAULT);
   }
   
   public static void main(String[] args) {
      GdxMapApp.init();
      //GdxMapApp.run(new MapsforgeTest(getMapFile(args)));
      //GdxMapApp.run(new MapsforgeTest(getMapFile("C:\\Users\\top\\BTSync\\oruxmaps\\mapfiles\\Germany_North_ML.map"), false));
      //GdxMapApp.run(new MapsforgeTest(getMapFile("D:\\OfflineMaps\\mapfiles\\mf\\germany.map"), true));
      //GdxMapApp.run(new MapsforgeTest(getMapFile("D:\\OfflineMaps\\mapfiles\\mf\\switzerland_V5.map"),true)); 
      GdxMapApp.run(new MapsforgeTest(getMapFile("C:\\mf_maps\\switzerland_V5.map"),true));
      
   }
   
   
   
}
