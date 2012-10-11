/*
 * DIDAuthenticateMessageParser.java
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
import eid.network.data.AuthenticationProtocolDataType;
import eid.network.data.ConnectionHandleType;
import eid.network.data.DIDAuthenticateType;
import eid.network.data.SlotHandleType;
import org.w3c.dom.Element;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 21.07.12
 * Time: 20:43
 */

/**
 * This class can parse DIDAuthenticate messages. The content data type is assigned through a generic parameter.
 * @param <T> Content data type
 */
public class DIDAuthenticateMessageParser<T extends AuthenticationProtocolDataType>
        implements IMessageParser<DIDAuthenticateType<T>> {

    protected AuthProtocolDataParser<T> authProtocolDataParser_;

    /**
     * Constructor
     * @param authProtocolDataParser Content data parser
     */
    public DIDAuthenticateMessageParser(AuthProtocolDataParser<T> authProtocolDataParser){
        authProtocolDataParser_ = authProtocolDataParser;
    }

    /**
     * Parse the XML resource
     * @param accessor XML accessor
     * @return DIDAuthenticateType object
     * @throws ParsingException
     */
    @Override
    public DIDAuthenticateType<T> parse(XmlAccessor accessor) throws ParsingException {
        NodeNameFilter nodeNameFilter = new NodeNameFilter("SlotHandle",
                NodeNameFilter.MatchingRule.EXACT_IGNORE_NAMESPACE);

        ConnectionHandleType connectionHandleData = null;
        try {
            Element slotHandleElem = accessor.requireElement(nodeNameFilter);
            SlotHandleType slotHandle = new SlotHandleType(slotHandleElem.getTextContent());
            connectionHandleData = new ConnectionHandleType(null, slotHandle);
        } catch (ElementNotFoundException ignored) {
        }

        nodeNameFilter.setMatchingName("DIDName");
        String didName;

        try {
            Element didNameElem = accessor.requireElement(nodeNameFilter);
            didName = didNameElem.getTextContent();

            nodeNameFilter.setMatchingName("AuthenticationProtocolData");
            accessor.requireElement(nodeNameFilter);
        } catch (ElementNotFoundException e) {
            e.printStackTrace();
            throw new ParsingException("DIDAuthenticate request is incomplete");
        }
        if(!authProtocolDataParser_.checkDataType(accessor)){
            throw new ParsingException("AuthenticationProtocolData type doesn't match desired type");
        }
        T authProtocolData = authProtocolDataParser_.parse(accessor);
        return new DIDAuthenticateType<T>(connectionHandleData,didName,authProtocolData);
    }
}
