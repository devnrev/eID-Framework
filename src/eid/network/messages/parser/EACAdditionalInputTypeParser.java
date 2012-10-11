/*
 * EACAdditionalTypeParser.java
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
import eid.network.data.EACAdditionalInputType;
import org.w3c.dom.Element;

import javax.xml.bind.DatatypeConverter;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 24.07.12
 * Time: 11:57
 */

/**
 * This class can extract EACAdditionalInputType data from a XML resource. Used in DIDAuthenticate step 3
 */
public class EACAdditionalInputTypeParser extends AuthProtocolDataParser<EACAdditionalInputType> {

    /**
     * Constructor
     */
    public EACAdditionalInputTypeParser() {
        super("EACAdditionalInputType");
    }

    /**
     * Parse the XML resource
     * @param accessor XML accessor
     * @return EACAdditionalInputType object
     * @throws ParsingException
     */
    @Override
    public EACAdditionalInputType parse(XmlAccessor accessor) throws ParsingException {
        NodeNameFilter nodeNameFilter = new NodeNameFilter("Signature",
                NodeNameFilter.MatchingRule.EXACT_IGNORE_NAMESPACE);
        Element signatureElem = null;
        try {
            signatureElem = accessor.findElementAtCursorMatching(nodeNameFilter);
        } catch (ElementNotFoundException e) {
            e.printStackTrace();
            throw new ParsingException("could not find Signature in "+ dataType_);
        }
        byte[] signature = DatatypeConverter.parseHexBinary(signatureElem.getTextContent());
        return new EACAdditionalInputType(signature);

    }
}
