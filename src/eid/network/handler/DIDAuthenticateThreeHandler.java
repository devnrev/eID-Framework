/*
 * DIDAuthenticateThreeHandler.java
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
import eid.network.data.DIDAuthenticateResponseType;
import eid.network.data.DIDAuthenticateType;
import eid.network.data.EAC2OutputType;
import eid.network.data.EACAdditionalInputType;
import eid.network.messages.DIDAuthenticateResponseMessageBuilder;
import eid.network.messages.parser.DIDAuthenticateMessageParser;
import eid.network.messages.parser.EACAdditionalInputTypeParser;
import eid.network.messages.transcoding.EAC2OutputDataEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 24.07.12
 * Time: 11:56
 */

/**
 * DIDAuthenticate step 3 handler which parses messages with content type EACAdditionalInputType and
 * builds an appropriate response
 */
public class DIDAuthenticateThreeHandler extends ProtocolRequestHandler<DIDAuthenticateType<EACAdditionalInputType>,
        DIDAuthenticateResponseType<EAC2OutputType>> {

    /**
     * Constructor
     * @param request PAOS request
     */
    public DIDAuthenticateThreeHandler(PAOSRequest request) {
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
    protected DIDAuthenticateType<EACAdditionalInputType> extractDataImpl(XmlAccessor accessor)
            throws ParsingException {
        DIDAuthenticateMessageParser<EACAdditionalInputType> parser =
                new DIDAuthenticateMessageParser<EACAdditionalInputType>(new EACAdditionalInputTypeParser());
        return parser.parse(accessor);
    }
}
