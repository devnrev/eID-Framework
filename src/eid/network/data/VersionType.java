/*
 * VerisonType.java
 *
 * Copyright (C) 2012, Axel
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 * have received a copy of the GNU General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package eid.network.data;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 29.06.12
 * Time: 15:50
 */

/**
 * This class represents the VersionType specified in TR-03112-7
 */
public class VersionType {
    private int versionMajor_;
    private int versionMinor_;
    private int versionSubMinor_;

    public VersionType(int versionMajor, int versionMinor, int versionSubMinor) {
        this.versionMajor_ = versionMajor;
        this.versionMinor_ = versionMinor;
        this.versionSubMinor_ = versionSubMinor;
    }

    public int getVersionMajor() {
        return versionMajor_;
    }

    public int getVersionMinor() {
        return versionMinor_;
    }

    public int getVersionSubMinor() {
        return versionSubMinor_;
    }
}
