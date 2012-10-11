/*
 * PaceCapabilities.java
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

import common.CConverter;

import java.util.BitSet;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 31.07.12
 * Time: 15:12
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class is used to access the supported PACE capabilities
 */
public class PaceCapabilities {
    private BitSet bits_;

    /**
     * Constructor
     * @param data Raw capability data
     */
    public PaceCapabilities(byte[] data){
        bits_ = CConverter.convertByteArrayToBitSet(data);
    }

    /**
     * Check if PACE is supported
     * @return Result
     */
    public boolean canDoPace(){
        return bits_.get(6);
    }

    /**
     * Check if eID functionality is supported
     * @return Result
     */
    public boolean supportsEId(){
        return bits_.get(5);
    }

    /**
     * Check if the card reader supports the eSign function
     * @return Result
     */
    public boolean supportsESign(){
        return bits_.get(4);
    }
}
