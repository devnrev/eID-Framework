/*
 * CAPDUSecureMessagingTest.java
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

package common.cardio.coding.test;

import common.cardio.CAPDUResponse;
import common.cardio.coding.CAPDUSecureMessaging;
import common.crypto.CryptoTypes;
import junit.framework.TestCase;
import libeac.cardio.CMSEDSTAPDUCommand;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 28.12.11
 * Time: 00:09
 * To change this template use File | Settings | File Templates.
 */

/**
 * Test class for the secure message coding of APDU messages
 */
public class CAPDUSecureMessagingTest extends TestCase {
    private final String szKENC = "68406B4162100563D9C901A6154D2901";
    private final String szKMAC = "73FF268784F72AF833FDC9464049AFC9";

    public void testEncode() throws Exception {
        CAPDUSecureMessaging secMsg = new CAPDUSecureMessaging(DatatypeConverter.parseHexBinary(szKENC),DatatypeConverter.parseHexBinary(szKMAC), CryptoTypes.ESymmetricBlockCipher.AES);
        CMSEDSTAPDUCommand carMSE = new CMSEDSTAPDUCommand();
        carMSE.appendData(new byte[]{ (byte)0x83, 0x0D, 0x44, 0x45, 0x43, 0x56, 0x43, 0x41, 0x41, 0x54, 0x30, 0x30, 0x30, 0x30, 0x31});
        carMSE.setMessageCoding(secMsg);
        String szTestVec1 = "0C2281B61D871101BE90237EEB4BA0FF253EA246AE31C8B88E0892D21C73A1DFE99900";
        byte[] byaRes = carMSE.getBytes();
        assertTrue(Arrays.equals(byaRes, DatatypeConverter.parseHexBinary(szTestVec1)));
    }

    public void testDecode() throws Exception {
        String szResp = "990290008E08A89570A68664A7D6";
        
        CAPDUSecureMessaging secMsg = new CAPDUSecureMessaging(DatatypeConverter.parseHexBinary(szKENC),DatatypeConverter.parseHexBinary(szKMAC), CryptoTypes.ESymmetricBlockCipher.AES);
        CAPDUResponse resp = secMsg.decode(DatatypeConverter.parseHexBinary(szResp));
        assertNotNull(resp);


    }
}
