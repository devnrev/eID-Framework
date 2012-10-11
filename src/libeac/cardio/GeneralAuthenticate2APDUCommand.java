/*
 * GeneralAuthenticate2APDUCommand.java
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

package libeac.cardio;

import common.CArrayHelper;
import common.cardio.CAPDUCommand;
import common.util.Logger;

import javax.xml.bind.DatatypeConverter;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 25.07.12
 * Time: 17:23
 */

/**
 * This class represents the  general authenticate command used for sending the
 * ephemeral public key during the chip authentication.
 */
public class GeneralAuthenticate2APDUCommand extends CAPDUCommand {

    /**
     * Constructor
     * @param key Ephemeral public key
     */
    public GeneralAuthenticate2APDUCommand(byte[] key) {
        super((byte)0x0, (byte)0x86, 0x00, 0x00);
        byte[] keyData = key;
        if(key.length == 64){
            keyData = CArrayHelper.concatArrays(new byte[]{0x04},keyData);
        }
        byte[] data =  CArrayHelper.concatArrays(new byte[]{(byte) 0x7C, (byte) (keyData.length + 2),
                (byte) 0x80, (byte) keyData.length}, keyData);
        appendData(data);
        Logger.log("data:\n" + DatatypeConverter.printHexBinary(m_byaData));
        case_ = 4;
        m_nLe = 0;
    }
}
