/*
 * HttpResponse.java
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

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 20.06.12
 * Time: 15:15
 */

/**
 * This class defines a HTTP response and provides the required attributes
 */
public class HttpResponse extends HttpMessage {
    private int status_;
    private String reason_;


    @Override
    protected String getInitialLine() {
        return version_+" "+status_+" "+reason_+"\n";
    }

    public HttpResponse(HttpVersion version,int status,String reason){
        super();
        version_ = version;
        status_ = status;
        reason_ = reason;

    }

    public HttpResponse(HttpHeader header, byte[] body) {
        super(header, body);
    }

    public int getStatus() {
        return status_;
    }

    public String getReason() {
        return reason_;
    }
}
