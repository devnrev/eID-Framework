/*
 * StartPaosMessageBuilder.java
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
import common.network.paos.WsPaosResponseMessageBuilder;
import eid.network.data.ChannelHandleType;
import eid.network.data.ConnectionHandleType;
import eid.network.data.SlotHandleType;
import eid.network.data.StartPAOSData;
import org.w3c.dom.NodeList;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 05.07.12
 * Time: 10:46
 */

/**
 * Message builder for the StartPAOS message.
 */
public class StartPaosMessageBuilder extends WsPaosResponseMessageBuilder<StartPAOSData> {

    /**
     * Build method for the message header
     * @return Header element
     * @throws BuildException
     */
    @Override
    public SOAPElement buildHeader() throws BuildException {
        SOAPElement header = super.buildHeader();
        NodeList nl = header.getElementsByTagName(wsNamespace_+":RelatesTo");
        if(nl.getLength()>0){
            for(int i = 0; i < nl.getLength();++i){
                header.removeChild(nl.item(i));
            }
        }
        return header_;
    }

    /**
     * Build the body element
     * @param payload Body data
     * @return Body element
     * @throws BuildException
     */
    @Override
    public SOAPElement buildBody(StartPAOSData payload) throws BuildException {
        try {
            SOAPElement content = body_.addChildElement(ProtocolResponseBodyHelper.buildContentTag("StartPAOS"));
            SOAPElement sessionIdentifier = content.addChildElement("SessionIdentifier",
                                                                    ProtocolResponseBodyHelper.ISO_NS);
            sessionIdentifier.setValue(payload.getSessionIdentifier());
            SOAPElement connectionHandle = content.addChildElement("ConnectionHandle",
                                                                    ProtocolResponseBodyHelper.ISO_NS);
            connectionHandle.addNamespaceDeclaration("type",connectionHandle.getTagName());
            ConnectionHandleType conHandle =  payload.getConnectionHandle();
            if(conHandle != null){
                ChannelHandleType ch = conHandle.getChannelHandle();
                if(ch!=null){
                    SOAPElement contextHandle = connectionHandle.addChildElement("ContextHandle",
                            ProtocolResponseBodyHelper.ISO_NS);
                    contextHandle.setValue(ch.getContextHandle());
                }
                SlotHandleType sh = conHandle.getSlotHandle();
                if(sh != null){
                    SOAPElement slotHandle = connectionHandle.addChildElement("SlotHandle",
                            ProtocolResponseBodyHelper.ISO_NS);
                    slotHandle.setValue(sh.getSlotHandle());
                }
            }
            return body_;
        } catch (SOAPException e) {
            throw new BuildException("could not set body content");
        }
    }
}
