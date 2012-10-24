/*
 * NetworkClientHttpHandler.java
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

import common.exceptions.TranscodingException;
import common.network.IHttpMessageObserver;
import common.util.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 29.05.12
 * Time: 16:09
 */

/**
 * This class handles data received from a server via a socket connection
 */
public class NetworkClientHttpHandler extends Thread {
    private IHttpMessageObserver objectToNotify_;
    private Socket clientSocket_;
    private boolean run_;
    private HttpClientMessageStreamProcessor messageProcessor_;
    private String context_;

    public NetworkClientHttpHandler(Socket clientSocket, IHttpMessageObserver observer, String context) {
        objectToNotify_ = observer;
        clientSocket_ = clientSocket;
        messageProcessor_ = new HttpClientMessageStreamProcessor();
        context_ = context;
    }

    @Override
    public void run() {
        run_ = true;
        while (run_ || !currentThread().isInterrupted()) {
            try {
                InputStream in = clientSocket_.getInputStream();
                if (in != null) {
                    HttpRequest req = messageProcessor_.decode(in);
                    if (req != null) {
                        if (req.getRessourcePath().startsWith(context_)) {
                            HttpResponse resp = objectToNotify_.update(req);
                            if (resp != null) {
                                messageProcessor_.encode(resp, clientSocket_.getOutputStream());
                            }
                        }
                    }
                }
            } catch (SocketException e) {

            } catch (IOException e) {
                Logger.log("Error during reading input stream from client");
               // e.printStackTrace();

            } catch (TranscodingException e) {
                Logger.log("Error during reading input stream from client");
                //e.printStackTrace();

            }
        }
    }

    @Override
    public void interrupt() {
        run_ = false;
        try {
            clientSocket_.close();
        } catch (IOException e) {
            e.printStackTrace();  //TODO
        }
        super.interrupt();

    }

    public void requestStop() {
        run_ = false;
    }

}
