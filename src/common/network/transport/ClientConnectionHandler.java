/*
 * ClientConnectionHandler.java
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
import common.util.Logger;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 28.06.12
 * Time: 10:54
 */

/**
 * This generic class
 * @param <P>
 * @param <R>
 */

/**
 * Handler to process incoming messages from a client over a socket connection
 * @param <P> Response type
 * @param <R> Request type
 */

public class ClientConnectionHandler<P, R> implements Runnable {
    protected IStreamMessageProcessor<P, R> messageProcessor_;
    protected IClientSocket clientSocket_;
    protected IMessageHandler<P, R> messageHandler_;
    protected Thread clientThread_;

    public ClientConnectionHandler(IClientSocket clientSocket,
                                   IStreamMessageProcessor<P, R> messageProcessor,
                                   IMessageHandler<P, R> messageHandler) {
        clientSocket_ = clientSocket;
        messageProcessor_ = messageProcessor;
        messageHandler_ = messageHandler;
        clientThread_ = new Thread(this);
    }

    @Override
    public void run() {
        while (true) {
            try {
                R decodedMessage = messageProcessor_.decode(clientSocket_.getInputStream());
                P returnMessage = messageHandler_.handle(decodedMessage);
                messageProcessor_.encode(returnMessage,clientSocket_.getOutputStream());
            } catch (IOException e) {
                Logger.log("error during processing message of client");
                e.printStackTrace();  //TODO
            } catch (TranscodingException e) {
                e.printStackTrace();  //TODO
            }

        }
    }
}
