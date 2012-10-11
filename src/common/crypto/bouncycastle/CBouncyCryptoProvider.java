/*
 * CBouncyCryptoProvider.java
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

package common.crypto.bouncycastle;

import common.crypto.*;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves;

import java.io.IOException;
import java.math.BigInteger;


/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 12.12.11
 * Time: 11:37
 * To change this template use File | Settings | File Templates.
 */
public class CBouncyCryptoProvider implements ICryptoProvider {
    private IRandomNumGen m_rndNumGen;


    public CBouncyCryptoProvider(){
        m_rndNumGen = new CRandomNumGenBC();

    }

    @Override
    public IDERTaggedObject createDERTaggetObject(boolean explicit, int tagNo, IDEREncodable object) {
        return new CDERTaggedObjectBC(explicit,tagNo,object);
    }

    @Override
    public IDEREncodable createDERInteger(int nVal) {
        return new CDERIntegerBC(nVal);
    }

    @Override
    public IDERObjectIdentifier createDERObjectIdentifier(String szVal) {
        return new CDERObjectIdentifierBC(szVal);
    }

    @Override
    public IDERSet createDERSet(byte[] byaBytes) throws IOException {
        return new CDERSetBC(byaBytes);
    }

    @Override
    public IDEROctedString createDEROctedString(byte[] byaData) {
        return new CDEROctetStringBC(byaData);
    }

    @Override
    public IDERSequence createDERSequence(byte[] byaData) throws IOException {
        return new CDERSequenceBC(byaData);
    }

    @Override
    public IASN1EncodableVector createASN1EncodableVector() {
        return new CASN1EncodableVectorBC();
    }

    @Override
    public IDERApplicationSpecific createDERApplicationSpecific(int nTagNo, IASN1EncodableVector vec) {
        return new CDERApplicationSpecificBC(nTagNo,vec);
    }

    @Override
    public IDERApplicationSpecific createDERApplicationSpecific(byte[] byaData) throws IOException {
        return new CDERApplicationSpecificBC(byaData);
    }

    @Override
    public ICryptoSHA createSHAGenerator() {
        return new CCryptoSHABC();
    }

    @Override
    public ISymmetricBlockCipher createAESProcessor() {
        return new CCryptoAESBC();
    }

    @Override
    public IRandomNumGen getRandomNumberGenerator() {
        return m_rndNumGen;
    }

    @Override
    public IECFieldElementFP createECFieldElementFP(BigInteger nQ, BigInteger nX) {
        return new CECFieldElementFPBC(nQ,nX);
    }

    @Override
    public IECPointFP createECPointFP(IECCurveFP curve, IECFieldElement x, IECFieldElement y) {
        return  new CECPointFPBC(curve,x,y);
    }


    @Override
    public IX9ECParameters getX9ECParametersFromTeleTrusTCurve(String szCurveName) {
        return new CX9ECParametersBC(TeleTrusTNamedCurves.getByName(szCurveName));
    }

    @Override
    public IX9ECParameters getX9ECParametersFromSECCurve(String szCurveName) {
        return new CX9ECParametersBC(SECNamedCurves.getByName(szCurveName));
    }

    @Override
    public ISymmetricMAC createCMacAuthenticator() {
        return new CCryptoCMacBC();
    }

    @Override
    public IEccKeyGenerator createEccKeyGenerator(IX9ECParameters curveSpec) throws Exception {
        CEccKeyGeneratorBC eccKeyGen = new CEccKeyGeneratorBC();
        eccKeyGen.initialize(curveSpec);
        return eccKeyGen;
    }


}
