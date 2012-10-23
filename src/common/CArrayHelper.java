/*
 * CArrayHelper.java
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

import java.lang.reflect.Array;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 28.12.11
 * Time: 13:54
 * To change this template use File | Settings | File Templates.
 */

/**
 * Helper class containing methods for arrays
 */
public class CArrayHelper {

    /**
     * Concatenate several byte arrays
     * @param byaArrays  Byte arrays
     * @return Concatenated byte array
     */
    public static byte[] concatArrays(byte[]... byaArrays) {
        int nLen = 0;
        for(byte[] byaArr : byaArrays){
            nLen += byaArr.length;
        }
        byte[] byaRet= new byte[nLen];
        int nPos = 0;
        for(byte[] byaArr : byaArrays){
            System.arraycopy(byaArr, 0, byaRet, nPos, byaArr.length);   
            nPos += byaArr.length;
        }
        return byaRet;
    }
}
