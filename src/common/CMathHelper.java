/*
 * CMathHelper.java
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
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 31.12.11
 * Time: 18:40
 * To change this template use File | Settings | File Templates.
 */
public class CMathHelper {

    public static void increment(byte[] A) {
        incrementAtIndex(A,15);
    }
    
    private static void incrementAtIndex(byte[] array, int index) {
        if (array[index] == 0xFF) {
            array[index] = 0;
            if(index > 0)
                incrementAtIndex(array, index - 1);
        }
        else {
            array[index]++;
        }
    }
}