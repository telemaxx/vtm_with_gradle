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

	//public static String mapFile = "C:\\OfflineMaps\\mapfiles\\download.mapsforge.org\\switzerland_V5.map";
	public static String mapFile = "C:\\Users\\top\\BTSync\\oruxmaps\\mapfiles\\Germany_North_ML.map";
	//public static String mapFile = "C:\\Users\\top\\BTSync\\Exchange\\gps_tools\\maps\\hamburg_ML.map";
	//public static String mapFile = "C:\\Users\\top\\BTSync\\oruxmaps\\mapfiles\\niedersachsen_V5.map";
	//public static String mapFile = "C:\\OfflineMaps\\mapfiles\\www.openandromaps.org\\Switzerland_ML.map";
	public static String themeFile = "C:\\Users\\top\\BTSync\\oruxmaps\\mapstyles\\ELV4\\Elevate.xml";
	//public String multithemeString = "elv-mtb,elv-hiking,elv-cycling";
	public String multithemeString = "elv-cycling";

	public static VectorTileLayer _l;
	public static TileSource _tileSourceOnline;
	//public static TileSource tileSourceS3DB;
	//public static MapFileTileSource _tileSourceOffline;
	
	private MapFileTileSource _tileSourceOffline;
	private MultiMapFileTileSource _tileSourceOfflineMM;
	
	//private BitmapTileLayer mLayer = null;
	private BitmapTileLayer _layer_HillShadingLayer = null;
	private BitmapTileLayer _layer_OSM_Layer = null;
	
	//private OpenResult _mOpenResult;
	//private FileFilter MapFileExtension;
	
	//private S3DBLayer l_s3db;

	private boolean s3db;
	
	private JFrame _pleaseWaitFrame;
	private JDialog _pleaseWaitDialog;
	private JLabel _pleaseWaitLabel;

	
	float angle = 0;

	MapsforgeTestOnlineOffline(String mapFile) {
		this(mapFile, false);
	}

	MapsforgeTestOnlineOffline(String mapFile, boolean s3db) {
		this.mapFile = mapFile;
		this.s3db = s3db;
	}

	/**
	 * please wait window
	 * @param show true or false
	 * {@link http://www.java2s.com/Tutorials/Java/Swing_How_to/JFrame/Create_Modeless_and_model_Dialog_from_JFrame.htm}
	 */
	public void pleaseWaitWindow(Boolean show) {
		_pleaseWaitDialog.setVisible(show);
		_pleaseWaitFrame.setVisible(show);
	}

   public void pleaseWaitWindow(Boolean show, String Text) {
      _pleaseWaitLabel.setText(Text);
      pleaseWaitWindow(show);
   }	
	
	public void init_pleaseWaitWindow() {
	   _pleaseWaitLabel = new JLabel("loading theme, please be patient");
	   
      _pleaseWaitFrame = new JFrame();
      _pleaseWaitFrame.setUndecorated(true);
      _pleaseWaitFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      _pleaseWaitFrame.pack();

      _pleaseWaitDialog = new JDialog(_pleaseWaitFrame, "Please Wait...", ModalityType.MODELESS);
      _pleaseWaitDialog.setLayout(new FlowLayout());
      _pleaseWaitDialog.add(_pleaseWaitLabel);
      _pleaseWaitDialog.add(Box.createRigidArea(new Dimension(200, 50)));
      _pleaseWaitDialog.pack();
      _pleaseWaitDialog.setLocationRelativeTo(null);
   }
	
	@Override
	public void createLayers() {
	   init_pleaseWaitWindow();
			    
		MapRenderer.setBackgroundColor(0xff888888);

		_tileSourceOfflineMM = getMapFile(mapFile);
		
		//MapFileTileSource _tileSourceOfflineMM = new MapFileTileSource();
		//tileSourceOfflineMM = new MultiMapFileTileSource(MultiMapDataStore.DataPolicy.RETURN_ALL);
		
		_tileSourceOffline = new MapFileTileSource();
		_tileSourceOffline.setMapFile(mapFile);
		
		//_tileSourceOffline.setPreferredLanguage("de");
	
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
      // Cache the tiles into file system
      File cacheDirectory = new File(System.getProperty("java.io.tmpdir") + "vtm-cache" , UUID.randomUUID().toString());
      System.out.println("caching to: " + System.getProperty("java.io.tmpdir") + "vtm-cache");
      int cacheSize = 32 * 1024 * 1024; // 32 MB
      Cache cache = new Cache(cacheDirectory, cacheSize);
      builder.cache(cache);
		
      OkHttpEngine.OkHttpFactory factory = new OkHttpEngine.OkHttpFactory(builder);
 
		TileSource hillshadingSource =  DefaultSources.HIKEBIKE_HILLSHADE
				.httpFactory(factory)
				.zoomMin(1)
				.zoomMax(16)
				.build();

      _tileSourceOnline = OSciMap4TileSource.builder()
            .httpFactory(factory)
            .build();

		TileSource TileSourceS3DB = OSciMap4TileSource.builder()
            .httpFactory(factory)
            .url("http://opensciencemap.org/tiles/s3db")
            .zoomMin(16)
            .zoomMax(16)
            .build();



		//S3DBLayer l_s3db = new S3DBLayer(mMap,TileSourceS3DB);

		//l = mMap.setBaseMap(tileSourceOnline);
		//l = mMap.setBaseMap(tileSourceOffline);

		//ITileCache cache;
		//_layer_HillShadingLayer = new BitmapTileLayer(mMap, DefaultSources.HIKEBIKE_HILLSHADE.build(), 4000000);
		_layer_HillShadingLayer = new BitmapTileLayer(mMap, hillshadingSource, 4000000);
		
		mMap.layers().add(_layer_HillShadingLayer);
		
		_layer_OSM_Layer = new BitmapTileLayer(mMap, DefaultSources.OPENSTREETMAP.build(), 4000000);
		//mMap.layers().add(_layer_OSM_Layer);

		if (s3db) {
			//_l = mMap.setBaseMap(_tileSourceOffline);
			_l = mMap.setBaseMap(_tileSourceOfflineMM);
			mMap.layers().add(new S3DBLayer(mMap, _l));}
		else {
			_l = mMap.setBaseMap(_tileSourceOnline);
			//mMap.layers().add(l_s3db);
			mMap.layers().add(new BuildingLayer(mMap, _l));}

		loadTheme(null);
		
		mMap.layers().add(new LabelLayer(mMap, _l));

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
	 * gget a sorted list with mapsforgemap files
	 * @param <MultiMapDataStore>
	 * @param filename
	 * @return files[]
	 * {@link http://www.avajava.com/tutorials/lessons/how-do-i-sort-an-array-of-files-according-to-their-sizes.html}
	 * 
	 */
	public MultiMapFileTileSource getMapFile(String filename) {
		int minZoom = 8;
		int maxZoom = 15;
		File file = new File(filename);
		File directory = new File(file.getParent());
		File[] files = directory.listFiles(new FilenameFilter() {
		    public boolean accept(File directory, String name) {
		        return name.toLowerCase().endsWith(".map");
		    }
		});
		
		System.out.println("basepath: " + directory);

		//Arrays.sort(files, SizeFileComparator.SIZE_COMPARATOR); // sort mapsfiles size
		
		MultiMapFileTileSource mMFileTileSource = new MultiMapFileTileSource (minZoom,maxZoom);   //DataPolicy.RETURN_ALL);
		MapFileTileSource tileSourceOfflinePrimary = new MapFileTileSource(minZoom,maxZoom);
		tileSourceOfflinePrimary.setMapFile(file.getAbsolutePath());
		tileSourceOfflinePrimary.setPreferredLanguage("de");
		mMFileTileSource.add(tileSourceOfflinePrimary);   // adding primary map first
		//System.out.println("Adding: " + file + " size: " +  	FileUtils.byteCountToDisplaySize(file.length()) + "(" + file.length() + " bytes)");
		System.out.println("Adding: " + file + " size: " +  file.length() + " bytes)");
		for (File f : files)
      {
			if(checkMapFile(f)) {
				if(!f.getAbsolutePath().equalsIgnoreCase(filename)) { //add all mapfiles except the primary map, which is already added
					MapFileTileSource tileSourceOffline = new MapFileTileSource(minZoom,maxZoom);
					tileSourceOffline.setMapFile(f.getAbsolutePath());
					mMFileTileSource.add(tileSourceOffline);
					//long size = FileUtils.sizeOf(f);
					//System.out.println("Adding: " + f + " size: " +  	FileUtils.byteCountToDisplaySize(size) + "(" + size + " bytes)");
					System.out.println("Adding: " + f + " size: " +  f.length() + " bytes)");
				}
			}
      }	
		return mMFileTileSource;
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
			_l = mMap.setBaseMap(_tileSourceOffline);
			//loadTheme("1");
			//mMap.clearMap();
			pleaseWaitWindow(true);
			return true;
		case Input.Keys.B:
			_l = mMap.setBaseMap(_tileSourceOnline);
			//loadTheme("2");
			//mMap.clearMap();
			pleaseWaitWindow(false);
			return true;
      case Input.Keys.C:
         _l = mMap.setBaseMap(_tileSourceOnline);
         //loadTheme("2");
         //mMap.clearMap();
         pleaseWaitWindow(true,"oooops");
         return true;			
			
			
		case Input.Keys.NUM_9:
			return true;
		}
		
		return super.onKeyDown(keycode);
	}

	public static void main(String[] args) {
		GdxMapApp.init();
		GdxMapApp.run(new MapsforgeTestOnlineOffline(mapFile,true));
		//GdxMapApp.run(new MapsforgeTestOnlineOffline(mapFile,false));
	}
}
