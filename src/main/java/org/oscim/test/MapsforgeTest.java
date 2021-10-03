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


import org.oscim.backend.CanvasAdapter;
import org.oscim.backend.canvas.Bitmap;
import org.oscim.core.MapPosition;
import org.oscim.core.Tile;
import org.oscim.gdx.GdxMapApp;

import org.oscim.layers.tile.buildings.BuildingLayer;
import org.oscim.layers.tile.buildings.S3DBLayer;
import org.oscim.layers.tile.vector.VectorTileLayer;
import org.oscim.layers.tile.vector.labeling.LabelLayer;

import org.oscim.renderer.BitmapRenderer;

import org.oscim.renderer.GLViewport;
import org.oscim.scalebar.DefaultMapScaleBar;
import org.oscim.scalebar.ImperialUnitAdapter;
import org.oscim.scalebar.MapScaleBar;
import org.oscim.scalebar.MapScaleBarLayer;
import org.oscim.scalebar.MetricUnitAdapter;

import org.oscim.theme.VtmThemes;
import org.oscim.tiling.source.mapfile.MapFileTileSource;
import org.oscim.tiling.source.mapfile.MapInfo;

import java.io.File;

public class MapsforgeTest extends GdxMapApp {

	protected Bitmap myBitmap;
	private File mapFile;
	private boolean s3db;
	float angle = 0;
	private VectorTileLayer l;
	private BuildingLayer buildingLayer;
	private S3DBLayer s3dbLayer;
	private LabelLayer labelLayer;

	MapsforgeTest(final File mapFile) {
		this(mapFile, false);
		System.out.println("building layer");
	}

	MapsforgeTest(final File mapFile, final boolean s3db) {
		this.mapFile = mapFile;
		this.s3db = s3db;
		System.out.println("s3db layer");
	}


	// static File getMapFile(String[] args) {
	static File getMapFile(final String string) {
		/*   if (args.length == 0) {
            throw new IllegalArgumentException("missing argument: <mapFile>");
        }*/

		final File file = new File(string);
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

	public static void main(final String[] args) {
      System.out.println("main, starting init...");
		GdxMapApp.init();
		//GdxMapApp.run(new MapsforgeTest(getMapFile(args)));
      System.out.println("main, starting run...");
		GdxMapApp.run(new MapsforgeTest(getMapFile("E:\\OfflineMaps\\mapfiles\\mf\\niedersachsen_V5.map"), false));
		//GdxMapApp.run(new MapsforgeTest(getMapFile("D:\\OfflineMaps\\mapfiles\\mf\\germany.map"), true));
		//GdxMapApp.run(new MapsforgeTest(getMapFile("C:\\Users\\top\\BTSync\\Exchange\\gps_tools\\maps\\Switzerland_ML.map")));
	}

	@Override
	public void createLayers() {
      System.out.println("DPI: " + CanvasAdapter.DEFAULT_DPI); //DPI: 160.0
      //CanvasAdapter.dpi = (int) (1.75 * CanvasAdapter.DEFAULT_DPI);
      //Tile.SIZE = Tile.calculateTileSize();

      CanvasAdapter.userScale = 3.0f;  // 3.0f
      CanvasAdapter.textScale = 0.75f;  //0.75f

		final MapFileTileSource tileSource = new MapFileTileSource();
		tileSource.setMapFile(mapFile.getAbsolutePath());
		tileSource.setPreferredLanguage("zh");

		l = mMap.setBaseMap(tileSource);
		loadTheme(null);
		//loadTheme("elv-mtb");


		mMap.getEventLayer().enableRotation(false);  //wont work
		mMap.getEventLayer().enableTilt(false); //dito

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
		//mMap.layers().add(labelLayer);

		final DefaultMapScaleBar mapScaleBar = new DefaultMapScaleBar(mMap,1f);

		mapScaleBar.setScaleBarMode(DefaultMapScaleBar.ScaleBarMode.BOTH);
		mapScaleBar.setDistanceUnitAdapter(MetricUnitAdapter.INSTANCE);
		mapScaleBar.setSecondaryDistanceUnitAdapter(ImperialUnitAdapter.INSTANCE);
		mapScaleBar.setScaleBarPosition(MapScaleBar.ScaleBarPosition.BOTTOM_LEFT);

		final MapScaleBarLayer mapScaleBarLayer = new MapScaleBarLayer(mMap, mapScaleBar);

		final BitmapRenderer renderer = mapScaleBarLayer.getRenderer();


		renderer.setPosition(GLViewport.Position.BOTTOM_LEFT);
		renderer.setOffset(5, 0);

		mMap.layers().add(mapScaleBarLayer);


		final MapInfo info = tileSource.getMapInfo();
		final MapPosition pos = new MapPosition();
		pos.setByBoundingBox(info.boundingBox, Tile.SIZE * 4, Tile.SIZE * 4);

		mMap.setMapPosition(53.08, 8.82, 1 << 17);
		//mMap.setMapPosition(pos);

		/*
		System.out.println("layers before: " + mMap.layers().toString() + " size: " + mMap.layers().size());
		//for( int n = 0; n < mMap.layers().size(); n++) {
		for( int n = mMap.layers().size() - 1; n > 0 ;n--) {
		   System.out.println("layer " + n + "/" + mMap.layers().size()+ " " + mMap.layers().get(n).toString());
		   mMap.layers().remove(n);
		}
		System.out.println("layers after: " + mMap.layers().toString() + " size: " + mMap.layers().size());
		mMap.layers().add(l);
		mMap.layers().add(buildingLayer);
		mMap.layers().add(labelLayer);
		mMap.layers().add(mapScaleBarLayer);
		mMap.clearMap();
		System.out.println("layers final: " + mMap.layers().toString() + " size: " + mMap.layers().size());
		*/
	}

	@Override
	public void dispose() {
		//MapPreferences.saveMapPosition(mMap.getMapPosition());
		super.dispose();
	}

	protected void loadTheme(final String styleId) {
		mMap.setTheme(VtmThemes.DEFAULT);
	}
}
