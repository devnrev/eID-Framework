/*
 * HttpRequestTest.java
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

import common.network.http.HttpHeader;
import common.network.http.HttpMessage;
import common.network.http.HttpRequest;
import junit.framework.TestCase;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 06.07.12
 * Time: 12:04
 */
public class HttpRequestTest extends TestCase {
    public void testGetMessage() throws Exception {
        HttpRequest req = new HttpRequest();
        req.setMethod(HttpRequest.HttpMethod.POST);
        req.setVersion(HttpMessage.HttpVersion.VER_1_1);
        req.setResourcePath("/?sessionid=e29270f1b8e745edcc9fe0b9caa9");
        String testVec = "POST /?sessionid=e29270f1b8e745edcc9fe0b9caa9 HTTP/1.1\n";
        HttpHeader header = new HttpHeader();
        header.addHeaderInfo("Accept","text/html");
        header.addHeaderInfo("Accept","application/vnd.paos+xml");
        testVec+="Accept: text/html; application/vnd.paos+xml\n";
        String testVecTmp = testVec;
        testVecTmp+="Content-Length: 0\n";
        req.setHeader(header);
        assertEquals(testVecTmp+"\n",req.getMessage());
        String bodyTest = "test";
        req.setBody(bodyTest.getBytes());
        testVec+="Content-Length: 4\n";
        testVec+="\ntest";
        assertEquals(testVec,req.getMessage());
    }
}
