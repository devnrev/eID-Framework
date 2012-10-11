/*
 * FeatureTable.java
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 31.07.12
 * Time: 13:31
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class represents a table of the features a card reader offers
 */
public class FeatureTable {
    private Map<Integer, Integer> featureMap_;

    /**
     * Constructor
     * @param data Raw feature data
     * @throws ParsingException
     */
    public FeatureTable(byte[] data) throws ParsingException {
        if(data.length % 6 == 0){
            featureMap_ = new HashMap<Integer, Integer>();
            for(int i = 0; i < data.length-1; i = i + 6){
                byte[] val = Arrays.copyOfRange(data,i+2,i+6);
                try {
                    int controlCode = CConverter.convertByteArrayToIntLittleEndian(val);
                    featureMap_.put((int) data[i],controlCode);
                } catch (ConverterException e) {
                    throw new ParsingException("feature table is not in expected format:"+e.getMessage());
                }
            }


        }else{
            throw new ParsingException("feature table is not in expected format");
        }
    }

    /**
     * Get the control code for a specific feature
     * @param featureCode Feature code
     * @return Control code; -1 of feature code not found
     */
    public int getControlCodeForFeature(int featureCode){
        if(featureMap_.containsKey(featureCode)){
            return featureMap_.get(featureCode);
        }
        return -1;
    }

}
