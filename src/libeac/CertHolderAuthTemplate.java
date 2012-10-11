/*
 * CCertHolderAuthTemplate.java
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
import common.crypto.IDERApplicationSpecific;
import common.crypto.IDERObjectIdentifier;
import common.crypto.IDERSequence;
import common.exceptions.ParsingException;
import libeac.definitions.PACETypes;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 23.12.11
 * Time: 14:48
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class represents the Certificate Holder Authorization Template (CHAT) which specifies which data a service
 * can request from the eID card
 */
public class CertHolderAuthTemplate {
    private IDERObjectIdentifier oid_;
    private byte[] authData_;

    /**
     * Creation method
     * @param chat Certificate Holder Authorization Template
     * @return New instance if successful
     * @throws ParsingException
     */
    public static CertHolderAuthTemplate fromBytes(byte[] chat) throws ParsingException {
        if(chat[0] != 0x7F || chat[1] != 0x4C ){
            throw new ParsingException("no valid CHAT data");
        }
        IDERSequence seq;
        try {
            IDERApplicationSpecific appSpec = CFactoryHelper.getCryptoProvider().createDERApplicationSpecific(chat);
            seq = appSpec.getDERSequence();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ParsingException("no valid CHAT data");
        }
        if(seq.size()!=2){
            throw new ParsingException("no valid CHAT data");
        }
        return new CertHolderAuthTemplate(seq.getDERObjectIdentifierAt(0),seq.getDERApplicationSpecificAt(1).getContents());
    }

    /**
     * Constructor
     * @param oid Identifier
     * @param authData Authorization data
     */
    public CertHolderAuthTemplate(IDERObjectIdentifier oid, byte[] authData){
        oid_ = oid;
        authData_ = authData;
    }

    /**
     * Constructor
     * @param eRole Role of the terminal
     */
    public CertHolderAuthTemplate(PACETypes.ERoleType eRole){
        String szOid;
        switch (eRole) {
            case AT:
                szOid = "0.4.0.127.0.7.3.1.2.2";
                break;
            default:
                szOid = "0.4.0.127.0.7.3.1.2.2";
        }
        oid_ =  CFactoryHelper.getCryptoProvider().createDERObjectIdentifier(szOid);
    }

    /**
     * Set the authorization data
     * @param byaAuth Authorization data
     */
    public void setAuthorization(byte[] byaAuth){
        authData_ = byaAuth;
    }

    /**
     * Encode the authorization data to a format the card can interpret
     * @return Encoded CHAT
     */
    public byte[] getEncoded(){
        byte[] byaOid = oid_.getDEREncoded();

        byte[] byaRet;
        if(authData_ !=null)
            byaRet = new byte[3+byaOid.length+2+ authData_.length];
        else
            byaRet = new byte[3+byaOid.length];

        byaRet[0] = (byte) 0x7F;
        byaRet[1] = (byte) 0x4C;
        byaRet[2] = (byte) (byaOid.length+ authData_.length+2);
        System.arraycopy(byaOid,0,byaRet,3,byaOid.length);
        if(authData_ !=null){
            int nOffset = 3 + byaOid.length;
            byaRet[nOffset] = (byte) 0x53;
            byaRet[nOffset+1] = (byte) authData_.length;
            System.arraycopy(authData_,0,byaRet,nOffset+2, authData_.length);
        }

        return byaRet;
    }
}
