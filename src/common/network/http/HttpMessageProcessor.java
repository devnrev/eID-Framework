/*
 * HttpMessageProcessor.java
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

import common.network.messaging.IByteMessageProcessor;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 20.06.12
 * Time: 15:14
 */
public class HttpMessageProcessor implements IByteMessageProcessor<HttpRequest,HttpResponse> {

    @Override
    public byte[] encode(HttpRequest message) {
        return new byte[0];  //TODO
    }

    @Override
    public HttpResponse decode(byte[] message) {
        return null;  //TODO
    }
}
