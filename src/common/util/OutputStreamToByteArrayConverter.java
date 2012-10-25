/*
 * OutputStreamToByteArrayConverter.java
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

import common.exceptions.BuildException;

import java.io.ByteArrayOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 06.07.12
 * Time: 15:21
 */

/**
 * Class which converts any OutputStreamResult input into a byte array
 */
public class OutputStreamToByteArrayConverter implements IDataConverter<IOutputStreamResult,byte[]> {

    @Override
    public  byte[] convert(IOutputStreamResult data) throws BuildException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        data.writeTo(out);
        return out.toByteArray();
    }
}
