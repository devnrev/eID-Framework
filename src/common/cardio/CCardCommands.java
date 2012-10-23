/*
 * CCardCommands.java
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

import common.cardio.coding.CAPDUNormalMessaging;
import common.exceptions.CommunicationException;
import libeac.cardio.SelectFileAPDUCommand;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 12.12.11
 * Time: 23:36
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class provides methods to read files specified by an id from the card
 */
public class CCardCommands {
    private ICardAccess cardAccess_;
    private IAPDUMessageCoding messageCoding_;


    public CCardCommands(ICardAccess cardAccess) {
        cardAccess_ = cardAccess;
        messageCoding_ = new CAPDUNormalMessaging();
    }

    public CCardCommands(ICardAccess cardAccess, IAPDUMessageCoding messageCoding) {
        cardAccess_ = cardAccess;
        messageCoding_ = messageCoding;
    }

    public byte[] readCardFile(byte[] fid) throws CommunicationException {
        /*SelectMasterFileAPDUCommand selectMfCmd = new SelectMasterFileAPDUCommand();
        selectMfCmd.setMessageCoding(messageCoding_);
        CAPDUResponse resp = cardAccess_.sendAPDUCommand(selectMfCmd);
        if (resp.getSW() != 0x9000)
            throw new Exception("Could not select master file;  response: \n" + resp.toString());
        */
        SelectFileAPDUCommand selectFileCmd = new SelectFileAPDUCommand(fid);
        selectFileCmd.setMessageCoding(messageCoding_);
        CAPDUResponse resp = cardAccess_.sendAPDUCommand(selectFileCmd);
        if (resp.getSW() != 0x9000)
            throw new CommunicationException("Could not select file;  response: \n" + resp.toString());

        byte readLength = (byte) 0xFF;
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        short i = 0;
        try {
            do {

                CBinaryReadADPUCommand binaryCmd = new CBinaryReadADPUCommand((short) (i++ * (readLength & 0xFF)),
                        readLength);
                binaryCmd.setMessageCoding(messageCoding_);
                resp = cardAccess_.sendAPDUCommand(binaryCmd);
                data.write(resp.getData());

            } while (resp.getSW() == 0x9000);
            data.close();
            return data.toByteArray();
        } catch (IOException e) {
            throw new CommunicationException("could not read data from the card: " + e.getMessage());
        }

    }

}
