/*
 * HttpRequestBuilder.java
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

import common.util.IDataConverter;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 21.06.12
 * Time: 10:30
 */

/**
 * Generic implementation of a HTTP request builder
 * @param <T>  Content data type
 */
public class GenericHttpRequestMessageBuilder<T> extends GenericHttpMessageBuilder<T,HttpRequest> {

    /**
     * Constructor
     */
    public GenericHttpRequestMessageBuilder(){

    }

    /**
     * Constructor using data converter
     * @param converter Converter
     */
    public GenericHttpRequestMessageBuilder(IDataConverter<T,byte[]> converter) {
        super(converter);
    }

    /**
     * Instantiate a new HttpReauest object
     */
    @Override
    public void initializeProduct() {
        product_ = new HttpRequest();
    }

}
