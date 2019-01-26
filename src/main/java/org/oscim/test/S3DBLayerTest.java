package org.oscim.test;

import org.oscim.gdx.GdxMapApp;
import org.oscim.layers.tile.bitmap.BitmapTileLayer;
import org.oscim.layers.tile.buildings.S3DBLayer;
import org.oscim.layers.tile.vector.VectorTileLayer;
import org.oscim.layers.tile.vector.labeling.LabelLayer;
import org.oscim.theme.VtmThemes;
import org.oscim.tiling.TileSource;
import org.oscim.tiling.source.bitmap.DefaultSources;
import org.oscim.tiling.source.oscimap4.OSciMap4TileSource;

public class S3DBLayerTest extends GdxMapApp {

	VectorTileLayer l;
	
	@Override
	public void createLayers() {

		//mBaseLayer = mMap.setBaseMap(mTileSource);
		
		
		TileSource ts = OSciMap4TileSource.builder()
			    .url("http://opensciencemap.org/tiles/s3db")
			    .zoomMin(8)
			    .zoomMax(16)
			    .build();
		
		l = mMap.setBaseMap(ts);
		
		mMap.setTheme(VtmThemes.DEFAULT);

		S3DBLayer tl = new S3DBLayer(mMap,l);
				//mMap, ts, true, false);
		
		mMap.layers().add(tl);
		mMap.layers().add(new LabelLayer(mMap, l));
		mMap.setMapPosition(53.08, 8.82, 1 << 17);

	}

	public static void main(String[] args) {
		init();
		run(new S3DBLayerTest(), null, 400);
	}
}