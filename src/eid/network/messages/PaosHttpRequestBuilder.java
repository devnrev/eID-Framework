/*
 * PaosHttpRequestBuilder.java
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

import common.network.http.GenericHttpRequestMessageBuilder;
import common.network.http.HttpHeader;
import common.network.http.HttpMessage;
import common.network.http.HttpRequest;
import common.util.IDataConverter;
import common.util.IOutputStreamResult;
import common.util.OutputStreamToByteArrayConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 06.07.12
 * Time: 14:59
 */

/**
 * This class creates HTTP requests which fulfil the PAOS requirements
 */
public class PaosHttpRequestBuilder extends GenericHttpRequestMessageBuilder<IOutputStreamResult> {
    protected String host_;
    protected String sessionId_;
    protected String resourcePath_;

    /**
     * Constructor
     * @param sessionId Session ID
     * @param host Host address
     */
    public PaosHttpRequestBuilder(String sessionId,String host, String resourcePath){
        super(new OutputStreamToByteArrayConverter());
        sessionId_ = sessionId;
        host_ = host;
        resourcePath_ = resourcePath;
    }

    /**
     * Build the HTTP request header
     */
    @Override
    public void buildHeader() {
        product_.setResourcePath(resourcePath_+"?sessionid="+sessionId_);
        product_.setMethod(HttpRequest.HttpMethod.POST);
        product_.setVersion(HttpMessage.HttpVersion.VER_1_1);
        HttpHeader header = product_.getHeader();
        List<String> acceptAttributes = new ArrayList<String>();
        acceptAttributes.add("text/html");
        acceptAttributes.add( "application/vnd.paos+xml");
        header.addHeaderInfo("Accept",acceptAttributes);
        List<String> paosAttributes = new ArrayList<String>();
        paosAttributes.add("ver=\"urn:liberty:2003-08\",\"urn:liberty:2006-08\"");
        paosAttributes.add("http://www.bsi.bund.de/ecard/api/1.0/PAOS/GetNextCommand");
        header.addHeaderInfo("PAOS",paosAttributes);
        header.addHeaderInfo("Host",host_);
    }
}
