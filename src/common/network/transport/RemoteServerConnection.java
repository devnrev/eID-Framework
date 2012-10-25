/*
 * RemoteConnection.java
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

package common.network.transport;

import common.exceptions.TranscodingException;
import common.network.messaging.IStreamMessageProcessor;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 20.06.12
 * Time: 15:06
 */

/**
 * Class which represents a generic skeleton for sending messages to a server over a client socket.
 * Messages are send and received using output and input streams
 * @param <P>
 * @param <R>
 */
public class RemoteServerConnection<P, R> {

    protected IStreamMessageProcessor<P, R> streamMessageProcessor_;
    protected IClientSocket clientSocket_;

    public RemoteServerConnection(IStreamMessageProcessor<P, R> byteMessageProcessor,
                                  IClientSocket clientSocket) {

        streamMessageProcessor_ = byteMessageProcessor;
        clientSocket_ = clientSocket;
    }

    public R sendMessage(P message) throws TranscodingException {
        try {
            OutputStream out = clientSocket_.getOutputStream();
            streamMessageProcessor_.encode(message, out);
            out.flush();
            InputStream in = clientSocket_.getInputStream();
            return streamMessageProcessor_.decode(in);
        } catch (IOException e) {
            throw new TranscodingException(e.getMessage());
        }
    }


}
