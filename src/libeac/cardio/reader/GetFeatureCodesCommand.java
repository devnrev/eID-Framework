/*
 * FeatureCodesCommand.java
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

package libeac.cardio.reader;

import common.Globals;
import common.cardio.ControlCommand;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 27.07.12
 * Time: 16:13
 */

/**
 * This class is used to get the supported features by the card reader
 */
public class GetFeatureCodesCommand extends ControlCommand {

    /**
     * Constructor
     */
    public GetFeatureCodesCommand() {
        super();
        controlCode_ = SCARD_CTL_CODE(3400);
    }

    /**
     * Control code wrapper
     * @param code Control code
     * @return Wraped control code
     */
    private int SCARD_CTL_CODE(int code){
        if(Globals.getOSType() == Globals.OS.WINDOWS){
            return (0x31<<16 | code << 2);
        }else{
            return 0x42000000 + (code);
        }

    }
}
