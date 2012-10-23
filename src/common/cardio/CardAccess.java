/*
 * CardAccess.java
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
import common.util.Logger;

import javax.smartcardio.*;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 27.07.12
 * Time: 14:03
 */

/**
 * Card accessor which provides methods to send APDU and terminal commands
 */
public class CardAccess implements ICardAccess {
    private CardTerminal terminal_;
    private Card card_;
    private CardChannel cardChannel_;

    public CardAccess(CardTerminal terminal) {
        terminal_ = terminal;
    }

    @Override
    public boolean connect() {
        try {
            card_ = terminal_.connect("*");
        } catch (CardException e) {
            e.printStackTrace();
            return false;
        }
        cardChannel_ = card_.getBasicChannel();
        return true;
    }

    @Override
    public void disconnect() {
        try {
            card_.disconnect(true);
        } catch (CardException e) {
            e.printStackTrace();  //TODO
        }
    }

    @Override
    public CAPDUResponse sendAPDUCommand(CAPDUCommand cmd) throws CommunicationException {
        try {
            if (terminal_.isCardPresent()) {
                ResponseAPDU resp = cardChannel_.transmit(new CommandAPDU(cmd.getBytes()));
                return cmd.getMessageCoding().decode(resp.getBytes());
            }
        } catch (Exception e) {
            Logger.log("Error while transmitting data: " + e.getMessage());
        }
        throw new CommunicationException("card communication error");
    }

    @Override
    public byte[] sendRawApduCommand(byte[] apduData) throws CommunicationException {
        try {
            if (terminal_.isCardPresent()) {
                ResponseAPDU resp = cardChannel_.transmit(new CommandAPDU(apduData));
                return resp.getBytes();
            }
        } catch (CardException e) {
            Logger.log("Error while transmitting data: " + e.getMessage());


        }
        throw new CommunicationException("card communication error");
    }

    @Override
    public byte[] sendControlCommand(ControlCommand controlCmd) throws CommunicationException {
        try {
            return card_.transmitControlCommand(controlCmd.getControlCode(),controlCmd.getControl());
        } catch (CardException e) {
            Logger.log("Error while transmitting data: " + e.getMessage());
        }
        throw new CommunicationException("card communication error");
    }

    @Override
    public boolean isConnected() {
        try {
            return terminal_.isCardPresent();
        } catch (CardException e) {
            e.printStackTrace();
            return false;
        }
    }
}
