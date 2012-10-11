/*
 * CSecurityInfos.java
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
import common.crypto.IDERObjectIdentifier;
import common.crypto.IDERSet;
import common.crypto.IDERSequence;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 13.12.11
 * Time: 14:00
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class wraps the used content of the EF.CardAccess file into an accessible object
 */
public class SecurityInfos {
    private IDERSequence paceInfo_;
    private IDERSequence paceDomainParameter_;
    private IDERSequence chipAuthenticationDomainParameter_;
    private IDERSequence chipAuthenticationInfo_;
    private IDERSequence terminalAuthenticationInfo_;
    private byte[] encodedData_;

    /**
     * Constructor
     */
    public SecurityInfos(){

    }

    /**
     * Initialize the object with the date received from the card
     * @param byaEncodedData Encoded data
     * @throws IOException
     */
    public void initialize(byte[] byaEncodedData) throws IOException  {
        encodedData_ = byaEncodedData;
        IDERSet derSet = CFactoryHelper.getCryptoProvider().createDERSet(byaEncodedData);
        for(IDERSequence derSeq : derSet.getDERSequences()){
            String oid = derSeq.getDERObjectIdentifierAt(0).toString();
            switch (oid.charAt(18) - '0') {
                case 2:
                    terminalAuthenticationInfo_ = derSeq;
                    break;
                case 3:
                    if(oid.length() == 23)
                        chipAuthenticationInfo_ = derSeq;
                    else
                        chipAuthenticationDomainParameter_ = derSeq;
                    break;
                case 4:
                    if(oid.length() == 23)
                        paceInfo_ = derSeq;
                    else
                        paceDomainParameter_ = derSeq;
                    break;
                case 8:

                    break;

                default:

                    break;

            }
        }

    }

    /**
     * Get the PACE Information
     * @return PACE Information
     */
    public IDERSequence getPaceInfo() {
        return paceInfo_;
    }

    /**
     * Get the domain parameters
     * @return Domain parameters
     */
    public IDERSequence getPaceDomainParamter() {
        return paceDomainParameter_;
    }

    /**
     * Get the chip authentication parameters
     * @return Chip authentication parameters
     */
    public IDERSequence getChipAuthenticationDomainParameter() {
        return chipAuthenticationDomainParameter_;
    }

    /**
     * Get the chip authentication information
     * @return Chip authentication information
     */
    public IDERSequence getChipAuthenticationInfo() {
        return chipAuthenticationInfo_;
    }

    /**
     * Get the terminal authentication information
     * @return Terminal authentication information
     */
    public IDERSequence getTerminalAuthenticationInfo() {
        return terminalAuthenticationInfo_;
    }

    /**
     * Get the encoded data which was used to initialize the object
     * @return Encoded data
     */
    public byte[] getEncodedData() {
        return encodedData_;
    }

    /**
     * Get the chip authentication object identifier
     * @return Chip authentication object identifier
     */
    public IDERObjectIdentifier getCaOid() {
        return chipAuthenticationInfo_.getDERObjectIdentifierAt(0);
    }

    /**
     * Get the private key reference of the chip authentication
     * @return Chip authentication private key reference
     */
    public byte getCaPrivateKeyRef() {
        return chipAuthenticationInfo_.getDERIntegerAt(2).getValue().byteValue();
    }
}
