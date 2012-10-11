/*
 * eIDCommunication.java
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

package eid.core;

import common.cardio.ICardAccess;
import common.exceptions.CommunicationException;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 26.07.12
 * Time: 14:11
 */

/**
 * Helper class which sends raw APDU commands to a smartcard
 */
public class eIDCommunication {
    private ICardAccess cardAccess_;

    /**
     * Constructor
     * @param cardAccess CardAccess object
     */
    public eIDCommunication(ICardAccess cardAccess){
        cardAccess_ = cardAccess;
    }

    /**
     * Send a plain apdu command to a smartcard and return the response
     * @param apduData Byte array of the command data
     * @return Byte array of the response data
     * @throws CommunicationException
     */
    public byte[] sendApduCommand(byte[] apduData) throws CommunicationException {
        return cardAccess_.sendRawApduCommand(apduData);
    }

}
