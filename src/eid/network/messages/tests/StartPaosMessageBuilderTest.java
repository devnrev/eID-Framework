/*
 * StartPaosMessageBuilderTest.java
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

import common.network.paos.PAOSResponse;
import eid.network.data.ChannelHandleType;
import eid.network.data.ConnectionHandleType;
import eid.network.data.SlotHandleType;
import eid.network.data.StartPAOSData;
import eid.network.messages.StartPaosMessageBuilder;
import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 05.07.12
 * Time: 11:02
 */
public class StartPaosMessageBuilderTest extends TestCase {
    public void testBuildBody() throws Exception {

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
        resp.writeTo(System.out);

    }
}
