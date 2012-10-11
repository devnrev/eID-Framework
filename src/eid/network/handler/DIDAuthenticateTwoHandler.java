/*
 * DIDAuthenticateTwoHandler.java
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

import common.data.XmlAccessor;
import common.exceptions.ParsingException;
import common.network.paos.PAOSRequest;
import eid.network.data.*;
import eid.network.messages.DIDAuthenticateResponseMessageBuilder;
import eid.network.messages.parser.DIDAuthenticateMessageParser;
import eid.network.messages.parser.EAC1InputTypeParser;
import eid.network.messages.parser.EAC2InputTypeParser;
import eid.network.messages.transcoding.EAC2OutputDataEncoder;


/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 23.07.12
 * Time: 11:46
 */

/**
 * DIDAuthenticate step 2 handler which parses messages with content type EAC2InputType and
 * builds an appropriate response
 */
public class DIDAuthenticateTwoHandler
        extends ProtocolRequestHandler<DIDAuthenticateType<EAC2InputType>,
                                       DIDAuthenticateResponseType<EAC2OutputType>>{

    /**
     * Constructor
     * @param request PAOS request
     */
    public DIDAuthenticateTwoHandler(PAOSRequest request) {
        super(request,"DIDAuthenticate",
                new DIDAuthenticateResponseMessageBuilder<EAC2OutputType>(new EAC2OutputDataEncoder()));
    }

    /**
     * Core parsing method. Initialize the specific parser and process the document
     * @param accessor XML accessor class
     * @return Response object
     * @throws ParsingException
     */
    @Override
    protected DIDAuthenticateType<EAC2InputType> extractDataImpl(XmlAccessor accessor) throws ParsingException {
        DIDAuthenticateMessageParser<EAC2InputType> parser =
                new DIDAuthenticateMessageParser<EAC2InputType>(new EAC2InputTypeParser());
        return parser.parse(accessor);
    }
}
