/*
 * ICryptoProvider.java
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

package common.crypto;

import java.io.IOException;
import java.math.BigInteger;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 12.12.11
 * Time: 11:30
 * To change this template use File | Settings | File Templates.
 */

/**
 * Interface for a crypto provider which is used by the framework
 */
public interface ICryptoProvider {
    
    IDERTaggedObject createDERTaggetObject(boolean explicit,int tagNo,IDEREncodable object);
    IDEREncodable createDERInteger(int nVal);
    IDERObjectIdentifier createDERObjectIdentifier(String szVal);
    IDERSet createDERSet(byte[] byaBytes) throws IOException;
    IDEROctedString createDEROctedString(byte[] byaData);
    IDERSequence createDERSequence(byte[] byaData) throws IOException;
    IASN1EncodableVector createASN1EncodableVector();
    IDERApplicationSpecific createDERApplicationSpecific(int nTagNo, IASN1EncodableVector vec);
    IDERApplicationSpecific createDERApplicationSpecific(byte[] byaData) throws IOException;
    ICryptoSHA createSHAGenerator();
    ISymmetricBlockCipher createAESProcessor();
    IRandomNumGen getRandomNumberGenerator();
    IECFieldElementFP createECFieldElementFP(BigInteger nQ,BigInteger nX);
    IECPointFP createECPointFP(IECCurveFP curve,IECFieldElement x,IECFieldElement y);
    IX9ECParameters getX9ECParametersFromTeleTrusTCurve(String szCurveName);
    IX9ECParameters getX9ECParametersFromSECCurve(String szCurveName);
    ISymmetricMAC createCMacAuthenticator();
    IEccKeyGenerator createEccKeyGenerator(IX9ECParameters curveSpec) throws Exception;
}
