/*
 * DIDAuthenticateResponseMessageBuilder.java
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

package eid.network.messages;

import common.exceptions.BuildException;
import common.exceptions.TranscodingException;
import eid.network.data.AuthenticationProtocolDataType;
import eid.network.data.DIDAuthenticateResponseType;
import eid.network.messages.transcoding.AuthProtocolDataEncoder;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 20.07.12
 * Time: 18:46
 */

/**
 * Message builder for the DIDAuthenticate response messages.
 * @param <T> Content data type
 */
public class DIDAuthenticateResponseMessageBuilder<T extends AuthenticationProtocolDataType>
        extends ProtocolResponseMessageBuilder<DIDAuthenticateResponseType<T>> {

    private AuthProtocolDataEncoder<T> encoder_;

    /**
     * Constructor
     * @param encoder Encoding unit for the content data
     */
    public DIDAuthenticateResponseMessageBuilder(AuthProtocolDataEncoder<T> encoder){
        super("DIDAuthenticateResponse", ProtocolResponseBodyHelper.ISO_NS, ProtocolResponseBodyHelper.ISO_NS_DEC);
        encoder_ = encoder;
    }

    /**
     * Specific implementation of the content insertion
     * @param payload Content data
     * @param content Body element
     * @throws BuildException
     * @throws SOAPException
     */
    @Override
    protected void buildBodyContent(DIDAuthenticateResponseType<T> payload, SOAPElement content)
            throws BuildException, SOAPException {
        try {
            encoder_.encodeAndAdd(payload.getAuthProtocolData(), content);
        } catch (TranscodingException e) {
            throw new BuildException("could not set body content: "+e.getMessage());
        }
    }
}
