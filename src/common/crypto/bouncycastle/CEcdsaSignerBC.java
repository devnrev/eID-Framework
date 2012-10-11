/*
 * CEcdsaSignerBC.java
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

import common.IAdapter;
import common.crypto.*;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 10.01.12
 * Time: 12:03
 * To change this template use File | Settings | File Templates.
 */
public class CEcdsaSignerBC implements IEcdsaSigner {
    private ECDSASigner m_signer;
    private ECDomainParameters m_eccParameter;
    private ICryptoSHA m_shaHash;

    @Override
    public void initialize(String szCurveName, CryptoTypes.ESHAMode eShaMode) {
        ECParameterSpec paramSpec = ECNamedCurveTable.getParameterSpec(szCurveName);
        m_eccParameter = new ECDomainParameters(paramSpec.getCurve(),paramSpec.getG(),paramSpec.getN());
        m_signer = new ECDSASigner();
        m_shaHash = new CCryptoSHABC();
        m_shaHash.initialize(eShaMode);
    }

    @Override
    public EcdsaSignature sign(byte[] byaMessage, BigInteger nSecretKey) {
        ECKeyParameters privParameters = new ECPrivateKeyParameters(nSecretKey, m_eccParameter);
        m_signer.init(true, privParameters);
        byte[] byaHashedMessage = m_shaHash.digest(byaMessage);
        BigInteger[] sig = m_signer.generateSignature(byaHashedMessage);
        return new EcdsaSignature(sig);
    }

    public boolean verifySignature(byte[] byaMessage,EcdsaSignature sig,IECPointFP pubkey){
        byte[] byaHashedMessage = m_shaHash.digest(byaMessage);
        ECKeyParameters pubParams = new ECPublicKeyParameters((ECPoint.Fp)((IAdapter)pubkey).getObject(),m_eccParameter);
        m_signer.init(false, pubParams);
        return m_signer.verifySignature(byaHashedMessage,sig.getR(),sig.getS());
    }
}
