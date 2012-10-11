/*
 * EAC2OutputDataEncoder.java
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
import eid.network.data.EAC2OutputType;
import eid.network.messages.ProtocolResponseBodyHelper;

import javax.xml.bind.DatatypeConverter;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 23.07.12
 * Time: 11:53
 */

/**
 * Specific transcoding class for the EAC2OutputData used in DIDAuthenticate step 2 and 3
 */
public class EAC2OutputDataEncoder extends AuthProtocolDataEncoder<EAC2OutputType> {

    /**
     * Constructor
     */
    public EAC2OutputDataEncoder() {
        super("iso:EAC2OutputType");
    }

    /**
     * Encode the actual authentication protocol data and add it to the container
     * @param authData Data
     * @param authProtocolDataElem Container element
     * @throws TranscodingException
     * @throws SOAPException
     */
    @Override
    protected void addContent(EAC2OutputType authData, SOAPElement authProtocolDataElem)
            throws TranscodingException, SOAPException {
        if (authData.isSignatureNeeded()) {
            SOAPElement challengeElem = authProtocolDataElem.addChildElement("Challenge",
                    ProtocolResponseBodyHelper.ISO_NS);
            challengeElem.setValue(DatatypeConverter.printHexBinary(authData.getChallenge()));
        } else {
            SOAPElement efCardSecurityElem = authProtocolDataElem.addChildElement("EFCardSecurity",
                    ProtocolResponseBodyHelper.ISO_NS);
            efCardSecurityElem.setValue(DatatypeConverter.printHexBinary(authData.getEfCardSecurity()));
            SOAPElement authTokenElem = authProtocolDataElem.addChildElement("AuthenticationToken",
                    ProtocolResponseBodyHelper.ISO_NS);
            authTokenElem.setValue(DatatypeConverter.printHexBinary(authData.getAuthenticationToken()));
            SOAPElement nonceElem = authProtocolDataElem.addChildElement("Nonce",
                    ProtocolResponseBodyHelper.ISO_NS);
            nonceElem.setValue(DatatypeConverter.printHexBinary(authData.getNonce()));
        }
    }
}
