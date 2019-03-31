package org.oscim.test;

import org.oscim.gdx.GdxMapApp;
import org.oscim.layers.tile.bitmap.BitmapTileLayer;
import org.oscim.layers.tile.buildings.BuildingLayer;
import org.oscim.layers.tile.buildings.S3DBLayer;
import org.oscim.layers.tile.buildings.S3DBTileLayer;
import org.oscim.layers.tile.vector.VectorTileLayer;
import org.oscim.layers.tile.vector.labeling.LabelLayer;
import org.oscim.theme.VtmThemes;
import org.oscim.tiling.TileSource;
import org.oscim.tiling.source.OkHttpEngine;
import org.oscim.tiling.source.bitmap.DefaultSources;
import org.oscim.tiling.source.oscimap4.OSciMap4TileSource;

public class S3DBLayerTest extends GdxMapApp {

	VectorTileLayer l;
	
	@Override
	public void createLayers() {

		//mBaseLayer = mMap.setBaseMap(mTileSource);
		
      TileSource tileSource = OSciMap4TileSource.builder()
            .httpFactory(new OkHttpEngine.OkHttpFactory())
            .build();
      VectorTileLayer l = mMap.setBaseMap(tileSource);
      mMap.setTheme(VtmThemes.DEFAULT);

		TileSource ts_s3db = OSciMap4TileSource.builder()
		      .httpFactory(new OkHttpEngine.OkHttpFactory())
			    .url("http://opensciencemap.org/tiles/s3db")
			    .zoomMin(16)
			    .zoomMax(16)
			    .build();

		S3DBTileLayer layerBuilding = new S3DBTileLayer(mMap, ts_s3db, false, true);  //StDBTilelayer, not s3dblayer
		//BuildingLayer layerBuilding = new BuildingLayer(mMap, l);

		mMap.layers().add(layerBuilding);
		mMap.layers().add(new LabelLayer(mMap, l));
		mMap.setMapPosition(53.08, 8.82, 1 << 17);

	}

	public static void main(String[] args) {
		init();
		run(new S3DBLayerTest(), null, 400);
	}
}