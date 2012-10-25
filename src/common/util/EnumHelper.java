/*
 * EnumHelper.java
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

package common.util;

import common.exceptions.ElementNotFoundException;
import common.exceptions.TranscodingException;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 09.07.12
 * Time: 11:50
 */

/**
 * Helper class which returns a enum value for a given string
 * @param <T> Enum type
 */
public class EnumHelper<T extends Enum<T>> {
    public T getEnumValue(Class<T> enu,String val) throws ElementNotFoundException {
        for(T e : enu.getEnumConstants() ){
             if(e.toString().equals(val)){
                 return e;
             }
        }
        throw new ElementNotFoundException("could not find enum value for: "+val);
    }
}
