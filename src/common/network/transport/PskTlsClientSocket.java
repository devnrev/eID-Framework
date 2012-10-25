/*
 * TlsClientSocket.java
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

import org.bouncycastle.crypto.tls.TlsProtocolHandler;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 20.06.12
 * Time: 16:24
 */

/**
 * TLS client socket implementation based on the BouncyCastle crypto library
 */
public class PskTlsClientSocket implements IClientSocket {
    private Socket socket_;
    private TlsProtocolHandler tlsHandler_;
    private PskTlsAuthClient pskTlsClient_;
    private TlsPskInfo tlsPskInfo_;


    public PskTlsClientSocket(TlsPskInfo pskInfo){
       tlsPskInfo_ = pskInfo;
    }

    @Override
    public boolean connect(String address, int port) {

        Logger.log("try to connect to:" + address + ":" + port);
        try {
            socket_ = new Socket(address,port);
            Logger.log("initialize tls protocol handler");
            tlsHandler_ = new TlsProtocolHandler(socket_.getInputStream(),socket_.getOutputStream());
            Logger.log("initialize pks client");
            Logger.log("pskIdentity: "+ new String(tlsPskInfo_.getPSKIdentity()));
            Logger.log("psk: "+ DatatypeConverter.printHexBinary(tlsPskInfo_.getPSK()));
            pskTlsClient_ = new PskTlsAuthClient(tlsPskInfo_);
            Logger.log("connect with psk client");
            tlsHandler_.connect(pskTlsClient_);
            Logger.log("psk client connected");
            return true;
        } catch (IOException e) {
            e.printStackTrace();  //TODO Exception definition
            return false;
        }
    }

    @Override
    public void disconnect() {
        try {
            tlsHandler_.close();
            socket_.close();
        } catch (IOException e) {
            e.printStackTrace();  //TODO Exception definition
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return tlsHandler_.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return tlsHandler_.getOutputStream();
    }
}
