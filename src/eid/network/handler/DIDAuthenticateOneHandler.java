/*
 * DIDAuthenticateOneHandler.java
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
import eid.network.messages.transcoding.EAC1OutputDataEncoder;
import eid.network.messages.DIDAuthenticateResponseMessageBuilder;
import eid.network.messages.parser.DIDAuthenticateMessageParser;
import eid.network.messages.parser.EAC1InputTypeParser;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 20.07.12
 * Time: 18:35
 */

/**
 * DIDAuthenticate step 1 handler which parses messages with content type EAC1InputType and
 * builds an appropriate response
 */
public class DIDAuthenticateOneHandler extends ProtocolRequestHandler<DIDAuthenticateType<EAC1InputType>,
        DIDAuthenticateResponseType<EAC1OutputType>> {

    /**
     * Constructor
     * @param request PAOS request
     */
    public DIDAuthenticateOneHandler(PAOSRequest request) {
        super(request,"DIDAuthenticate",
                new DIDAuthenticateResponseMessageBuilder<EAC1OutputType>(new EAC1OutputDataEncoder()));
    }

    /**
     * Core parsing method. Initialize the specific parser and process the document
     * @param accessor XML accessor class
     * @return Response object
     * @throws ParsingException
     */
    @Override
    protected DIDAuthenticateType<EAC1InputType> extractDataImpl(XmlAccessor accessor) throws ParsingException {
        DIDAuthenticateMessageParser<EAC1InputType> parser =
                new DIDAuthenticateMessageParser<EAC1InputType>(new EAC1InputTypeParser());
        return parser.parse(accessor);
    }

}
