/*
 * InitializeFrameworkHandler.java
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
import common.network.paos.PAOSRequest;
import eid.network.data.InitializeFrameworkResponseType;
import eid.network.messages.InitFrameworkResponseMessageBuilder;

import javax.lang.model.type.NullType;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 18.07.12
 * Time: 12:01
 */

/**
 * This class is used to process the InitializeFramework request
 */
public class InitializeFrameworkHandler extends ProtocolRequestHandler<NullType,InitializeFrameworkResponseType> {

    /**
     * Constructor
     * @param request PAOS request
     */
    public InitializeFrameworkHandler(PAOSRequest req){
        super(req,"InitializeFramework",new InitFrameworkResponseMessageBuilder());
    }

    /**
     * Core parsing method. nothing to be done here since the message contains no content data
     * @param accessor XML accessor class
     * @return Response object -> no response therefor null
     */
    @Override
    protected NullType extractDataImpl(XmlAccessor accessor) {
        return null;
    }

}
