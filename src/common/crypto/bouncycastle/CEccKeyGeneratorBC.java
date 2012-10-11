/*
 * CEccKeyGeneratorBC.java
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
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.*;


/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 08.01.12
 * Time: 17:25
 * To change this template use File | Settings | File Templates.
 */
public class CEccKeyGeneratorBC implements IEccKeyGenerator{
    private ECKeyPairGenerator m_keyGen;

   
    @Override
    public void initialize(IX9ECParameters curveSpec) throws Exception{

        ECDomainParameters domainParams = new ECDomainParameters((ECCurve.Fp)((IAdapter)curveSpec.getCurveFP()).getObject(),
                (ECPoint.Fp)((IAdapter)curveSpec.getG()).getObject(),
                curveSpec.getN());
        m_keyGen = null;
        m_keyGen = new ECKeyPairGenerator();
        m_keyGen.init(new ECKeyGenerationParameters(domainParams, new SecureRandom()));



    }

    @Override
    public EccKeyPair generate() {
        AsymmetricCipherKeyPair pair = m_keyGen.generateKeyPair();
        IECPointFP publicKey = new CECPointFPBC((ECPoint.Fp)((ECPublicKeyParameters)pair.getPublic()).getQ());
        BigInteger secretKey = ((ECPrivateKeyParameters)pair.getPrivate()).getD();
        return new EccKeyPair(publicKey,secretKey);

    }
}
