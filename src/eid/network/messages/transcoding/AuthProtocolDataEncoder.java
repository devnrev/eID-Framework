/*
 * IAuthProtocolDataEncoder.java
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
import eid.network.data.AuthenticationProtocolDataType;
import eid.network.messages.ProtocolResponseBodyHelper;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;


/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 21.07.12
 * Time: 14:21
 */

/**
 * Abstract generic class which encodes authentication protocol data and adds it to a soap message
 * @param <T> Data type
 */
public abstract class AuthProtocolDataEncoder<T extends AuthenticationProtocolDataType> {
    protected String type_;

    /**
     * Constructor
     * @param type Data type as a string
     */
    protected AuthProtocolDataEncoder(String type){
        type_ = type;
    }

    /**
     * Encode the data and add it as a child to the given SOAP message
     * @param authData Data
     * @param parent Message
     * @throws TranscodingException
     */
    public  void encodeAndAdd(T authData, SOAPElement parent) throws TranscodingException{
        try {
            SOAPElement authProtocolDataElem = parent.addChildElement("AuthenticationProtocolData",
                    ProtocolResponseBodyHelper.ISO_NS);
            authProtocolDataElem.setAttribute("xsi:type", type_);
            addContent(authData,authProtocolDataElem);
        } catch (SOAPException e) {
            e.printStackTrace();
            throw new TranscodingException("could not encodeAndAdd AuthenticationProtocolData");
        }
    }

    /**
     * Pure virtual method which should implement the actual transcoding of the authentication protocol data
     * @param authData Data
     * @param authProtocolDataElem Container element
     * @throws TranscodingException
     * @throws SOAPException
     */
    protected abstract void addContent(T authData, SOAPElement authProtocolDataElem)
            throws TranscodingException,SOAPException;
}
