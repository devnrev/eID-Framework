/*
 * CCryptoSHABCTest.java
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
import common.crypto.bouncycastle.CCryptoSHABC;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 20.12.11
 * Time: 23:15
 * To change this template use File | Settings | File Templates.
 */

/**
 * Unit test for the SHA implementation. Several input data vectors and SHA types are tested.
 */
public class CCryptoSHABCTest extends TestCase {
    public void testDigest_SHA1() throws Exception {
        CCryptoSHABC sha = new CCryptoSHABC();
        assertNotNull(sha);
        sha.initialize(CryptoTypes.ESHAMode.SHA1);
        byte[] byaNull = new byte[]{};
        byte[] byaNullTest = DatatypeConverter.parseHexBinary("da39a3ee5e6b4b0d3255bfef95601890afd80709");
        byte[] bDigest = sha.digest(byaNull);
        boolean arrEq = Arrays.equals(bDigest,byaNullTest);
        assertTrue(arrEq);
        byte[] byaMsg =  ("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq").getBytes();
        byte[] byaMsgTest =  DatatypeConverter.parseHexBinary("84983e441c3bd26ebaae4aa1f95129e5e54670f1");
        bDigest = sha.digest(byaMsg);
        arrEq = Arrays.equals(bDigest,byaMsgTest);
        assertTrue(arrEq);
    }

    public void testDigest_SHA256() throws Exception {
        CCryptoSHABC sha = new CCryptoSHABC();
        assertNotNull(sha);
        sha.initialize(CryptoTypes.ESHAMode.SHA256);
        byte[] byaNull = new byte[]{};
        byte[] byaNullTest = DatatypeConverter.parseHexBinary("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        byte[] byaDigest = sha.digest(byaNull);
        boolean arrEq = Arrays.equals(byaDigest,byaNullTest);
        assertTrue(arrEq);
        byte[] byaMsg =  ("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq").getBytes();
        byte[] byaMsgTest =  DatatypeConverter.parseHexBinary("248D6A61D20638B8E5C026930C3E6039A33CE45964FF2167F6ECEDD419DB06C1");
        byaDigest = sha.digest(byaMsg);
        arrEq = Arrays.equals(byaDigest,byaMsgTest);
        assertTrue(arrEq);
    }

    public void testDigest_SHA512() throws Exception {
        CCryptoSHABC sha = new CCryptoSHABC();
        assertNotNull(sha);
        sha.initialize(CryptoTypes.ESHAMode.SHA512);
        byte[] byaNull = new byte[]{};
        byte[] byaNullTest = DatatypeConverter.parseHexBinary("cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce" +
                                                              "47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e");
        byte[] byaDigest = sha.digest(byaNull);
        boolean arrEq = Arrays.equals(byaDigest,byaNullTest);
        assertTrue(arrEq);
        byte[] byaMsg =  ("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq").getBytes();
        byte[] byaMsgTest =  DatatypeConverter.parseHexBinary("204A8FC6DDA82F0A0CED7BEB8E08A41657C16EF468B228A8279BE331A703C335" +
                                                              "96FD15C13B1B07F9AA1D3BEA57789CA031AD85C7A71DD70354EC631238CA3445");
        byaDigest = sha.digest(byaMsg);
        arrEq = Arrays.equals(byaDigest,byaMsgTest);
        assertTrue(arrEq);
    }
}
