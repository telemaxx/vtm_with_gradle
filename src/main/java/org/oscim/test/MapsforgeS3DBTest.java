/*
 * Copyright 2018 devemux86
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

import java.io.File;

import org.oscim.gdx.GdxMapApp;

public class MapsforgeS3DBTest extends MapsforgeTest {
	//public static final String mapFile = "C:\\OfflineMaps\\mapfiles\\download.mapsforge.org\\switzerland_V5.map";
	//public static final String mapFile = "C:\\OfflineMaps\\mapfiles\\www.openandromaps.org\\Switzerland_ML.map";
	public static final String mapFile = "/home/top/oruxmaps/mapfiles/V5/niedersachsenV5.map";
   //public static final String mapFile = "D:\\OfflineMaps\\mapfiles\\mf\\france.map";


    private MapsforgeS3DBTest(final File mapFile) {
        super(mapFile, true);
    }

    public static void main(final String[] args) {
        GdxMapApp.init();
//        GdxMapApp.run(new MapsforgeS3DBTest(getMapFile(args)));
        GdxMapApp.run(new MapsforgeS3DBTest(getMapFile(mapFile)));
    }
}
