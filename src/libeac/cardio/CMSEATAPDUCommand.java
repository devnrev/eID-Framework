/*
 * CMSEATAPDUCommand.java
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

package libeac.cardio;


import common.CArrayHelper;
import common.CConverter;
import common.CFactoryHelper;
import common.cardio.CAPDUCommand;
import common.crypto.IDERObjectIdentifier;
import common.crypto.IDERTaggedObject;
import common.crypto.IECPointFP;
import libeac.CertHolderAuthTemplate;
import libeac.definitions.PACETypes;

import java.io.IOException;
import java.util.Arrays;


/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 11.12.11
 * Time: 23:28
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class represents the MSE:Set AT APDU command
 */
public class CMSEATAPDUCommand extends CAPDUCommand {

    private byte[] m_byaProtocol;
    private byte[] m_byaPublicKeyRef;
    private byte[] m_byaPrivateKeyRef;
    private byte[] m_byaChat;
    private byte[] m_byaEphemeralPublicKey;
    private byte[] auxData_;
    private PACETypes.AuthenticationType m_eAuthType;

    /**
     * Constructor
     * @param eAuthType Authentication type
     */
    public CMSEATAPDUCommand(PACETypes.AuthenticationType eAuthType) {
        super((byte)0x00, (byte)0x22, (byte)0, (byte)0);
        m_eAuthType = eAuthType;
        switch (eAuthType){
            case PACE:
                m_byPOne = (byte)0xC1;
                m_byPTwo = (byte)0xA4;
                break;
            case CA:
                m_byPOne = (byte)0x41;
                m_byPTwo = (byte)0xA4;
                break;
            case TA:
                m_byPOne = (byte)0x81;
                m_byPTwo = (byte)0xA4;
                break;
        }
        case_ = 3;

    }

    /**
     * Set the protocol identifier
     * @param protocol Protocol identifier
     */
    public void setProtocol(IDERObjectIdentifier protocol){
        IDERTaggedObject derTO = CFactoryHelper.getCryptoProvider().createDERTaggetObject(false, 0x00, protocol);
        try {
            m_byaProtocol = derTO.getEncoded();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    /**
     * Set the password type
     * @param ePwd Password type
     */
    public void  setPassword(PACETypes.Password ePwd){
        byte nVal;
        switch (ePwd){
            case MRZ:
                nVal = 1;
                break;
            case CAN:
                nVal = 2;
                break;
            case PIN:
                nVal = 3;
                break;
            case PUK:
                nVal = 4;
                break;
            default:
                throw new NoSuchFieldError("Password type is not accepted");

        }
        IDERTaggedObject derTO = CFactoryHelper.getCryptoProvider().createDERTaggetObject(false, 0x3, CFactoryHelper.getCryptoProvider().createDERInteger(nVal));
        try {
           m_byaPublicKeyRef =derTO.getEncoded();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * Set the public key reference
     * @param byaData Public key reference
     */
    public void setPublicKeyReference(byte[] byaData){
        if(byaData == null)
            return;
        m_byaPublicKeyRef = CArrayHelper.concatArrays(new byte[]{(byte)0x83,(byte)byaData.length},byaData);
    }

    /**
     * Set the private key reference
     * @param nPrivateKeyRef  Private key reference
     */
    public void setPrivateKeyReference(int nPrivateKeyRef){
        IDERTaggedObject derTO = CFactoryHelper.getCryptoProvider().createDERTaggetObject(false, 0x4,
                CFactoryHelper.getCryptoProvider().createDERInteger(nPrivateKeyRef));
        try {
            m_byaPrivateKeyRef=derTO.getEncoded();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the ephemeral public key
     * @param point Ephemeral public key point data
     */
    public void setEphemeralPublicKey(IECPointFP point){
        byte[] byaCompPoint = CConverter.convertBigIntegerToByteArray(point.getX());
        m_byaEphemeralPublicKey = CArrayHelper.concatArrays(new byte[]{(byte)0x91,(byte)byaCompPoint.length},byaCompPoint);
    }

    /**
     * Set the ephemeral public key
     * @param point Ephemeral public key
     */
    public void setEphemeralPublicKey(byte[] point){
        byte[] byaCompPoint;
        if(point[0] == 0x4){
            byaCompPoint= Arrays.copyOfRange(point,1,33);
        }else{
            byaCompPoint= Arrays.copyOf(point,32);
        }
        m_byaEphemeralPublicKey = CArrayHelper.concatArrays(new byte[]{(byte)0x91,(byte)byaCompPoint.length},byaCompPoint);
    }

    /**
     * Set the Certificate Holder Authorization Template
     * @param chat Certificate Holder Authorization Template
     */
    public void setCHAT(CertHolderAuthTemplate chat){
       m_byaChat = chat.getEncoded();
    }

    /**
     * Get the raw data of the  APDU command
     * @return Raw data
     */
    @Override
    public byte[] getBytes(){
        m_byaData = null;
        m_nLc = 0;
        if(m_byaProtocol != null){
            this.appendData(m_byaProtocol);
        }
        if(m_byaPublicKeyRef!=null){
            this.appendData(m_byaPublicKeyRef);
        }
        if(m_byaChat != null){
            this.appendData(m_byaChat);
        }
        if(m_byaPrivateKeyRef!=null){
            this.appendData(m_byaPrivateKeyRef);
        }
        if(m_byaEphemeralPublicKey!=null){
            this.appendData(m_byaEphemeralPublicKey);
        }
        if(auxData_ != null){
            this.appendData(auxData_);
        }

        return super.getBytes();
    }

    /**
     * Set the auxiliary data
     * @param auxData Auxiliary data
     */
    public void setAuxData(byte[] auxData) {
        auxData_ = auxData;
    }
}
