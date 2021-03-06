/*
 * CGetChallengeAPDUCommand.java
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
 * Date: 08.01.12
 * Time: 20:17
 * To change this template use File | Settings | File Templates.
 */

/**
 * This APDU command class is used to get the challenge from the card
 */
public class CGetChallengeAPDUCommand extends CAPDUCommand {

    /**
     * Constructor
     */
    public CGetChallengeAPDUCommand(){
        super(0x00,(byte)0x84,0x00,0x00,0x08);
        case_ = 2;
    }

}
