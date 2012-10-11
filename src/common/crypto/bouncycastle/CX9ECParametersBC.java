/*
 * CX9ECParametersBC.java
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

import common.CAdapterObj;
import common.crypto.IECCurveFP;
import common.crypto.IECPoint;
import common.crypto.IX9ECParameters;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 19.12.11
 * Time: 16:44
 * To change this template use File | Settings | File Templates.
 */
public class CX9ECParametersBC extends CAdapterObj<X9ECParameters> implements IX9ECParameters{

    public CX9ECParametersBC(X9ECParameters obj){
        m_adaptedObject = obj;
    }

    @Override
    public IECPoint getG() {
        return new CECPointFPBC((ECPoint.Fp)m_adaptedObject.getG());
    }

    @Override
    public BigInteger getN() {
        return m_adaptedObject.getN();
    }

    @Override
    public IECCurveFP getCurveFP() {
        return new CECCurveFPBC((ECCurve.Fp)m_adaptedObject.getCurve());
    }
}
