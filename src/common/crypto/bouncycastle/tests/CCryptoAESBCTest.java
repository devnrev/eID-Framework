/*
 * CCryptoAESBCTest.java
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

package common.crypto.bouncycastle.tests;

import junit.framework.TestCase;
import common.crypto.CryptoTypes;
import common.crypto.bouncycastle.CCryptoAESBC;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 20.12.11
 * Time: 22:26
 * To change this template use File | Settings | File Templates.
 */
public class CCryptoAESBCTest extends TestCase {


    public void testEncryption128() throws Exception {
        CCryptoAESBC aes = new CCryptoAESBC();
        assertNotNull(aes);
        byte[] byaIV = DatatypeConverter.parseHexBinary("000102030405060708090A0B0C0D0E0F");
        byte[] byaKey = DatatypeConverter.parseHexBinary("2b7e151628aed2a6abf7158809cf4f3c");
        aes.initialize(byaIV, byaKey, CryptoTypes.EBlockMode.CBC, CryptoTypes.EKeyLength.LEN_128);
        byte[] byaBuff = DatatypeConverter.parseHexBinary("6bc1bee22e409f96e93d7e117393172a");
        byte[] byaEnc=aes.encrypt(byaBuff);
        byte[] byaTestEnc = DatatypeConverter.parseHexBinary("7649abac8119b246cee98e9b12e9197d");
        boolean arrEq = Arrays.equals(byaEnc, byaTestEnc);
        assertTrue(arrEq);
        byte[] byaDec = aes.decrypt(byaEnc);
        arrEq = Arrays.equals(byaBuff,byaDec);
        assertTrue(arrEq);

    }

    public void testEncryption256() throws Exception {
        CCryptoAESBC aes = new CCryptoAESBC();
        assertNotNull(aes);
        byte[] byaIV = DatatypeConverter.parseHexBinary("000102030405060708090A0B0C0D0E0F");
        byte[] byaKey = DatatypeConverter.parseHexBinary("603deb1015ca71be2b73aef0857d77811f352c073b6108d72d9810a30914dff4");
        aes.initialize(byaIV, byaKey, CryptoTypes.EBlockMode.CBC, CryptoTypes.EKeyLength.LEN_256);
        byte[] byaBuff = DatatypeConverter.parseHexBinary("6bc1bee22e409f96e93d7e117393172a");
        byte[] byaEnc=aes.encrypt(byaBuff);
        byte[] byaTestEnc = DatatypeConverter.parseHexBinary("f58c4c04d6e5f1ba779eabfb5f7bfbd6");
        boolean arrDiff = Arrays.equals(byaEnc, byaTestEnc);
        assertTrue(arrDiff);
        byte[] byaDec = aes.decrypt(byaEnc);
        arrDiff = Arrays.equals(byaBuff,byaDec);
        assertTrue(arrDiff);

    }

}
