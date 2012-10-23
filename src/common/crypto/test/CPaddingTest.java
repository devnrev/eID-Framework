/*
 * CPaddingTest.java
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

package common.crypto.test;

import common.crypto.CPadding;
import junit.framework.TestCase;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 27.12.11
 * Time: 16:54
 * To change this template use File | Settings | File Templates.
 */

/**
 * Unit test for the check the implemented padding methods.
 */
public class CPaddingTest extends TestCase {

    /**
     * Test secure message padding
     * @throws Exception
     */
    public void testApplySecMsgPadding() throws Exception {
        int nBlockSize = 8;
        byte[] byaTestVec1 = DatatypeConverter.parseHexBinary("11223344");
        byte[] byaRes = CPadding.applySecMsgPadding(byaTestVec1,nBlockSize);
        assertTrue(Arrays.equals(byaRes,DatatypeConverter.parseHexBinary("1122334480000000")));
        byte[] byaTestVec2 = DatatypeConverter.parseHexBinary("11223344556677");
        byaRes = CPadding.applySecMsgPadding(byaTestVec2,nBlockSize);
        assertTrue(Arrays.equals(byaRes,DatatypeConverter.parseHexBinary("1122334455667780")));
        byte[] byaTestVec3 = DatatypeConverter.parseHexBinary("1122334455667788");
        byaRes = CPadding.applySecMsgPadding(byaTestVec3,nBlockSize);
        assertTrue(Arrays.equals(byaRes,DatatypeConverter.parseHexBinary("11223344556677888000000000000000")));
        byte[] byaTestVec4 = DatatypeConverter.parseHexBinary("");
        byaRes = CPadding.applySecMsgPadding(byaTestVec4,nBlockSize);
        assertTrue(Arrays.equals(byaRes,DatatypeConverter.parseHexBinary("8000000000000000")));
    }
}
