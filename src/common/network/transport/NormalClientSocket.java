/*
 * PlainClientSocket.java
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

import common.util.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 30.05.12
 * Time: 10:06
 */

/**
 * Implementation of a client socket which can connect to a specific port and host address
 */
public class NormalClientSocket extends PlainClientSocket<Socket> implements IClientSocket{
    private String address_;
    private int port_;

    public NormalClientSocket(Socket socket){
        socket_ = socket;
    }

    @Override
    public boolean connect(String address, int port) {
        socket_ = new Socket();
        try {
            socket_.connect(new InetSocketAddress(address,port));
            address_ = address;
            port_ = port;
            return true;
        } catch (IOException e) {
            Logger.log("Could not connect to server socket: " + address + ":" + port);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void disconnect() {
        //TODO
    }
}
