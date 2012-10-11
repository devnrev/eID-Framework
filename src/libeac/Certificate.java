/*
 * CCertificate.java
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

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 05.01.12
 * Time: 10:04
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class represents a certificate and provides function to readout specific values
 */
public class Certificate {
    protected IDERSequence certificate_;
    private byte[] data_;
    protected IDERApplicationSpecific appSpec_;

    /**
     * Constructor
     * @param byaCertificate Raw certificate data
     * @throws IOException
     */
    public Certificate(byte[] byaCertificate) throws IOException {
        appSpec_ = CFactoryHelper.getCryptoProvider().createDERApplicationSpecific(byaCertificate);
        if(byaCertificate[0] == 0x7F && byaCertificate[1] != 0x4E){
            data_ = appSpec_.getContents();
            certificate_ =  CFactoryHelper.getCryptoProvider().createDERApplicationSpecific(data_).getDERSequence();
        }else{
            data_ = byaCertificate;
            certificate_ = appSpec_.getDERSequence();
        }
    }

    /**
     * Get the Certificate authority Reference
     * @return Certificate Authority Reference
     */
    public byte[] getCar(){
        return certificate_.getDERApplicationSpecificAt(1).getContents();
    }

    /**
     * Get the Certificate Holder Reference
     * @return Certificate Holder Reference
     * @throws IOException
     */
    public byte[] getChr() throws IOException {
        return certificate_.getDERApplicationSpecificAt(3).getContents();
    }

    /**
     * Get the protocol identifier
     * @return Protocol identifier
     * @throws IOException
     */
    public IDERObjectIdentifier getOid() throws IOException {
        return certificate_.getDERApplicationSpecificAt(2).getDERSequence().getDERObjectIdentifierAt(0);
    }

    /**
     * Get the raw data of the certificate
     * @return Raw data
     */
    public byte[] getBytes(){
       return data_;
    }



}
