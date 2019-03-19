/*
 * Copyright 2017 Longri
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
package org.oscim.theme.comparator.location;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class LocationPane extends JPanel {

    public LocationPane() {

        this.setBorder(BorderFactory.createTitledBorder("Location"));

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Normal", new LocationNormal());
        tabs.addTab("NMEA Format", new LocationNMEA());
        tabs.addTab("GPS Format", new LocationGPS());
        tabs.addTab("Tile Format", new LocationTile());

        this.add(tabs);
    }
}
