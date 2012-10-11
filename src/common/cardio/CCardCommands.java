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

    /*
    public byte[] readFile(byte bySFID) throws Exception {
        if (bySFID > 0x1F)
            throw new Exception("SfID out of range");
        CBinaryReadADPUCommand binaryCmd = new CBinaryReadADPUCommand(bySFID, (byte) 0x08);
        if(messageCoding_!=null){
            binaryCmd.setMessageCoding(messageCoding_);
        }

        CAPDUResponse resp = cardAccess_.sendAPDUCommand(binaryCmd);
        if (resp.getSW() != 0x9000)
            throw new Exception("Could not read binary data for SFID: " + bySFID + " response: \n" + resp.toString());

        int nRemainingBytes = readLength(resp.getData());
        byte[] byaFileData = new byte[nRemainingBytes];
        int nOffset = 0;
        int nReadLength = 0xFF; //could also be 0xF0

        do {
            if (nRemainingBytes < nReadLength)
                nReadLength = nRemainingBytes;

            binaryCmd = new CBinaryReadADPUCommand((byte) ((nOffset & 0xFF00) >> 8),
                                                   (byte) (nOffset & 0xFF), nReadLength);
            if(messageCoding_!=null){
                binaryCmd.setMessageCoding(messageCoding_);
            }
            resp = cardAccess_.sendAPDUCommand(binaryCmd);
            byte[] data = resp.getData();
            System.arraycopy(data, 0, byaFileData, nOffset, data.length);
            nOffset += data.length;
            nRemainingBytes -= data.length;
        } while (nRemainingBytes > 0);

        return byaFileData;
    }


    //copied from Bouncy Castle ASN1InputStream
    //return value + 2 because of 1 byte for tag and 1 byte
    protected int readLength(byte[] byaData) throws IOException {
        ByteArrayInputStream s = new ByteArrayInputStream(byaData);
        s.read();
        int nSize = 0;
        int nLength = s.read();
        if (nLength < 0) {
            throw new EOFException("EOF found when length expected");
        }

        if (nLength == 0x80) {
            return -1;      // indefinite-length encoding
        }

        if (nLength > 127) {
            nSize = nLength & 0x7f;

            // Note: The invalid long form "0xff" (see X.690 8.1.3.5c) will be caught here
            if (nSize > 4) {
                throw new IOException("DER length more than 4 bytes: " + nSize);
            }

            nLength = 0;
            for (int i = 0; i < nSize; i++) {
                int nNext = s.read();

                if (nNext < 0) {
                    throw new EOFException("EOF found reading length");
                }

                nLength = (nLength << 8) + nNext;
            }

            if (nLength < 0) {
                throw new IOException("corrupted stream - negative length found");
            }

        }
        return nLength + nSize + 2;
    }
    */
}
