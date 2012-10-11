/*
 * HttpClientMessageStreamProcessorTest.java
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

package common.network.http.tests;

import common.network.http.HttpClientMessageStreamProcessor;
import common.network.http.HttpRequest;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 09.07.12
 * Time: 15:36
 */
public class HttpClientMessageStreamProcessorTest extends TestCase {
    public void testDecode() throws Exception {
        String httpreq = "POST /?sessionid=e29270f1b8e745edcc9fe0b9caa9 HTTP/1.1\n";
        httpreq+="Content-Length: 0\n";
        httpreq+="Accept: text/html; application/vnd.paos+xml\n\n";
        ByteArrayInputStream bis = new ByteArrayInputStream(httpreq.getBytes());
        HttpClientMessageStreamProcessor hcmsp = new HttpClientMessageStreamProcessor();
        HttpRequest req = hcmsp.decode(bis);
        assertEquals(httpreq,req.toString());

        String httpreq2 = "POST /?sessionid=e29270f1b8e745edcc9fe0b9caa9 HTTP/1.1\n";
        httpreq2+="Content-Length: 4\n";
        httpreq2+="Accept: text/html; application/vnd.paos+xml\n\n";
        httpreq2+="test";
        bis = new ByteArrayInputStream(httpreq2.getBytes());
        req = hcmsp.decode(bis);
        assertEquals(httpreq2,req.toString());


    }
}
