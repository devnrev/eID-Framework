/*
 * PAOSConnection.java
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

package common.network.paos;

import common.network.transport.IClientSocket;
import common.network.transport.RemoteServerConnection;
import eid.network.messages.PaosHttpRequestBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 02.07.12
 * Time: 17:31
 */
public class PAOSConnection extends RemoteServerConnection<PAOSResponse,PAOSRequest> {

    public PAOSConnection(IClientSocket clientSocket,String sessionId,String host,String ressourcePath) {
        super(new PaosToHttpProcessor(new PaosHttpRequestBuilder(sessionId,host,ressourcePath)), clientSocket);
    }
}
