/*
 * TcApiOpenMessageParser.java
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
import eid.network.data.PathSecurityData;
import eid.network.data.TcApiOpenData;
import org.w3c.dom.Element;

import javax.xml.bind.DatatypeConverter;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 11.10.12
 * Time: 22:17
 */
public class TcApiOpenMessageParser implements IMessageParser<TcApiOpenData> {

    /**
     * Parse the XML resource
     *
     * @param accessor XML accessor
     * @return TcApiOpenData object
     * @throws ParsingException
     */
    @Override
    public TcApiOpenData parse(XmlAccessor accessor) throws ParsingException {
        NodeNameFilter nodeNameFilter = new NodeNameFilter("ProtocolTerminationPoint",
                NodeNameFilter.MatchingRule.EXACT_IGNORE_NAMESPACE);
        try {
            String ptp = accessor.requireElement(nodeNameFilter).getTextContent();
            nodeNameFilter.setMatchingName("SessionIdentifier");
            String sessionId = accessor.requireElement(nodeNameFilter).getTextContent();
            nodeNameFilter.setMatchingName("Binding");
            String binding = accessor.requireElement(nodeNameFilter).getTextContent();
            nodeNameFilter.setMatchingName("Protocol");
            String protocol = accessor.requireElement(nodeNameFilter).getTextContent();
            nodeNameFilter.setMatchingName("PSK");
            byte[] psk = DatatypeConverter.parseHexBinary(accessor.requireElement(nodeNameFilter).getTextContent());
            nodeNameFilter.setMatchingName("TLSCertificate");
            List<Element> tlsCert = accessor.getAllNodesAtCursorMatching(nodeNameFilter);
            PathSecurityData pathSec;
            if(tlsCert.size()>0){
                byte[] tlsCertificate  = DatatypeConverter.parseHexBinary(tlsCert.get(0).getTextContent());
                pathSec = new PathSecurityData(protocol,psk,tlsCertificate);
            } else {
                pathSec = new PathSecurityData(protocol,psk);
            }
            return new TcApiOpenData(ptp,sessionId,binding,pathSec);
        } catch (ElementNotFoundException e) {
            throw new ParsingException("Transmit message not in right format: " + e.getMessage());
        }
    }
}
