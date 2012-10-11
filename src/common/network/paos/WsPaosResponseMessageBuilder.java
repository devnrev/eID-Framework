/*
 * WsPaosResponseMessageBuilder.java
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

package common.network.paos;

import common.exceptions.BuildException;
import common.network.soap.GenericWsSoapMessageBuilder;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 05.07.12
 * Time: 10:49
 */

/**
 * Specialization of the ws addressing message builder for the creation of PAOS responses
 * @param <T> Response content data type
 */
public abstract class WsPaosResponseMessageBuilder<T>
        extends GenericWsSoapMessageBuilder<T,PAOSRequest,PAOSResponse> {
    protected final String messageNamespace_ = "S";
    protected final String paosNamespace_ = "paosns";
    protected final String serviceTypeVal_ = "http://www.bsi.bund.de/ecard/api/1.0/PAOS/GetNextCommand";

    /**
     * Create a new PAOSResponse
     * @return PAOS response object
     * @throws BuildException
     */
    @Override
    protected PAOSResponse createNewProduct() throws BuildException {
        return new PAOSResponse();
    }

    /**
     * Build the envelope with PAOS definitions
     * @return Envelope element
     * @throws BuildException
     */
    @Override
    public SOAPElement buildEnvelope() throws BuildException {
        super.buildEnvelope();
        try {
            envelope_.addNamespaceDeclaration(messageNamespace_, "http://schemas.xmlsoap.org/soap/envelope/");
            envelope_.addNamespaceDeclaration(paosNamespace_, "urn:liberty:paos:2006-08");
            envelope_.setPrefix(messageNamespace_);
            envelope_.removeNamespaceDeclaration("SOAP-ENV");
            header_.setPrefix(messageNamespace_);
            body_.setPrefix(messageNamespace_);
            return envelope_;
        } catch (SOAPException e) {
            throw new BuildException("could not configure envelope");
        }
    }

    /**
     * Build the header with PAOS definitions
     * @return Header element
     * @throws BuildException
     */
    @Override
    public SOAPElement buildHeader() throws BuildException {
        assert (header_ != null);
        try {
            SOAPElement paosElem = header_.addChildElement("PAOS", paosNamespace_);
            setPaosRequirements(paosElem);
            SOAPElement versionTwo = paosElem.addChildElement("Version", paosNamespace_);
            versionTwo.setValue("urn:liberty:2006-08");
            SOAPElement versionOne = paosElem.addChildElement("Version", paosNamespace_);
            versionOne.setValue("urn:liberty:2003-08");
            SOAPElement enpointRef = paosElem.addChildElement("EndpointReference", paosNamespace_);
            SOAPElement address = enpointRef.addChildElement("Address",paosNamespace_);
            address.setValue("http://www.projectliberty.org/2006/02/role/paos");
            SOAPElement metaData = enpointRef.addChildElement("MetaData",paosNamespace_);
            SOAPElement serviceType = metaData.addChildElement("ServiceType",paosNamespace_);
            serviceType.setValue(serviceTypeVal_);
            metaData.addChildElement("Options", paosNamespace_);
            buildMessageId();
            buildReplyTo();
            buildRelatesTo();
            return header_;
        } catch (SOAPException e) {
            throw new BuildException("could not set PAOS version Information");
        }
    }

    /**
     * Internal method used to add attributes to an element required by the PAOS protocol
     * @param elem Element
     * @return Element
     */
    protected SOAPElement setPaosRequirements(SOAPElement elem){
        elem.setAttribute("mustUnderstand","1");
        elem.setAttribute("actor", "http://schemas.xmlsoap.org/soap/actor/next");
        return elem;
    }

    /**
     * Add PAOS definitions to the "messageID" field
     * @return Element
     * @throws BuildException
     */
    @Override
    protected SOAPElement buildMessageId() throws BuildException {
        return setPaosRequirements(super.buildMessageId());
    }

    /**
     * Add PAOS definitions to the "relatesTo" field
     * @return Element
     * @throws BuildException
     */
    @Override
    protected SOAPElement buildRelatesTo() throws BuildException {
        return setPaosRequirements(super.buildRelatesTo());
    }

    /**
     * Add PAOS definitions to the "replyTo" field
     * @return Element
     * @throws BuildException
     */
    @Override
    protected SOAPElement buildReplyTo() throws BuildException {
        return setPaosRequirements(super.buildReplyTo());
    }
}
