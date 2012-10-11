/*
 * CGetNonceADPUCommand.java
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
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 13.12.11
 * Time: 14:44
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class is used to get the nonce from the card
 */
public class CGetNonceADPUCommand extends CAPDUCommand {
    public CGetNonceADPUCommand(){
        super((byte)0x10,(byte)0x86,0x00,0x00,new byte[]{0x7C,0x00},0x00);
    }

}
