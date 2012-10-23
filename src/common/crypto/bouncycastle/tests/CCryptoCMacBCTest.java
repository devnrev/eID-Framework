/*
 * CCryptoCMacBCTest.java
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
import common.crypto.bouncycastle.CCryptoCMacBC;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 20.12.11
 * Time: 23:34
 * To change this template use File | Settings | File Templates.
 */

/**
 * Unit test for the CMAC implementation. Several input data vectors are tested.
 */
public class CCryptoCMacBCTest extends TestCase {

    public void testCalculate_128() throws Exception {
        CCryptoCMacBC cmac = new CCryptoCMacBC();
        assertNotNull(cmac);
        cmac.initialize(CryptoTypes.EKeyLength.LEN_128);
        byte[] byaKey = DatatypeConverter.parseHexBinary("2b7e151628aed2a6abf7158809cf4f3c");
        byte[] byaNull = new byte[]{};
        byte[] byaNullTest = DatatypeConverter.parseHexBinary("bb1d6929e95937287fa37d129b756746");
        byte[] byaRes = cmac.calculate(byaKey,byaNull);
        boolean arrEq = Arrays.equals(byaRes,byaNullTest);
        assertTrue(arrEq);
        byte[] byaMsg = DatatypeConverter.parseHexBinary("6bc1bee22e409f96e93d7e117393172a");
        byte[] byaMsgTest = DatatypeConverter.parseHexBinary("070a16b46b4d4144f79bdd9dd04a287c");
        byaRes = cmac.calculate(byaKey,byaMsg);
        arrEq = Arrays.equals(byaRes,byaMsgTest);
        assertTrue(arrEq);
        byte[] byaMsg2 = DatatypeConverter.parseHexBinary("6bc1bee22e409f96e93d7e117393172aae2d8a571e03ac9c9eb76fac45af8e5130c81c46a35ce411");
        byte[] byaMs2Test = DatatypeConverter.parseHexBinary("dfa66747de9ae63030ca32611497c827");
        byaRes = cmac.calculate(byaKey,byaMsg2);
        arrEq = Arrays.equals(byaRes,byaMs2Test);
        assertTrue(arrEq);
    }
}
