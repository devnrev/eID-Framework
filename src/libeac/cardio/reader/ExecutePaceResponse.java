/*
 * ExecutePaceResponse.java
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
import common.exceptions.ConverterException;
import common.exceptions.ParsingException;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 31.07.12
 * Time: 14:21
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class handles the card reader response when pace should be executed
 */
public class ExecutePaceResponse {
    private int status_;
    private byte[] data_;

    /**
     * Constructor
     * @param data Card reader response data
     * @throws ParsingException
     */
    public ExecutePaceResponse(byte[] data) throws ParsingException {
        if(data.length < 6){
            throw new ParsingException("response not in correct format");
        }
        int length;
        try {
            status_ = CConverter.convertByteArrayToIntBigEndian(Arrays.copyOf(data, 4));
            length = CConverter.convertByteArrayToIntBigEndian(Arrays.copyOfRange(data, 4, 6));
        } catch (ConverterException e) {
            throw new ParsingException("status or length could not be converted to integer");
        }

        if(data.length < length+6){
            throw new ParsingException("data length is shorter than expected");
        }
        data_ = Arrays.copyOfRange(data,6,6+length);
    }

    /**
     * Get the status code
     * @return Status code
     */
    public int getStatus(){
        return status_;
    }

    /**
     * Get the response data
     * @return Response data
     */
    public byte[] getData(){
        return data_;
    }
}
