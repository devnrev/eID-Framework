/*
 * EccKeyPair.java
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

import java.math.BigInteger;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 08.01.12
 * Time: 18:55
 * To change this template use File | Settings | File Templates.
 */

/**
 * Value class representing a ECC key pair
  */
public class EccKeyPair{
    private IECPointFP m_publicKey;
    private BigInteger m_secretKey;

    public EccKeyPair(IECPointFP pubKey,BigInteger secKey){
        m_publicKey = pubKey;
        m_secretKey = secKey;
    }

    public IECPointFP getPublicKey(){
        return m_publicKey;
    }

    public BigInteger getSecretKey(){
        return  m_secretKey;
    }
}