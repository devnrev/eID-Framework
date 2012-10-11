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

import javax.xml.soap.*;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 05.07.12
 * Time: 10:29
 */

/**
 * This abstract class represents a generic SOAP message builder which initializes a normal message
 * @param <T> Data type of the content data
 * @param <P> Data type of the product
 */
public abstract class GenericSoapMessageBuilder <T,P extends SoapMessage>
        implements IGenericSoapMessageBuilder<T,P> {

    protected P product_;
    protected SOAPMessage message_;
    protected SOAPEnvelope envelope_;
    protected SOAPHeader header_;
    protected SOAPBody body_;

    /**
     * Pure virtual method which should return a new skeleton object og the product
     * @return Product object
     * @throws BuildException
     */
    protected abstract P createNewProduct() throws BuildException;


    /**
     * Generalized product initialization
     * @throws BuildException
     */
    protected void initializeProductInternal() throws BuildException {
        product_ = createNewProduct();
        try {
            MessageFactory mf = MessageFactory.newInstance();
            message_ = mf.createMessage();
            envelope_ = message_.getSOAPPart().getEnvelope();
            header_ = envelope_.getHeader();
            body_ = envelope_.getBody();
        } catch (SOAPException e) {
            throw new BuildException("could not construct message object");
        }
    }

    /**
     * Setup the product
     * @throws BuildException
     */
    @Override
    public void initializeProduct() throws BuildException {
        initializeProductInternal();
    }

    /**
     * Build the envelope
     * @return
     * @throws BuildException
     */
    @Override
    public SOAPElement buildEnvelope() throws BuildException {
        return envelope_;
    }

    /**
     * Build the header
     * @return
     * @throws BuildException
     */
    @Override
    public SOAPElement buildHeader() throws BuildException {
        return header_;
    }

    /**
     * Get the product
     * @return Product
     * @throws BuildException
     */
    @Override
    public P getProduct() throws BuildException {
        product_.setMessage(message_);
        return product_;
    }
}
