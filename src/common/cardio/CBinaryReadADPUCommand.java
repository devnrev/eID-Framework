/*
 * CBinaryReadADPUCommand.java
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

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 12.12.11
 * Time: 23:39
 * To change this template use File | Settings | File Templates.
 */

/**
 * APDU Command for reading binary data from the card
 */
public class CBinaryReadADPUCommand extends CAPDUCommand {

    public CBinaryReadADPUCommand(short offset, byte length){
        super(0x00,(byte)0xB0,(byte) (((byte) (offset >> 8)) & 0x7F),(byte) (offset & 0xFF),length);
    }

    public CBinaryReadADPUCommand(byte bySFID, byte nLe) {
        super(0x00, (byte)0xB0, (0x80|bySFID), 0x00,nLe);
    }

    public CBinaryReadADPUCommand(byte byEnd,byte byStart, int nLe) {
        super(0x00, (byte)0xB0, byEnd,byStart,nLe);
    }
}
