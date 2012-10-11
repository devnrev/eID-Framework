/*
 * HttpBuilder.java
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
import common.util.IDataConverter;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 21.06.12
 * Time: 10:27
 */

/**
 * Abstract class which implements a generic HTTP message builder
 * @param <T> Content data type
 * @param <P> Product data type
 */
public abstract class GenericHttpMessageBuilder<T,P extends HttpMessage>
        implements IGenericHttpMessageBuilder<T,P>{
    protected P product_;
    protected IDataConverter<T,byte[]> converter_;
    protected boolean useConverter_;

    /**
     * Constructor
     */
    protected GenericHttpMessageBuilder(){
        useConverter_ = false;
    }

    /**
     * Constructor for additional data transformation
     * @param converter Data converter
     */
    protected GenericHttpMessageBuilder(IDataConverter<T,byte[]> converter){
        converter_ = converter;
        useConverter_ = true;
    }

    /**
     * Pure virtual method which should setup a new product
     */
    public abstract void initializeProduct();

    /**
     * Skeleton implementation for building the HTTP header
     * @throws BuildException
     */
    public void buildHeader() throws BuildException{

    }

    /**
     * Build the body content. If initialized with a converter, convert data to the desired format
     * @param payload Content data
     * @throws BuildException
     */
    public void buildBody(T payload) throws BuildException {
        if(!useConverter_){
            product_.setBody((byte[])payload);
            return;
        }
        product_.setBody(converter_.convert(payload));
    }

    /**
     * Get the built product
     * @return Product
     */
    public P getProduct(){
        return product_;
    }

}
