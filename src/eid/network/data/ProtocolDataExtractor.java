/*
 * SOAPMessageExtractor.java
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

package eid.network.data;

import org.w3c.dom.NodeList;

import javax.xml.bind.DatatypeConverter;
import javax.xml.soap.SOAPBody;


/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 21.06.12
 * Time: 16:11
 */

/**
 * This class is used to extract the XML values of the TcApiOpen command and to
 * create a corresponding TcApiOpenData Object
 */
public class ProtocolDataExtractor {

    /**
     * Search a tag inside a XML document and return its value
     * @param data SOAP Body
     * @param tag Tag name
     * @return Tag value
     */
    private static String getFirstValueOfTag(SOAPBody data, String tag){
        NodeList nl = data.getElementsByTagName(tag);
        if(nl.getLength() > 0){
            return nl.item(0).getTextContent();
        }
        return "";
    }

    /**
     * Static helper method which parses the XML message
     * @param body SOAP body
     * @return Result object
     */
    public static TcApiOpenData extractTcApiOpenData(SOAPBody body){
        String terminationendPoint = getFirstValueOfTag(body,"iso:ProtocolTerminationPoint");
        String sessionId = getFirstValueOfTag(body,"iso:SessionIdentifier");
        String binding = getFirstValueOfTag(body,"iso:Binding");
        String protocol = getFirstValueOfTag(body,"iso:Protocol");
        String psk = getFirstValueOfTag(body,"iso:PSK");
        String tlsCertificate = getFirstValueOfTag(body,"iso:TLSCertificate");
        PathSecurityData psd = new PathSecurityData(
                protocol,
                DatatypeConverter.parseHexBinary(psk),
                DatatypeConverter.parseHexBinary(tlsCertificate));
        TcApiOpenData data = new TcApiOpenData(
                terminationendPoint,
                sessionId,
                binding,
                psd);
        return data;

    }

}
