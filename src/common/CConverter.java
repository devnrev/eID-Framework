/*
 * CConverter.java
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

import common.CFactoryHelper;
import common.crypto.IECCurveFP;
import common.crypto.IECFieldElementFP;
import common.crypto.IECPointFP;
import common.crypto.bouncycastle.CECPointFPBC;
import common.exceptions.ConverterException;

import java.math.BigInteger;
import java.util.BitSet;


/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 19.12.11
 * Time: 15:52
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class provides several conversion methods
 */
public class CConverter {

    /* Pace ECPoint format taken from BSI worked example
     * Let L be the length of given bytes - 1
     * bytes[0]             == 0x04 --> uncompressed point
     * bytes[1... (L/2)]    == x coordinate of the ECPoint
     * bytes[(L/2)+1 ... L] == y coordinate of the ECPoint
     */

    /**
     * Extract EC Point from a byte array
     * @param byaValue Byte array
     * @param curve Elliptic curve
     * @return EC Point
     */
    public static IECPointFP getECPointFPFromByteArray(byte[] byaValue, IECCurveFP curve){
        if(byaValue[0] != 0x04){
            byaValue = CArrayHelper.concatArrays(new byte[]{0x04},byaValue);
        }

        return new CECPointFPBC(curve.decodePoint(byaValue));
        /*

        int nLen = byaValue.length-offset;
        byte[] byaX = new byte[nLen/2];
        byte[] byaY = new byte[nLen/2];
        System.arraycopy(byaValue,offset,byaX,0,nLen/2);
        System.arraycopy(byaValue,offset+(nLen/2),byaY,0,nLen/2);
        IECFieldElementFP fieldElemX = CFactoryHelper.getCryptoProvider().createECFieldElementFP(curve.getQ(),new BigInteger(1,byaX));
        IECFieldElementFP fieldElemY = CFactoryHelper.getCryptoProvider().createECFieldElementFP(curve.getQ(),new BigInteger(1,byaY));
        return CFactoryHelper.getCryptoProvider().createECPointFP(curve,fieldElemX,fieldElemY);
        */
    }


    /**
     * Convert BigInteger to byte array with no leading 0
     * @param nVal BigInteger
     * @return BigInteger as byte array
     */
    public static byte[] convertBigIntegerToByteArray(BigInteger nVal){
        byte[] byaVal = nVal.toByteArray();
        byte[] byaRet = byaVal;
        if(byaVal[0] == 0){
            byaRet = new byte[byaVal.length-1];
            System.arraycopy(byaVal,1,byaRet,0,byaRet.length);
        }
        return byaRet;
    }

    /**
     * Extract little endian integer from byte array
     * @param data Byte array
     * @return Little endian integer
     * @throws ConverterException
     */
    public static int convertByteArrayToIntLittleEndian(byte[] data) throws ConverterException {
        if(data.length < 5){
            int num=0;
            for(byte i = 0; i < data.length; ++i){
                num |= ((((int)data[i])&0xFF) << (data.length-1-i) * 0x8);
            }
            return num;
        }
        throw new ConverterException("could not convert byte array to int");
    }

    /**
     * Extrac big endian integer from byte array
     * @param data Byte array
     * @return Big endian integer
     * @throws ConverterException
     */
    public static int convertByteArrayToIntBigEndian(byte[] data) throws ConverterException {
        if(data.length < 5){
            int num=0;
            for(byte i = 0; i < data.length; ++i){
                num|=((((int)data[i])&0xFF) << i * 0x8);
            }
            return num;
        }
        throw new ConverterException("could not convert byte array to int");
    }

    /**
     * Convert a byte array to a Java BitSet
     * @param data Byte array
     * @return BitSet
     */
    public static BitSet convertByteArrayToBitSet(byte[] data){
        BitSet bits = new BitSet();
        for (int i = 0; i < data.length * 8; i++) {
            if ((data[data.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
                bits.set(i);
            }
        }
        return bits;
    }
}
