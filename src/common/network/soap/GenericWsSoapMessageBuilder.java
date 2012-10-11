/*
 * GenericSoapMessageBuilder.java
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

package common.network.soap;

import common.exceptions.BuildException;
import org.w3c.dom.NodeList;

import javax.xml.soap.*;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 05.07.12
 * Time: 09:28
 */

/**
 * Generic message builder for SOAP messages with WS Binding
 * @param <T> Data type of the content data
 * @param <E> Data type of the previous message
 * @param <P> Data type of the product
 */
public abstract class GenericWsSoapMessageBuilder<T,E extends SoapMessage,P extends SoapMessage>
        extends GenericSoapMessageBuilder<T,P>
        implements IGenericWsSoapMessageBuilder<T,E,P> {


    protected String relatesToVal_ = "";
    protected String addressValue_ = "http://www.projectliberty.org/2006/02/role/paos";
    protected final String wsNamespace_ = "wsa";

    /**
     * Build the envelope with respective addressing
     * @return Envelope
     * @throws BuildException
     */
    @Override
    public SOAPElement buildEnvelope() throws BuildException {
        try {
            envelope_.addNamespaceDeclaration(wsNamespace_, "http://www.w3.org/2005/08/addressing");
            return envelope_;
        } catch (SOAPException e) {
            throw new BuildException("could not set ws binding namespace");
        }

    }

    /**
     * Setup a new product based on the previous SOAP message
     * @param previousMessage Previous message
     * @throws BuildException
     */
    @Override
    public void initializeProduct(E previousMessage) throws BuildException {
        initializeProductInternal();
        relatesToVal_ = previousMessage.getMessageId();
    }

    /**
     * Add a unique messageID to the product
     * @return MessageID element
     * @throws BuildException
     */
    protected SOAPElement buildMessageId() throws BuildException {
        assert (header_ != null);
        UUID uuid = UUID.randomUUID();
        try {
            SOAPElement elem = header_.addChildElement("MessageID", wsNamespace_);
            elem.setValue("urn:uuid:"+uuid.toString());
            return elem;
        } catch (SOAPException e) {
            throw new BuildException("could not construct messageID object");
        }
    }

    /**
     * Add a "relatesTo" field to the product
     * @return RelatesTo element
     * @throws BuildException
     */
    protected SOAPElement buildRelatesTo() throws BuildException {
        try {
            SOAPElement elem = header_.addChildElement("RelatesTo", wsNamespace_);
            elem.setValue(relatesToVal_);
            return elem;
        } catch (SOAPException e) {
            throw new BuildException("could not construct messageID object");
        }
    }

    /**
     * Add a "replyTo" field to the product
     * @return ReplyTo element
     * @throws BuildException
     */
    protected SOAPElement buildReplyTo() throws BuildException {
        assert (header_ != null);
        try {
            SOAPElement replyToElem = header_.addChildElement("ReplyTo", wsNamespace_);
            SOAPElement addressElem = replyToElem.addChildElement("Address", wsNamespace_);
            addressElem.setValue(addressValue_);
            return replyToElem;
        } catch (SOAPException e) {
            throw new BuildException("could not construct ReplyTo object");
        }
    }
}

