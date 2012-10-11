/*
 * EAC1OutputDataEncoder.java
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

package eid.network.messages.transcoding;


import common.exceptions.TranscodingException;
import eid.network.data.EAC1OutputType;
import eid.network.messages.ProtocolResponseBodyHelper;

import javax.xml.bind.DatatypeConverter;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 21.07.12
 * Time: 14:31
 */

/**
 * Specific transcoding class for the EAC1OutputData used in DIDAuthenticate step 1
 */
public class EAC1OutputDataEncoder extends AuthProtocolDataEncoder<EAC1OutputType> {

    /**
     * Constructor
     */
    public EAC1OutputDataEncoder() {
        super("iso:EAC1OutputType");
    }

    /**
     * Encode the actual authentication protocol data and add it to the container
     * @param authData Data
     * @param authProtocolDataElem Container element
     * @throws TranscodingException
     * @throws SOAPException
     */
    @Override
    protected void addContent(EAC1OutputType authData, SOAPElement authProtocolDataElem)
            throws TranscodingException, SOAPException {

        SOAPElement retryCounterElem = authProtocolDataElem.addChildElement("RetryCounter",
                ProtocolResponseBodyHelper.ISO_NS);
        retryCounterElem.setValue(String.valueOf(authData.getRetryCounter()));

        byte[] chat = authData.getChat();
        if (chat != null) {
            SOAPElement chatElem = authProtocolDataElem.addChildElement("CertificateHolderAuthorizationTemplate",
                    ProtocolResponseBodyHelper.ISO_NS);
            chatElem.setValue(DatatypeConverter.printHexBinary(chat));
        }
        List<byte[]> carList = authData.getCarList();
        for (byte[] car : carList) {
            SOAPElement carElem = authProtocolDataElem.addChildElement("CertificationAuthorityReference",
                    ProtocolResponseBodyHelper.ISO_NS);
            carElem.setValue(DatatypeConverter.printHexBinary(car));
        }
        byte[] efCardAccess = authData.getEfCardAccess();
        if (efCardAccess != null) {
            SOAPElement efCardAccessElem = authProtocolDataElem.addChildElement("EFCardAccess",
                    ProtocolResponseBodyHelper.ISO_NS);
            efCardAccessElem.setValue(DatatypeConverter.printHexBinary(efCardAccess));
        }
        byte[] idPicc = authData.getIdPICC();
        if (idPicc != null) {
            SOAPElement idPiccElem = authProtocolDataElem.addChildElement("IDPICC",
                    ProtocolResponseBodyHelper.ISO_NS);
            idPiccElem.setValue(DatatypeConverter.printHexBinary(idPicc));
        }
        byte[] challenge = authData.getChallenge();
        if (challenge != null) {
            SOAPElement challengeElem = authProtocolDataElem.addChildElement("Challenge",
                    ProtocolResponseBodyHelper.ISO_NS);
            challengeElem.setValue(DatatypeConverter.printHexBinary(challenge));
        }


    }
}
