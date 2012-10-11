/*
 * StartPaosHandler.java
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

import common.exceptions.BuildException;
import common.network.paos.PAOSRequest;
import common.network.paos.PAOSResponse;
import eid.network.data.*;
import eid.network.messages.StartPaosMessageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 03.07.12
 * Time: 11:02
 */

/**
 * This class creates the StartPAOS PAOS response
 */
public class StartPaosHandler {
    private TcTokenData tcToken_;

    /**
     * Constructor
     * @param tokenData TcToken
     */
    public StartPaosHandler(TcTokenData tokenData) {
        tcToken_ = tokenData;
    }

    /**
     * Fill the StartPAOS data structure with the requeired values
     * @return StartPAOS data object
     */
    public StartPAOSData generateStartPaosToken(){

        ChannelHandleType channelHandle = new ChannelHandleType("channelhandle",
                                                    UUID.randomUUID().toString().replaceAll("-",""),
                                                    "reader",
                                                    0,
                                                    "application");
        SlotHandleType slotHandle = new SlotHandleType(UUID.randomUUID().toString().replaceAll("-",""),
                                              "recognition info");

        ConnectionHandleType connHandle= new ConnectionHandleType(channelHandle,
                                                                       slotHandle);

        StartPAOSData spd = new StartPAOSData(tcToken_.getSessionIdentifier(),connHandle);
        return spd;
    }

    /**
     * Build the PAOS response object
     * @param paosData StartPAOS data
     * @return Response object
     * @throws BuildException
     */
    public PAOSResponse generateStartPaosMessage(StartPAOSData paosData) throws BuildException {
        StartPaosMessageBuilder messgeBuilder = new StartPaosMessageBuilder();
        messgeBuilder.initializeProduct();
        messgeBuilder.buildEnvelope();
        messgeBuilder.buildHeader();
        messgeBuilder.buildBody(paosData);
        return messgeBuilder.getProduct();
    }
}
