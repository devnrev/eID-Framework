/*
 * SelectMasterFileAPDUCommand.java
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

import common.cardio.CAPDUCommand;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 25.07.12
 * Time: 11:52
 */

/**
 * This class is used to select the master file on the card
 */
public class SelectMasterFileAPDUCommand extends CAPDUCommand {

    /**
     * Constructor
     */
    public SelectMasterFileAPDUCommand(){
        super(0x00,(byte)0xA4,(byte)0x00,(byte)0x0C);
        case_ = 3;
        appendData(new byte[]{(byte) 0x3F, (byte) 0x00});
    }
}