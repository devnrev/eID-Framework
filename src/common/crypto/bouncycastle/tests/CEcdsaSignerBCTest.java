/*
 * CEcdsaSignerBCTest.java
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

import common.CFactoryHelper;
import common.crypto.CryptoTypes;
import common.crypto.EccKeyPair;
import common.crypto.EcdsaSignature;
import common.crypto.bouncycastle.CEccKeyGeneratorBC;
import common.crypto.bouncycastle.CEcdsaSignerBC;
import common.util.Logger;
import junit.framework.TestCase;

import javax.xml.bind.DatatypeConverter;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 10.01.12
 * Time: 12:25
 * To change this template use File | Settings | File Templates.
 */
public class CEcdsaSignerBCTest extends TestCase {
    public void testSign() throws Exception {
        CEcdsaSignerBC signer = new CEcdsaSignerBC();
        signer.initialize("brainpoolp512r1", CryptoTypes.ESHAMode.SHA512);
        String szDataToSign =   "282CF38073036AFAC216AF135BD994DA0C357F10BD4C34AFEA1042B2EB0FD680" +
                                "547E4EAB03B235D25A7A377FC9CAFC03AC7FF45441A8B2909D88EAB8E6B01738" +
                                "47AB49B949DF3799";      
        CEccKeyGeneratorBC ecckeygen = new CEccKeyGeneratorBC();
        ecckeygen.initialize( CFactoryHelper.getCryptoProvider().getX9ECParametersFromTeleTrusTCurve("brainpoolp512r1"));
        EccKeyPair ecKeyPair = ecckeygen.generate();
        
        EcdsaSignature sig = signer.sign(DatatypeConverter.parseHexBinary(szDataToSign),ecKeyPair.getSecretKey());

        assertNotNull(sig);
        Logger.log("r: " + DatatypeConverter.printHexBinary(sig.getR().toByteArray()) + "\ns: " +
                DatatypeConverter.printHexBinary(sig.getS().toByteArray()));
        boolean bVerifyResult = signer.verifySignature(DatatypeConverter.parseHexBinary(szDataToSign),sig,ecKeyPair.getPublicKey());
        Logger.log("Signature verified with public key: " + bVerifyResult);
        assertTrue(bVerifyResult);
    }
}
