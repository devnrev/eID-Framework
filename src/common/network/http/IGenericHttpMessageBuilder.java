/*
 * IGenericHttpMesageBuilder.java
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

package common.network.http;

import common.exceptions.BuildException;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 06.07.12
 * Time: 14:46
 */

/**
 * This generic interface defines methods for http message builders
 * @param <T> Content data type
 * @param <P> Product data type
 */
public interface IGenericHttpMessageBuilder<T,P extends HttpMessage> {

    /**
     * Setup a new product, HTTP request or response in this case
     */
    void initializeProduct();

    /**
     * Build the HTTP header
     * @throws BuildException
     */
    void buildHeader() throws BuildException;

    /**
     * Build the body content of the HTTP message
     * @param payload Content data
     * @throws BuildException
     */
    void buildBody(T payload) throws BuildException;
}
