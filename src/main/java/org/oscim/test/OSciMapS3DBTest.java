package org.oscim.test;

import org.oscim.gdx.GdxMapApp;
import org.oscim.layers.tile.buildings.S3DBTileLayer;
import org.oscim.layers.tile.vector.VectorTileLayer;
import org.oscim.layers.tile.vector.labeling.LabelLayer;
import org.oscim.theme.VtmThemes;
import org.oscim.tiling.TileSource;
import org.oscim.tiling.source.OkHttpEngine;
import org.oscim.tiling.source.oscimap4.OSciMap4TileSource;

public class OSciMapS3DBTest extends GdxMapApp {

    @Override
    public void createLayers() {
        TileSource tileSource = OSciMap4TileSource.builder()
                .httpFactory(new OkHttpEngine.OkHttpFactory())
                .build();
        VectorTileLayer l = mMap.setBaseMap(tileSource);
        mMap.setTheme(VtmThemes.DEFAULT);

        TileSource ts = OSciMap4TileSource.builder()
                .httpFactory(new OkHttpEngine.OkHttpFactory())
                .url("http://opensciencemap.org/tiles/s3db")
                .zoomMin(8)
                .zoomMax(17)
                .build();

        S3DBTileLayer tl = new S3DBTileLayer(mMap, ts);
        mMap.layers().add(tl);
        mMap.layers().add(new LabelLayer(mMap, l));

        mMap.setMapPosition(53.08, 8.82, 1 << 17);

    }

    public static void main(String[] args) {
        init();
        run(new OSciMapS3DBTest());
    }
}