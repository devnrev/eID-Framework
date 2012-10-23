/*
 * CCryptoSHABC.java
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

import common.crypto.CryptoTypes;
import common.crypto.ICryptoSHA;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 16.12.11
 * Time: 09:59
 * To change this template use File | Settings | File Templates.
 */

/**
 * Class which performs SHA checksum calculation using the BouncyCastle crypto provider
 */
public class CCryptoSHABC implements ICryptoSHA{
    private Digest m_digest;

    @Override
    public void initialize(CryptoTypes.ESHAMode eMode) {
        switch (eMode) {
            case SHA1:
                m_digest = new SHA1Digest();
                break;
            case SHA256:
                m_digest = new SHA256Digest();
                break;
            case SHA512:
                m_digest = new SHA512Digest();
        }
    }

    @Override
    public byte[] digest(byte[] byaData) {
        if(m_digest == null)
            throw new NullPointerException("SHA digest not initialized");

        byte[] byaBuff = new byte[m_digest.getDigestSize()];
        m_digest.update(byaData,0,byaData.length);
        m_digest.doFinal(byaBuff,0);
        return byaBuff;

    }
}
