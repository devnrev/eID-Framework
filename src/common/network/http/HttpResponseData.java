/*
 * HttpResponseData.java
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

package common.network.http;

import com.sun.net.httpserver.Headers;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 31.05.12
 * Time: 14:24
 */
public final class HttpResponseData{
    private final Headers header_;
    private final byte[] data_;

    public HttpResponseData(Headers header,byte[] data){
        header_ = header;
        data_ = data;
    }

    public Headers getHeader() {
        return header_;
    }

    public byte[] getData() {
        return data_;
    }
}
