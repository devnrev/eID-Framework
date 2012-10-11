/*
 * TcTokenHandlerTest.java
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

package eid.network.handler.tests;

import eid.network.data.TcTokenData;
import eid.network.handler.TcTokenHandler;
import junit.framework.TestCase;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 10.07.12
 * Time: 13:05
 */
public class TcTokenHandlerTest extends TestCase {
    public void testGetTcToken() throws Exception {
        TcTokenHandler tch = new TcTokenHandler("https://willow.mtg.de/eid-server-demo-app/result/request.html");
        TcTokenData tcToken = tch.getTcToken();
        assertEquals("fry.mtg.de:443",tcToken.getServerAddress());
        assertEquals("urn:liberty:paos:2006-08",tcToken.getBinding());
        assertEquals("urn:ietf:rfc:4279",tcToken.getPathSecurityData().getProtocol());
        assertEquals("https://willow.mtg.de:443/eid-server-demo-app/result/response.html",
                     tcToken.getRefreshAddress());
        assertEquals(32,tcToken.getPathSecurityData().getPsk().length);
        assertEquals(64,tcToken.getSessionIdentifier().length());
    }
}
