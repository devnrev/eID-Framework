/*
 * PlainServerSocket.java
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
import java.net.ServerSocket;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 30.05.12
 * Time: 10:18
 */

/**
 * Basic server socket which can be opened on specific port
 */
public class NormalServerSocket implements IServerSocket {
    protected ServerSocket socket_;
    protected int port_;

    @Override
    public boolean setup(int port) {
        port_ = port;
        try {
            socket_ = new ServerSocket(port);
            return true;

        } catch (IOException e) {
            Logger.log("could not setup server socket for port: "+port);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void close() {
        try {
            socket_.close();
        } catch (IOException e) {
            Logger.log("could not close socket for port: " +port_);
            e.printStackTrace();
        }
    }

    @Override
    public IClientSocket accept() throws IOException {
        return new NormalClientSocket(socket_.accept());
    }
}
