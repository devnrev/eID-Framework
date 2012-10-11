/*
 * PaosRequestHandler.java
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

package eid.network.handler;

import common.data.NodeNameFilter;
import common.data.XmlAccessor;
import common.exceptions.BuildException;
import common.exceptions.ElementNotFoundException;
import common.exceptions.ParsingException;
import common.network.paos.PAOSRequest;
import common.network.paos.PAOSResponse;
import common.network.paos.WsPaosResponseMessageBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 20.07.12
 * Time: 18:38
 */

/**
 * This class is used as a generic handler for processing incoming PAOS requests and generating
 * appropriate PAOS responses
 * @param <R> Request data type
 * @param <T> Response data type
 */
public abstract class ProtocolRequestHandler<R,T> {
    protected PAOSRequest request_;
    protected NodeNameFilter nodeNameFilter_;
    protected WsPaosResponseMessageBuilder<T> messageBuilder_;


    /**
     * Constructor
     * @param request POAS request
     * @param identifierTag Tag which the handler accepts
     * @param messageBuilder Builder class for the response message
     */
    public ProtocolRequestHandler(PAOSRequest request,
                                  String identifierTag,
                                  WsPaosResponseMessageBuilder<T> messageBuilder){

        request_ = request;
        nodeNameFilter_ = new NodeNameFilter(identifierTag,
                NodeNameFilter.MatchingRule.EXACT_IGNORE_NAMESPACE);
        messageBuilder_ = messageBuilder;
    }

    /**
     * Check if the message can be processed by the handler
     * @return true if it can be
     */
    public boolean checkRequest(){
        XmlAccessor xmlAcc = new XmlAccessor(request_.getBodyDocument());
        try {
            xmlAcc.requireElement(nodeNameFilter_);
            return true;
        } catch (ElementNotFoundException e) {
            return false;
        }
    }

    /**
     * Parse the content of the message into the request data type
     * @return Request data object
     * @throws ParsingException
     */
    public R extractData() throws ParsingException{
        XmlAccessor accessor = new XmlAccessor(request_.getBodyDocument());
        return extractDataImpl(accessor);
    }

    /**
     * Pure virtual method which defines the core parsing method
     * @param accessor XML accessor class
     * @return Request data object
     * @throws ParsingException
     */
    protected abstract R extractDataImpl(XmlAccessor accessor) throws ParsingException;

    /**
     * Generate the PAOS response for the PAOS request
     * @param data response data
     * @return PAOS response object
     * @throws BuildException
     */
    public PAOSResponse generateResponse(T data) throws BuildException{
        messageBuilder_.initializeProduct(request_);
        messageBuilder_.buildEnvelope();
        messageBuilder_.buildHeader();
        messageBuilder_.buildBody(data);
        return messageBuilder_.getProduct();
    }
}
