/*
 * ICardAccess.java
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

package common.cardio;

import common.exceptions.CommunicationException;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 11.12.11
 * Time: 22:50
 * To change this template use File | Settings | File Templates.
 */

/**
 * Interface describing methods to interact with a smart card or terminal
 */
public interface ICardAccess {
    boolean connect();
    void disconnect();
    CAPDUResponse sendAPDUCommand(CAPDUCommand cmd) throws CommunicationException;
    byte[] sendRawApduCommand(byte[] apduData) throws CommunicationException;
    byte[] sendControlCommand(ControlCommand controlCmd) throws CommunicationException;
    boolean isConnected();
}
