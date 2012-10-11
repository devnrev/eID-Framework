/*
 * CCertHolderAuthTemplateTest.java
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

package libeac.tests;

import junit.framework.TestCase;
import libeac.CertHolderAuthTemplate;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 21.07.12
 * Time: 18:45
 */
public class CertHolderAuthTemplateTest extends TestCase {
    public void testFromBytes() throws Exception {
        byte[] chat = DatatypeConverter.parseHexBinary("7F4C12060904007F00070301020253050000000000");
        CertHolderAuthTemplate certHolderauthTemplate = CertHolderAuthTemplate.fromBytes(chat);
        assertTrue(Arrays.equals(chat,certHolderauthTemplate.getEncoded()));
    }
}
