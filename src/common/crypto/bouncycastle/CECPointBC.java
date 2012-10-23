/*
 * CECPointBC.java
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
import common.IAdapter;
import common.crypto.IECPoint;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 19.12.11
 * Time: 14:42
 * To change this template use File | Settings | File Templates.
 */

/**
 * BouncyCastle adapter for IECPoint
 */
public class CECPointBC extends CAdapterObj<ECPoint> implements IECPoint{

    public CECPointBC(){

    }

    public CECPointBC(ECPoint obj){
        m_adaptedObject = obj;
    }

    @Override
    public IECPoint multiply(BigInteger nVal) {
        return new CECPointFPBC((ECPoint.Fp)m_adaptedObject.multiply(nVal));
    }

    @Override
    public IECPoint add(IECPoint point) {
        return new CECPointFPBC((ECPoint.Fp)m_adaptedObject.add((ECPoint.Fp)((IAdapter)point).getObject()));
    }

    @Override
    public BigInteger getX() {
        return m_adaptedObject.getX().toBigInteger();
    }

    @Override
    public BigInteger getY() {
        return m_adaptedObject.getY().toBigInteger();
    }

    @Override
    public byte[] getEncoded() {
        return m_adaptedObject.getEncoded();
    }
}
