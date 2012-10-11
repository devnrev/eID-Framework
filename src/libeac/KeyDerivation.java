/*
 * CKeyDerivation.java
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

package libeac;

import common.CFactoryHelper;
import common.crypto.CryptoTypes;
import common.crypto.ICryptoSHA;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 16.12.11
 * Time: 10:08
 * To change this template use File | Settings | File Templates.
 */

/**
 * The class is used to derive the special keys from the specific input values
 */
public class KeyDerivation {
    private int keyLen_;
    private ICryptoSHA shaGen_;

    /**
     * Constructor
     * @param eKeyLen Used key length
     */
    public KeyDerivation(CryptoTypes.EKeyLength eKeyLen){
        shaGen_ = CFactoryHelper.getCryptoProvider().createSHAGenerator();
        keyLen_ = CryptoTypes.getKeyLen(eKeyLen);
        switch (keyLen_){
            case 128:
                shaGen_.initialize(CryptoTypes.ESHAMode.SHA1);
                break;
            case 256:
                shaGen_.initialize(CryptoTypes.ESHAMode.SHA256);
                break;
        }

    }

    /**
     * General function to generate the key
     * @param byaK Secret value K
     * @param byaR Nonce
     * @param byaC Counter
     * @return Derived key
     */
    private  byte[] calcKey(byte[] byaK,byte[] byaR,byte[] byaC){
        byte[] byaData = new byte[byaK.length+byaR.length+byaC.length];
        System.arraycopy(byaK,0,byaData,0,byaK.length);
        if(byaR.length>0){
            System.arraycopy(byaR,0,byaData,byaK.length,byaR.length);
        }
        System.arraycopy(byaC,0,byaData,byaK.length+byaR.length,byaC.length);


        byte[] byaDigest = shaGen_.digest(byaData);
        byte[] byaRet = new byte[keyLen_ /8];
        System.arraycopy(byaDigest,0,byaRet,0, keyLen_ /8);


        return byaRet;

    }

    /**
     * Derive encryption key
     * @param byaK Secret value
     * @param byaR Nonce
     * @return Encryption key
     */
    public  byte[] derivateForEnc(byte[] byaK,byte[] byaR){
        return calcKey(byaK,byaR,new byte[]{0x00,0x00,0x00,0x01});
    }

    /**
     * Derive encryption key
     * @param byaK Secret value
     * @return Encryption key
     */
    public  byte[] derivateForEnc(byte[] byaK){
        return derivateForEnc(byaK,new byte[]{});
    }

    /**
     * Derive authentication key
     * @param byaK Secret value
     * @param byaR Nonce
     * @return Authentication key
     */
    public  byte[] derivateForMAC(byte[] byaK,byte[] byaR){
        return calcKey(byaK,byaR,new byte[]{0x00,0x00,0x00,0x02});
    }

    /**
     * Derive authentication key
     * @param byaK Secret value
     * @return Authentication key
     */
    public  byte[] derivateForMAC(byte[] byaK){
        return derivateForMAC(byaK,new byte[]{});
    }

    /**
     * Derive key for password
     * @param byaK Secret value
     * @return Password key
     */
    public  byte[] derivateForPassword(byte[] byaK){
        return calcKey(byaK,new byte[]{},new byte[]{0x00,0x00,0x00,0x03});
    }
}
