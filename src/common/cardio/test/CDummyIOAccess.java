/*
 * CDummyIOAccess.java
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

package common.cardio.test;

import common.cardio.CAPDUCommand;
import common.cardio.CAPDUResponse;
import common.cardio.ControlCommand;
import common.cardio.ICardAccess;
import common.exceptions.CommunicationException;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 08.01.12
 * Time: 11:30
 * To change this template use File | Settings | File Templates.
 */

/**
 * Simple dummy class of a card accessor for testing purpose
 */
public class CDummyIOAccess implements ICardAccess {

    @Override
    public boolean connect() {
        return false;  //TODO
    }

    @Override
    public void disconnect() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CAPDUResponse sendAPDUCommand(CAPDUCommand cmd) {
        return new CAPDUResponse(cmd.getBytes(),0x00,00);
    }

    @Override
    public byte[] sendRawApduCommand(byte[] apduData) throws CommunicationException {
        return new byte[0];  //TODO
    }

    @Override
    public byte[] sendControlCommand(ControlCommand controlCmd) throws CommunicationException {
        return new byte[0];  //TODO
    }

    @Override
    public boolean isConnected() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
