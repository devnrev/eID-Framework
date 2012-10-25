/*
 * NetworkServer.java
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


import common.network.IHttpMessageObserver;
import common.network.http.NetworkClientHttpHandler;
import common.util.Logger;

import java.io.IOException;
import java.net.*;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 29.05.12
 * Time: 14:25
 */

/**
 * This class provides a basic HTTP web server implementation based on a server socket.
 * Every client is handled in a separate thread by its own handler.
 */
public class NetworkHttpServer implements Runnable{
    private int port_;
    private boolean run_;
    private ServerSocket socket_;
    private IHttpMessageObserver observer_;
    private List<NetworkClientHttpHandler> clientThreads_;
    private String context_;
    private Thread listeningThread_;


    public NetworkHttpServer(int port, String context, IHttpMessageObserver obs) throws IOException {
        socket_ = new ServerSocket(port);
        clientThreads_ = new ArrayList<NetworkClientHttpHandler>();
        port_ = port;
        context_ = context;
        observer_ = obs;
    }


    public void start() {
        listeningThread_ = new Thread(this);
        run_ = true;
        listeningThread_.start();
    }

    public void stop() {


        for (Thread t : clientThreads_) {
            t.interrupt();
            try {
                t.join();
            } catch (InterruptedException e) {
                Logger.log("could not join client");
            }
        }
        listeningThread_.interrupt();
        try {

            socket_.close();
        } catch (IOException e) {
            Logger.log("could not close socket");
        }

        try {
            listeningThread_.join();
        } catch (InterruptedException e) {
              Logger.log("thread terminate failure");
        }

    }

    public int getPort(){
        return port_;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket client = socket_.accept();
                    if(client != null){
                        NetworkClientHttpHandler clientHandler = new NetworkClientHttpHandler(client,
                                observer_, context_);
                        clientThreads_.add(clientHandler);
                        clientHandler.start();
                    }
                }catch(SocketException e){ }
                catch (IOException e) {
                    Logger.log("Error while accepting client!");
                    e.printStackTrace();
                }
            }

    }

    public void interrupt() {

        listeningThread_.interrupt();
    }
}
