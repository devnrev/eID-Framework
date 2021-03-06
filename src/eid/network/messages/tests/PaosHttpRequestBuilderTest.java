/*
 * PaosHttpRequestBuilderTest.java
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

package eid.network.messages.tests;

import common.network.http.HttpRequest;
import common.network.paos.PAOSResponse;
import common.util.Logger;
import eid.network.data.ChannelHandleType;
import eid.network.data.ConnectionHandleType;
import eid.network.data.SlotHandleType;
import eid.network.data.StartPAOSData;
import eid.network.messages.PaosHttpRequestBuilder;
import eid.network.messages.StartPaosMessageBuilder;
import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 06.07.12
 * Time: 15:05
 */
public class PaosHttpRequestBuilderTest extends TestCase {
    public void testGetProduct() throws Exception {
        PaosHttpRequestBuilder phrb = new PaosHttpRequestBuilder("e29270f1b8e745edcc9fe0b9caa9",
                "live.eid-service.de:443","/");
        phrb.initializeProduct();
        phrb.buildHeader();
        HttpRequest req = phrb.getProduct();
        String testVec = "POST /?sessionid=e29270f1b8e745edcc9fe0b9caa9 HTTP/1.1\n" +
                "Accept: text/html; application/vnd.paos+xml\n" +
                "PAOS: ver=\"urn:liberty:2003-08\",\"urn:liberty:2006-08\"; " +
                "http://www.bsi.bund.de/ecard/api/1.0/PAOS/GetNextCommand\n" +
                "Host: live.eid-service.de:443\n"+
                "Content-Length: 0\n\n";
        assertEquals(testVec,req.getMessage());


        ChannelHandleType cht = new ChannelHandleType("ch test","428684103954799E4D6712D0239081FD74BBBA24",
                "ifd test",1,"ca test");
        SlotHandleType sht = new SlotHandleType("05D4F40AEBD9919383C22216055EA3DB15056C51","recog info test");
        ConnectionHandleType coht = new ConnectionHandleType(cht,sht);

        StartPAOSData spd = new StartPAOSData("e29270f1b8e745edcc9fe0b9caa9",coht);
        StartPaosMessageBuilder spmb=new StartPaosMessageBuilder();
        spmb.initializeProduct();
        spmb.buildEnvelope();
        spmb.buildHeader();
        spmb.buildBody(spd);
        PAOSResponse resp = spmb.getProduct();
        phrb.initializeProduct();
        phrb.buildHeader();
        phrb.buildBody(resp);
        req = phrb.getProduct();
        Logger.log(req.getMessage());

    }
}
