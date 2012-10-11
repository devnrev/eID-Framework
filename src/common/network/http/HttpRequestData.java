/*
 * HttpRequestData.java
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

import java.io.InputStream;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 30.05.12
 * Time: 12:45
 */
public final class HttpRequestData {
    private final Headers header_;
    private final Map<String, String> parameters_;
    private final InputStream data_;
    private final int dataSize_;

    public HttpRequestData(Headers header, Map<String, String> parameters,InputStream data, int dataSize){
        header_ = header;
        parameters_ = parameters;
        data_ = data;
        dataSize_ = dataSize;

    }

    public Headers getHeader() {
        return header_;
    }

    public Map<String, String> getParameters(){
        return parameters_;
    }

    public InputStream getData(){
        return data_;
    }

    public int getDataSize(){
        return dataSize_;
    }
}
