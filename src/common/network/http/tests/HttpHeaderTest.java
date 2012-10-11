/*
 * HttpHeaderTest.java
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
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 06.07.12
 * Time: 11:31
 */
public class HttpHeaderTest extends TestCase {
    public void testAddHeaderInfo() throws Exception {
        HttpHeader header = new HttpHeader();
        header.addHeaderInfo("Content-Length","3896");
        String testVec = "Content-Length: 3896\n";
        assertEquals(testVec,header.toString());
        header.addHeaderInfo("Accept","text/html");
        header.addHeaderInfo("Accept","application/vnd.paos+xml");
        testVec+="Accept: text/html; application/vnd.paos+xml\n";
        assertEquals(testVec,header.toString());
        List<String> params = new ArrayList<String>();
        params.add("ver=\"urn:liberty:2003-08\",\"urn:liberty:2006-08\"");
        params.add("http://www.bsi.bund.de/ecard/api/1.0/PAOS/GetNextCommand");
        header.addHeaderInfo("PAOS",params);
        testVec+="PAOS: ver=\"urn:liberty:2003-08\",\"urn:liberty:2006-08\"; " +
                "http://www.bsi.bund.de/ecard/api/1.0/PAOS/GetNextCommand\n";
        assertEquals(testVec,header.toString());



    }
}
