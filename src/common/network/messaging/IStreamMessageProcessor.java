/*
 * IStreamMessageProcessor.java
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

package common.network.messaging;

import common.exceptions.TranscodingException;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 21.06.12
 * Time: 13:12
 */

/**
 * Generic interface which defines methods to encode data into an OutputStream and decode data from an InputStream
 * @param <P> Data type of the outgoing message
 * @param <R> Data type of the incoming message
 */
public interface IStreamMessageProcessor<P,R>{
    void encode(P message,OutputStream out) throws TranscodingException;
    R decode(InputStream message) throws TranscodingException, SocketException;

}
