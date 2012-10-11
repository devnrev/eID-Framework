/*
 * IGenericWsSoapMessageBuilder.java
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
 * Date: 05.07.12
 * Time: 10:26
 */

/**
 * This interface defines the methods which are needed implement a soap message processor
 * that supports the WS Binding
 * @param <T> Data type of the content data
 * @param <E> Data type of the previous message
 * @param <P> Data type of the product
 */
public interface IGenericWsSoapMessageBuilder<T,E extends SoapMessage,P extends SoapMessage>
        extends IGenericSoapMessageBuilder<T,P>  {

    /**
     * This method should setup the product based on a previous SOAP message
     * @param previousMessage Previous message
     * @throws BuildException
     */
    void initializeProduct(E previousMessage) throws BuildException;
}
