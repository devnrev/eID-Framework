/*
 * ConverterException.java
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

package common.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 31.07.12
 * Time: 13:45
 * To change this template use File | Settings | File Templates.
 */

/**
 * Exception class which indicates converting errors
 */
public class ConverterException extends Exception {
    public ConverterException(String message) {
        super(message);
    }
}
