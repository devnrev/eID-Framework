/*
 * CCryptoCMacBC.java
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
import common.crypto.ISymmetricMAC;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.engines.AESLightEngine;
import org.bouncycastle.crypto.macs.CMac;
import org.bouncycastle.crypto.params.KeyParameter;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 20.12.11
 * Time: 10:08
 * To change this template use File | Settings | File Templates.
 */
public class CCryptoCMacBC implements ISymmetricMAC {
    private CMac m_cmac;
    private int m_nKeyLen;
    
    @Override
    public void initialize(CryptoTypes.EKeyLength eLen) {
        m_nKeyLen= CryptoTypes.getKeyLen(eLen);
        BlockCipher bc = new AESLightEngine();
        m_cmac = new CMac(bc,m_nKeyLen);

    }

    @Override
    public byte[] calculate(byte[] byaKey, byte[] byaData) {
        KeyParameter kp = new KeyParameter(byaKey);
        byte[] byaBuff=new byte[m_nKeyLen/8];
        m_cmac.init(kp);
        m_cmac.update(byaData,0,byaData.length);
        m_cmac.doFinal(byaBuff,0);
        return byaBuff;
    }
}
