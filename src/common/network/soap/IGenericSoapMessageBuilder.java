/*
 * IGenericSoapMessageBuilder.java
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

import javax.xml.soap.SOAPElement;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 03.07.12
 * Time: 11:35
 */

/**
 * Generic Interface which defines the methods a soap message builder should implement, based
 * on the builder design pattern
 * @param <T> Data type of the content data
 * @param <P> Data type of the product
 */
public interface IGenericSoapMessageBuilder<T,P extends SoapMessage> {

    /**
     * Setup a new product which can be built by this class
     * @throws BuildException
     */
    void initializeProduct() throws BuildException;

    /**
     * Create the content of the SOAP envelope
     * @return Envelope element
     * @throws BuildException
     */
    SOAPElement buildEnvelope() throws BuildException;

    /**
     * This method defines the SOAP header content
     * @return Header element
     * @throws BuildException
     */
    SOAPElement buildHeader() throws BuildException;

    /**
     * The creation of the SOAP body content is done here
     * @param payload Body data
     * @return Body element
     * @throws BuildException
     */
    SOAPElement buildBody(T payload) throws BuildException;

    /**
     * Get the built product, a SOAP message in this case
     * @return Product
     * @throws BuildException
     */
    P getProduct() throws BuildException;
}
