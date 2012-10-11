/*
 * FeatureRequest.java
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

import common.CArrayHelper;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 27.07.12
 * Time: 15:51
 */

/**
 * This command class is used defines what function should be executed on a card reader
 */
public class CommandRequest {
    protected byte function_;
    protected short length_;
    protected byte[] data_;

    /**
     * Constructor
     * @param function Function number
     */
    public CommandRequest(byte function) {
        this.function_ = function;
        this.length_ = 0;
    }

    /**
     * Constructor
     * @param function Function number
     * @param data Additional data
     */
    public CommandRequest(byte function, byte[] data) {
        this.function_ = function;
        this.length_ = (short)data.length;
        this.data_ = data;
    }

    /**
     * Get the function number
     * @return Function number
     */
    public byte getFunction() {
        return function_;
    }

    /**
     * Get the data length
     * @return Data length
     */
    public short getLength() {
        return length_;
    }

    /**
     * Get the data
     * @return Data
     */
    public byte[] getData() {
        return data_;
    }

    /**
     * Encode the command
     * @return Encoded command
     */
    public byte[] getBytes(){
        if(data_ == null){
            length_ = 0;
        }else{
            length_ = (short)data_.length;
        }

        byte[] res = new byte[]{function_, (byte) (length_ & 0xFF), (byte) ((length_ & 0xFF00) >> 0x8)};
        if(length_>0){
            return CArrayHelper.concatArrays(res,data_);
        }
        return res;
    }
}
