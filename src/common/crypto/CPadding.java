/*
 * CPadding.java
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

package common.crypto;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 27.12.11
 * Time: 16:46
 * To change this template use File | Settings | File Templates.
 */
public class CPadding {

    private static final byte s_bySecMsgContentIdentifier = (byte)0x80;

    public static byte[] applySecMsgPadding(byte[] byaData,int nBlockSize){
        int nLength = byaData.length;
        int nNumOfPadBytes;
        nNumOfPadBytes= nBlockSize - (nLength % nBlockSize);
        byte[] byaRet = new byte[nLength+nNumOfPadBytes];

        //subtract 1 because of data identifier
        --nNumOfPadBytes;

        System.arraycopy(byaData,0,byaRet,0,nLength);
        byaRet[nLength] = s_bySecMsgContentIdentifier;
        /*
        // Not needed due to standard array initialization with 0
        for(int i=0;i<nNumOfPadBytes;++i){
            byaRet[i+nLength+1]=(byte) 0x00;
        }
        */
        return byaRet;
    }

    public static byte[] removeSecMsgPadding(byte[] byaData) {
        if(byaData == null )
            return null;
        int nLength = byaData.length;

        for(int i = nLength-1;i >= 0;--i){
            if(byaData[i] == s_bySecMsgContentIdentifier){
                if(i == 0)
                    return null;
                return Arrays.copyOf(byaData,i);
            }

        }
        return byaData;
    }
}
