/*
 * EAC1InputTypeParser.java
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
import eid.network.data.EAC1InputType;
import org.w3c.dom.Element;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 21.07.12
 * Time: 23:12
 */

/**
 * This class can extract EAC1InputType data from a XML resource. Used in DIDAuthenticate step 1
 */
public class EAC1InputTypeParser extends AuthProtocolDataParser<EAC1InputType> {

    /**
     * Constructor
     */
    public EAC1InputTypeParser() {
        super("EAC1InputType");
    }

    /**
     * Parse the XML resource
     * @param accessor XML accessor
     * @return EAC1InputType object
     * @throws ParsingException
     */
    @Override
    public EAC1InputType parse(XmlAccessor accessor) throws ParsingException {
        NodeNameFilter nodeNameFilter = new NodeNameFilter("Certificate",
                NodeNameFilter.MatchingRule.EXACT_IGNORE_NAMESPACE);
        List<Element> certificates = accessor.getAllNodesAtCursorMatching(nodeNameFilter);
        List<byte[]> certificateList = new ArrayList<byte[]>();
        for(Element cert : certificates){
            certificateList.add(DatatypeConverter.parseHexBinary(cert.getTextContent()));
        }
        nodeNameFilter.setMatchingName("CertificateDescription");
        List<Element> certificateDescs = accessor.getAllNodesAtCursorMatching(nodeNameFilter);
        List<byte[]> certificateDescList = new ArrayList<byte[]>();
        for(Element certDesc : certificateDescs){
            certificateDescList.add(DatatypeConverter.parseHexBinary(certDesc.getTextContent()));
        }

        nodeNameFilter.setMatchingName("RequiredCHAT");
        Element requiredChatElem,optionalChatElem,authenticatedAuxDataElem;
        try {
            requiredChatElem = accessor.findElementAtCursorMatching(nodeNameFilter);
            nodeNameFilter.setMatchingName("OptionalCHAT");
            optionalChatElem  = accessor.findElementAtCursorMatching(nodeNameFilter);
            nodeNameFilter.setMatchingName("AuthenticatedAuxiliaryData");
            authenticatedAuxDataElem  = accessor.findElementAtCursorMatching(nodeNameFilter);
        } catch (ElementNotFoundException e) {
            e.printStackTrace();
            throw new ParsingException("could not find RequiredCHAT entry in DIDAuthenticate request");
        }
        return new EAC1InputType(certificateList,
                certificateDescList,
                DatatypeConverter.parseHexBinary(requiredChatElem.getTextContent()),
                DatatypeConverter.parseHexBinary(optionalChatElem.getTextContent()),
                DatatypeConverter.parseHexBinary(authenticatedAuxDataElem.getTextContent()));
    }

}
