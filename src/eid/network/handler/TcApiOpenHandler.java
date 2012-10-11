/*
 * TcApiOpenHandler.java
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
import eid.network.data.NoResponseType;
import eid.network.data.TcApiOpenData;
import eid.network.messages.parser.TcApiOpenMessageParser;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 11.10.12
 * Time: 22:19
 */
public class TcApiOpenHandler extends ProtocolRequestHandler<TcApiOpenData, NoResponseType> {

    /**
     * Constructor
     * @param request PAOS request
     */
    public TcApiOpenHandler(PAOSRequest request) {
        super(request, "TC_API_Open", null);
    }

    /**
     * Core parsing method. nothing to be done here since the message contains no content data
     * @param accessor XML accessor class
     * @return Response object -> no response therefor null
     */
    @Override
    protected TcApiOpenData extractDataImpl(XmlAccessor accessor) throws ParsingException {
        TcApiOpenMessageParser messageParser = new TcApiOpenMessageParser();
        return messageParser.parse(accessor);
    }
}
