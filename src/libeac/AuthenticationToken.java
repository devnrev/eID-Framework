/*
 * CAuthenticationToken.java
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
import common.crypto.*;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 20.12.11
 * Time: 09:59
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class encapsulates the authentication token used in the final PACE step
 */
public class AuthenticationToken {
    private ISymmetricMAC cmac_;
    private IDERObjectIdentifier protocol_;

    /**
     * Constructor
     * @param protocol PACE protocol identifier
     */
    public AuthenticationToken(IDERObjectIdentifier protocol){
        cmac_ = CFactoryHelper.getCryptoProvider().createCMacAuthenticator();
        cmac_.initialize(CryptoTypes.EKeyLength.LEN_128);
        protocol_ = protocol;
    }

    /**
     * Calculate the authentication token
     * @param K Secret key
     * @param x Ephemeral key
     * @return First 8 bytes of the authenticated data
     */
    public byte[] calcAuthToken(byte[] K, IECPoint x){
        byte[] byaAuthData = cmac_.calculate(K,formatData(x));
        byte[] byaRet = new byte[8];
        System.arraycopy(byaAuthData,0,byaRet,0,8);
        return byaRet;
    }

    /**
     * Encode the data to authenticate described in TR 03110
     * tag + length + oid + received ephemeral key
     * tag: 0x7F49
     * length: protocol identifier length + ephemeral key length
     * @param x Ephemeral key
     * @return Formatted data
     */
    private byte[] formatData(IECPoint x){

        byte[] byaPoint = x.getEncoded();
        /* byte[] protocolData = protocol_.getDEREncoded();
       return CArrayHelper.concatArrays(new byte[]{(byte)0x7F,(byte)0x49},
                                 new byte[]{(byte)(protocolData.length+byaPoint.length+2)},
                                 protocolData,
                                 byaPoint);

           */
       byte[] byaPointData = new byte[2+byaPoint.length];
       byaPointData[0] = (byte)0x86;
       byaPointData[1] = (byte)byaPoint.length;
       System.arraycopy(byaPoint,0,byaPointData,2,byaPoint.length);
       byte[] byaProtocol = protocol_.getDEREncoded();
       byte[] byaRet = new byte[3+byaProtocol.length+byaPointData.length];
       byaRet[0] = (byte)0x7F;
       byaRet[1] = (byte)0x49;
       byaRet[2] = (byte)(byaProtocol.length+byaPointData.length);
       System.arraycopy(byaProtocol,0,byaRet,3,byaProtocol.length);
       System.arraycopy(byaPointData,0,byaRet,3+byaProtocol.length,byaPointData.length);
       return byaRet;



    }
}
