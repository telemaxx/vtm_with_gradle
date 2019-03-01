/*
 * Copyright 2016-2018 devemux86
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

import org.oscim.core.MapPosition;
import org.oscim.core.Tile;
import org.oscim.gdx.GdxMapApp;
//import org.oscim.gdx.poi3d.Poi3DLayer;  //from 0.11

import org.oscim.layers.tile.bitmap.BitmapTileLayer;
import org.oscim.layers.tile.buildings.BuildingLayer;
import org.oscim.layers.tile.buildings.S3DBLayer;
import org.oscim.layers.tile.vector.VectorTileLayer;
import org.oscim.layers.tile.vector.labeling.LabelLayer;

import org.oscim.renderer.BitmapRenderer;
import org.oscim.renderer.GLViewport;
import org.oscim.renderer.MapRenderer;

import org.oscim.scalebar.DefaultMapScaleBar;
import org.oscim.scalebar.ImperialUnitAdapter;
import org.oscim.scalebar.MapScaleBar;
import org.oscim.scalebar.MapScaleBarLayer;
import org.oscim.scalebar.MetricUnitAdapter;

import org.oscim.tiling.source.mapfile.MultiMapFileTileSource;
import org.oscim.tiling.ITileCache;
import org.oscim.tiling.TileSource;
import org.oscim.tiling.TileSource.OpenResult;
import org.oscim.tiling.source.OkHttpEngine;
import org.oscim.tiling.source.bitmap.DefaultSources;
import org.oscim.tiling.source.mapfile.MapFileTileSource;
import org.oscim.tiling.source.mapfile.MapInfo;
import org.oscim.tiling.source.oscimap4.OSciMap4TileSource;

import com.badlogic.gdx.Input;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

import org.oscim.theme.VtmThemes;
import org.oscim.theme.StreamRenderTheme;
import org.oscim.theme.XmlRenderThemeMenuCallback;
import org.oscim.theme.XmlRenderThemeStyleLayer;
import org.oscim.theme.XmlRenderThemeStyleMenu;

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
//import java.io.FileFilter;
import java.io.FilenameFilter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

//import org.apache.commons.io.FileUtils;
//import org.apache.commons.io.comparator.SizeFileComparator;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class MapsforgeTestOnlineOffline extends GdxMapApp {

	//private static String mapFile = "C:\\OfflineMaps\\mapfiles\\download.mapsforge.org\\switzerland_V5.map";
	//private static String mapFile = "C:\\Users\\top\\BTSync\\oruxmaps\\mapfiles\\Germany_North_ML.map";
	//private static String mapFile = "C:\\Users\\top\\BTSync\\Exchange\\gps_tools\\maps\\hamburg_ML.map";
	private static String mapFile = "C:\\OfflineMaps\\mapfiles\\download.mapsforge.org\\niedersachsen_V5.map";
	//private static String mapFile = "C:\\OfflineMaps\\mapfiles\\www.openandromaps.org\\Switzerland_ML.map";
	private static String themeFile = "C:\\Users\\top\\BTSync\\oruxmaps\\mapstyles\\ELV4\\Elevate.xml";
	//private String multithemeString = "elv-mtb,elv-hiking,elv-cycling";
	private String multithemeString = "elv-cycling";

	private static TileSource _tileSourceOnline;
	private static TileSource _tileSourceS3DB = null;
	
	private MapFileTileSource _tileSourceOffline = null;

	private BitmapTileLayer _tilesourceHillshading = null;
	private BitmapTileLayer _layer_HillShadingLayer = null;
	private TileSource hillshadingSource;
	
	private Boolean _isOnline = null;
	private S3DBLayer _l_s3db = null;
	private BuildingLayer _l_building = null;
	
	private VectorTileLayer _l = null;
	private LabelLayer _l_label = null;
	
	MapScaleBarLayer _mapScaleBarLayer;

	MapsforgeTestOnlineOffline(String mapFile, boolean runOnline) {
	   if(runOnline) {
         System.out.println("isonline: ");
         this.mapFile = mapFile;
         this._isOnline = runOnline;
	   } else {
	      System.out.println("isoffline: ");
	      this.mapFile = mapFile;
	      this._isOnline = runOnline;
	   }
  
	}


	
	@Override
	public void createLayers() {
			    
		MapRenderer.setBackgroundColor(0xff888888);
		
		_tileSourceOffline = new MapFileTileSource();
		_tileSourceOffline.setMapFile(mapFile);
	
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
      // Cache the tiles into file system
      File cacheDirectory = new File(System.getProperty("java.io.tmpdir") + "vtm-cache");
      System.out.println("caching to: " + cacheDirectory.toString());
      int cacheSize = 32 * 1024 * 1024; // 32 MB
      Cache cache = new Cache(cacheDirectory, cacheSize);
      builder.cache(cache);
		
      OkHttpEngine.OkHttpFactory factory = new OkHttpEngine.OkHttpFactory(builder);
 
		hillshadingSource =  DefaultSources.HIKEBIKE_HILLSHADE
				.httpFactory(factory)
				.zoomMin(1)
				.zoomMax(16)
				.build();

      _tileSourceOnline = OSciMap4TileSource.builder()
            .httpFactory(factory)
            .build();

		//_l = mMap.setBaseMap(_tileSourceOffline);

      
      

		_layer_HillShadingLayer = new BitmapTileLayer(mMap, hillshadingSource, 4000000);
		mMap.layers().add(_layer_HillShadingLayer);
		

		
		if (_isOnline) {
		   System.out.println("Onlinemap, using building layer");
         _l = mMap.setBaseMap(_tileSourceOnline);
         _l_building = new BuildingLayer(mMap, _l);
         mMap.layers().add(_l_building);
         //_l_s3db = new S3DBLayer(mMap,_l);
         }  
		else {
		   System.out.println("Onlinemap, using s3db layer");
         _l = mMap.setBaseMap(_tileSourceOffline);
         _l_s3db = new S3DBLayer(mMap,_l);
         mMap.layers().add(_l_s3db);
         //_l_building = new BuildingLayer(mMap, _l);
         }

/*		_l_building = new BuildingLayer(mMap, _l);
		
		_l_s3db = new S3DBLayer(mMap,_l);
		mMap.layers().add(_l_s3db);
*/
		_l_label = new LabelLayer(mMap, _l);  
		mMap.layers().add(_l_label);


		
		


		DefaultMapScaleBar mapScaleBar = new DefaultMapScaleBar(mMap);
		mapScaleBar.setScaleBarMode(DefaultMapScaleBar.ScaleBarMode.BOTH);
		mapScaleBar.setDistanceUnitAdapter(MetricUnitAdapter.INSTANCE);
		mapScaleBar.setSecondaryDistanceUnitAdapter(ImperialUnitAdapter.INSTANCE);
		mapScaleBar.setScaleBarPosition(MapScaleBar.ScaleBarPosition.BOTTOM_LEFT);

		_mapScaleBarLayer = new MapScaleBarLayer(mMap, mapScaleBar);
		BitmapRenderer renderer = _mapScaleBarLayer.getRenderer();

		renderer.setPosition(GLViewport.Position.BOTTOM_LEFT);
		renderer.setOffset(5, 0);
		mMap.layers().add(_mapScaleBarLayer);

		loadTheme(null);
		
		mMap.setMapPosition(52.4, 9.79, 1 << 18);
		 
		MapPosition pos = new MapPosition();

		pos.set(52.4, 9.79, 1 << 18, 180, 45, 45);

		
		
	}

	@Override
	public void dispose() {
		//MapPreferences.saveMapPosition(mMap.getMapPosition());
		super.dispose();
	}

	protected void loadTheme(final String styleId) {
		mMap.setTheme(VtmThemes.DEFAULT);
	}

	

	/**
	 * Checks if a given file is a valid mapsforge file
	 * @param file2check
	 * @return true, when file is ok
	 */
	public Boolean checkMapFile(File file2check) {
		Boolean result = false;
		MapFileTileSource mapFileSource = new MapFileTileSource();
		mapFileSource.setMapFile(file2check.getAbsolutePath());
		OpenResult mOpenResult = mapFileSource.open();
		mapFileSource.close();
		result = mOpenResult.isSuccess();
		if (!mOpenResult.isSuccess()) {
			System.out.println("not adding: " + file2check.getAbsolutePath() + " " + mOpenResult.getErrorMessage());
		}
		return result;
	}
	
	@Override
	protected boolean onKeyDown(int keycode) {
		switch (keycode) {
		case Input.Keys.NUM_1:
			//loadTheme("1");
			mMap.setTheme(VtmThemes.DEFAULT);
			//mMap.clearMap();
			return true;
		case Input.Keys.NUM_2:
			//loadTheme("2");
			mMap.setTheme(VtmThemes.TRONRENDER);
			//mMap.clearMap();
			return true;
		case Input.Keys.NUM_3:
			mMap.setTheme(VtmThemes.OSMARENDER);
			return true;
		case Input.Keys.A:
		   System.out.println("Offlinemap");
		   
		   System.out.println("layers before: " + mMap.layers().toString() + " size: " + mMap.layers().size());
		   for( int n = mMap.layers().size() - 1; n > 0 ;n--) { 
		      System.out.println("layer " + n + "/" + mMap.layers().size()+ " " + mMap.layers().get(n).toString());
		      mMap.layers().remove(n);
		   }
		   System.out.println("layers after: " + mMap.layers().toString() + " size: " + mMap.layers().size());

		   _l = mMap.setBaseMap(_tileSourceOffline);
		   //mMap.layers().add(_l);
		   mMap.layers().add(_layer_HillShadingLayer);
		   _l_building = new S3DBLayer(mMap, _l);
		   mMap.layers().add(_l_building);
		   _l_label = new LabelLayer(mMap, _l);
		   mMap.layers().add(_l_label);
		   mMap.layers().add(_mapScaleBarLayer);
		   

			mMap.clearMap();
         loadTheme("1");
			return true;
		case Input.Keys.B:
		   System.out.println("Onlinemap");
	       
         System.out.println("layers before: " + mMap.layers().toString() + " size: " + mMap.layers().size());
         for( int n = mMap.layers().size() - 1; n > 0 ;n--) { 
            System.out.println("layer " + n + "/" + mMap.layers().size()+ " " + mMap.layers().get(n).toString());
            mMap.layers().remove(n);
         }
         System.out.println("layers after: " + mMap.layers().toString() + " size: " + mMap.layers().size());
         
			/*if (mMap.layers().contains(_l_s3db)) {
			   mMap.layers().remove(_l_s3db);
			}*/
			_l = mMap.setBaseMap(_tileSourceOnline);
			
         //mMap.layers().add(_l);
			_layer_HillShadingLayer =  new BitmapTileLayer(mMap, hillshadingSource, 4000000);
			mMap.layers().add(_layer_HillShadingLayer);
			_l_building = new BuildingLayer(mMap, _l);
         mMap.layers().add(_l_building);
         _l_label = new LabelLayer(mMap, _l);
         mMap.layers().add(_l_label);
         mMap.layers().add(_mapScaleBarLayer);

         mMap.clearMap();
			loadTheme("2");
			return true;
      case Input.Keys.C:
         _l = mMap.setBaseMap(_tileSourceOnline);
         loadTheme("1");
         mMap.clearMap();
         return true;			
		case Input.Keys.NUM_9:
			return true;
		}
		
		return super.onKeyDown(keycode);
	}
	



	public static void main(String[] args) {
		GdxMapApp.init();
		//GdxMapApp.run(new MapsforgeTestOnlineOffline(mapFile,true));
		GdxMapApp.run(new MapsforgeTestOnlineOffline(mapFile,false));
	}
}
