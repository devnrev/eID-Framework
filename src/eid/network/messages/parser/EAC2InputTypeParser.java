/*
 * EAC2InputTypeParser.java
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

package eid.network.messages.parser;

import common.data.NodeNameFilter;
import common.data.XmlAccessor;
import common.exceptions.ElementNotFoundException;
import common.exceptions.ParsingException;
import eid.network.data.EAC2InputType;
import org.w3c.dom.Element;

import javax.xml.bind.DatatypeConverter;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 22.07.12
 * Time: 00:33
 */

/**
 * This class can extract EAC2InputType data from a XML resource. Used in DIDAuthenticate step 2
 */
public class EAC2InputTypeParser extends AuthProtocolDataParser<EAC2InputType> {

    /**
     * Constructor
     */
    public EAC2InputTypeParser() {
        super("EAC2InputType");
    }

    /**
     * Parse the XML resource
     * @param accessor XML accessor
     * @return EAC2InputType object
     * @throws ParsingException
     */
    @Override
    public EAC2InputType parse(XmlAccessor accessor) throws ParsingException {
        NodeNameFilter nodeNameFilter = new NodeNameFilter("Certificate",
                NodeNameFilter.MatchingRule.EXACT_IGNORE_NAMESPACE);
        List<Element> certificates = accessor.getAllNodesAtCursorMatching(nodeNameFilter);
        List<byte[]> certificateList = new ArrayList<byte[]>();
        for(Element cert : certificates){
            certificateList.add(DatatypeConverter.parseHexBinary(cert.getTextContent()));
        }
        nodeNameFilter.setMatchingName("EphemeralPublicKey");
        Element ephemeralPubKeyElem = null;
        try {
            ephemeralPubKeyElem = accessor.findElementAtCursorMatching(nodeNameFilter);
        } catch (ElementNotFoundException e) {
            e.printStackTrace();
            throw new ParsingException("could not find EphemeralPublicKey in "+ dataType_);
        }
        byte[] ephemeralPublicKey = DatatypeConverter.parseHexBinary(ephemeralPubKeyElem.getTextContent());
        return new EAC2InputType(certificateList,ephemeralPublicKey);
    }


}
