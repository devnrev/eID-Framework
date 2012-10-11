/*
 * TransmitMessageParser.java
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
import eid.network.data.ConnectionHandleType;
import eid.network.data.InputApduInfoType;
import eid.network.data.SlotHandleType;
import eid.network.data.TransmitRequestType;
import org.w3c.dom.Element;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 26.07.12
 * Time: 11:37
 */

/**
 * This class is used to parse the Transmit message into its corresponding data object.
 */
public class TransmitMessageParser implements IMessageParser<TransmitRequestType> {

    /**
     * Parse the XML resource
     * @param accessor XML accessor
     * @return TransmitRequestType object
     * @throws ParsingException
     */
    @Override
    public TransmitRequestType parse(XmlAccessor accessor) throws ParsingException {
        NodeNameFilter nodeNameFilter = new NodeNameFilter("SlotHandle",
                NodeNameFilter.MatchingRule.EXACT_IGNORE_NAMESPACE);
        ConnectionHandleType connectionHandleData = null;
        try {
            Element slotHandleElem = accessor.requireElement(nodeNameFilter);
            SlotHandleType slotHandle = new SlotHandleType(slotHandleElem.getTextContent());
            connectionHandleData = new ConnectionHandleType(null, slotHandle);
        } catch (ElementNotFoundException e) {
            throw new ParsingException("Transmit message not in right format: " + e.getMessage());
        }
        nodeNameFilter = new NodeNameFilter("InputAPDUInfo",
                NodeNameFilter.MatchingRule.EXACT_IGNORE_NAMESPACE);
        accessor.createSeek(nodeNameFilter);
        List<InputApduInfoType> inputApduInfos = new ArrayList<InputApduInfoType>();
        Element inputApduInfo;
        while((inputApduInfo = accessor.seekNext()) != null){
            NodeNameFilter contenFilter = new NodeNameFilter("InputAPDU",
                    NodeNameFilter.MatchingRule.EXACT_IGNORE_NAMESPACE);
            Element inputApduElem = null;
            try {
                inputApduElem = accessor.findElementAtCursorMatching(contenFilter);
            } catch (ElementNotFoundException e) {
                throw new ParsingException("Transmit message not in right format: " + e.getMessage());
            }
            byte[] apduData = DatatypeConverter.parseHexBinary(inputApduElem.getTextContent());
            contenFilter.setMatchingName("AcceptableStatusCode");
            List<Element> acceptStatusCodeElements = accessor.getAllNodesAtCursorMatching(contenFilter);
            if (acceptStatusCodeElements.size() > 0) {
                List<byte[]> statusCodes = new ArrayList<byte[]>();
                for (Element statusCodeElem : acceptStatusCodeElements) {
                    statusCodes.add(DatatypeConverter.parseHexBinary(statusCodeElem.getTextContent()));
                }
                inputApduInfos.add(new InputApduInfoType(apduData, statusCodes));
            } else {
                inputApduInfos.add(new InputApduInfoType(apduData));
            }
        }
        if(inputApduInfos.size() == 0){
            throw new ParsingException("Transmit message does not contain any input apdus");
        }
        return new TransmitRequestType(connectionHandleData,inputApduInfos);

    }
}
