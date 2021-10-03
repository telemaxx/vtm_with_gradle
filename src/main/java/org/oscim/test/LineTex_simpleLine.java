/*
 * Copyright 2014 Hannes Janetzek
 * Copyright 2016-2020 devemux86
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.oscim.backend.CanvasAdapter;
import org.oscim.backend.canvas.Bitmap;
import org.oscim.backend.canvas.Paint;
import org.oscim.core.GeoPoint;
import org.oscim.core.MapPosition;
import org.oscim.event.Event;
import org.oscim.gdx.GdxMapApp;
import org.oscim.layers.tile.bitmap.BitmapTileLayer;
import org.oscim.layers.vector.PathLayer;
import org.oscim.layers.vector.geometries.Style;
import org.oscim.map.Map;
import org.oscim.renderer.bucket.TextureItem;
import org.oscim.tiling.source.OkHttpEngine;
import org.oscim.tiling.source.UrlTileSource;
import org.oscim.tiling.source.bitmap.DefaultSources;

public class LineTex_simpleLine extends GdxMapApp {

	private static final boolean ANIMATION = false;
	// protected int _fgColor = 0xFFFF0000; // 100 percent red. AARRGGBB
	// protected int _bgColor = 0xFFFF00FF; // 100 precent Green. AARRGGBB
	protected int _symbolSizeInt = 10;
	private Bitmap _bitmapArrow;
	protected Paint _fillPainter = CanvasAdapter.newPaint();
	protected Paint _linePainter = CanvasAdapter.newPaint();

	private List<PathLayer> mPathLayers = new ArrayList<>();
	private TextureItem tex;

	public static void main(String[] args) {
		GdxMapApp.init();
		GdxMapApp.run(new LineTex_simpleLine());
	}

	@Override
	public void createLayers() {
		UrlTileSource tileSource = DefaultSources.OPENSTREETMAP.httpFactory(new OkHttpEngine.OkHttpFactory()).build();
		tileSource.setHttpRequestHeaders(Collections.singletonMap("User-Agent", "vtm-playground"));
		mMap.layers().add(new BitmapTileLayer(mMap, tileSource));

		mMap.setMapPosition(0, 0, 1 << 2);

//		try {
//			tex = new TextureItem(CanvasAdapter.getBitmapAsset("", "patterns/pike.png"));
//			// tex.mipmap = true;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		createLayers(1, true);

		if (ANIMATION) {
			mMap.events.bind(new Map.UpdateListener() {
				@Override
				public void onMapEvent(Event e, MapPosition mapPosition) {
					// if (e == Map.UPDATE_EVENT) {
					long t = System.currentTimeMillis();
					float pos = t % 20000 / 10000f - 1f;
					createLayers(pos, false);
					mMap.updateMap(true);
					// }
				}
			});
		}
	}

	void createLayers(float pos, boolean init) {

		int i = 0;
		double lat = -90;

		List<GeoPoint> pts = new ArrayList<>();
		pts.add(new GeoPoint(+00.0, -90.0));
		pts.add(new GeoPoint(+45.0, -45.0));
		pts.add(new GeoPoint(-45.0, +45.0));
		pts.add(new GeoPoint(+00.0, +90.0));

		PathLayer pathLayer;
		if (init) {
			int bitmapArrowSize = 10;
			float bitmapArrowSizeF = bitmapArrowSize - 1;
			final float half = bitmapArrowSize / 2;
			int fg = 0xFFFF0000;
			int bg = 0xFF00FF00;
			// _fillPainter.setStyle(Paint.Style.FILL);
			_linePainter.setStyle(Paint.Style.STROKE);
			_linePainter.setStrokeWidth(2);
			System.out.println("size: " + bitmapArrowSizeF);
			System.out.println("size / 2: " + bitmapArrowSizeF / 2);
			_linePainter.setColor(fg);
			_bitmapArrow = CanvasAdapter.newBitmap(bitmapArrowSize, bitmapArrowSize, 0);
			final org.oscim.backend.canvas.Canvas arrowCanvas = CanvasAdapter.newCanvas();
			arrowCanvas.setBitmap(_bitmapArrow);
			arrowCanvas.drawLine(bitmapArrowSizeF, bitmapArrowSizeF / 2, 1f, bitmapArrowSizeF, _linePainter);
			// arrowCanvas.drawLine(1f, bitmapArrowSizeF, 1f, 1f, _linePainter);
			arrowCanvas.drawLine(1f, 1f, bitmapArrowSizeF, bitmapArrowSizeF / 2, _linePainter);
			arrowCanvas.drawLine(bitmapArrowSizeF, bitmapArrowSizeF / 2, 1, bitmapArrowSizeF / 2, _linePainter);

			tex = new TextureItem(_bitmapArrow);
			// tex = null;

			// int c = Color.fade(Color.rainbow((float) (lat + 90) / 180), 0.5f);
			// AARRGGBB


			Style style = Style.builder().stipple(bitmapArrowSize).stippleWidth(20f)
					.strokeWidth(bitmapArrowSize)
					.strokeColor(fg)
					.stippleColor(fg)
					.fixed(true)
					.texture(tex)
					// .randomOffset(true)
					.cap(Paint.Cap.ROUND)
					.build();

			pathLayer = new PathLayer(mMap, style);
			mMap.layers().add(pathLayer);
			mPathLayers.add(pathLayer);
		} else {
			pathLayer = mPathLayers.get(i++);
		}

		pathLayer.setPoints(pts);
	}

}

