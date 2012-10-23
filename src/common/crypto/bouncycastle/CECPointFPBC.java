/*
 * CECPointFPBC.java
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
import common.crypto.IECCurve;
import common.crypto.IECFieldElement;
import common.crypto.IECPoint;
import common.crypto.IECPointFP;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 19.12.11
 * Time: 15:21
 * To change this template use File | Settings | File Templates.
 */

/**
 * BouncyCastle adapter for IECPointFP
 */
public class CECPointFPBC extends CECPointBC implements IECPointFP{
    public CECPointFPBC(ECPoint.Fp point){
        super(point);
    }

    public CECPointFPBC(IECPoint point){
        super((ECPoint.Fp)((IAdapter)point).getObject());
    }

    public CECPointFPBC(IECCurve curve, IECFieldElement x,IECFieldElement y){
        m_adaptedObject = new ECPoint.Fp((ECCurve)((IAdapter)curve).getObject(),(ECFieldElement)((IAdapter)x).getObject(),(ECFieldElement)((IAdapter)y).getObject());
    }

    @Override
    public IECPointFP multiply(BigInteger nVal) {
       return new CECPointFPBC((ECPoint.Fp)m_adaptedObject.multiply(nVal));

    }

    @Override
    public IECPointFP add(IECPoint point) {
        return new CECPointFPBC((ECPoint.Fp)(m_adaptedObject.add((ECPoint)((IAdapter)point).getObject())));
    }


}
