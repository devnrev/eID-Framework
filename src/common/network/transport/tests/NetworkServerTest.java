/*
 * NetworkServerTest.java
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

package common.network.transport.tests;

import common.network.IHttpMessageObserver;
import common.network.http.HttpRequest;
import common.network.http.HttpResponse;
import common.network.transport.NetworkHttpServer;
import common.util.Logger;
import junit.framework.TestCase;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 29.05.12
 * Time: 16:38
 */
public class NetworkServerTest extends TestCase implements IHttpMessageObserver {
    public void testListen() throws Exception {

        NetworkHttpServer ns = new NetworkHttpServer(41225,"/bub",this);

        ns.start();
        Logger.log("blub");
        Thread.sleep(30000);
        ns.stop();
    }

    @Override
    public HttpResponse update(HttpRequest request) {
        Logger.log(request.toString());
        return null;
    }
}
