/*
 * RemoteClientConnection.java
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

import common.network.messaging.IStreamMessageProcessor;
import common.util.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 28.06.12
 * Time: 10:47
 */

/**
 * Generic class which is used to accept clients on a server socket and assign them to a specific handler
 * @param <P> Response type
 * @param <R> Request type
 */
public class RemoteClientConnection<P,R> implements Runnable{
    protected IServerSocket serverSocket_;
    protected IStreamMessageProcessor<P,R> messageProcessor_;
    protected Thread receiverThread_;
    protected List<ClientConnectionHandler<P,R>> clientThreadList_;
    protected IMessageHandler<P,R> messageHandler_;

    public RemoteClientConnection(IServerSocket serverSocket,
                                  IStreamMessageProcessor<P,R> messageProcessor){
        serverSocket_ = serverSocket;
        messageProcessor_ = messageProcessor;
        receiverThread_ = new Thread(this);
    }

    @Override
    public void run() {
        while(true){
            try {
                IClientSocket client = serverSocket_.accept();
                clientThreadList_.add(new ClientConnectionHandler<P, R>(client,messageProcessor_,messageHandler_));

            } catch (IOException e) {
                Logger.log("error during accepting client");
                e.printStackTrace();
            }
        }
    }
}
