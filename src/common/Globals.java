/*
 * Globals.java
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

package common;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 31.07.12
 * Time: 12:51
 * To change this template use File | Settings | File Templates.
 */
public class Globals {
    public enum OS{
        WINDOWS,
        OSX,
        UNKNOWN
    }


    public static OS getOSType(){
        String osName = System.getProperty("os.name").toLowerCase();
        if(osName.contains("windows")){
            return OS.WINDOWS;
        }else if(osName.contains("os x")){
            return OS.OSX;
        }
        return OS.UNKNOWN;

    }
}
