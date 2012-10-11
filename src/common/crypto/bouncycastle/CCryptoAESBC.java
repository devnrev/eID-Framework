/*
 * CCryptoAESBC.java
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
import common.crypto.ISymmetricBlockCipher;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.engines.AESLightEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 15.12.11
 * Time: 11:55
 * To change this template use File | Settings | File Templates.
 */
public class CCryptoAESBC implements ISymmetricBlockCipher {
    private BlockCipher m_blockCipher;
    private ParametersWithIV m_keyParam;
    private int m_nBlockSize;
    @Override
    public void initialize(byte[] byaIV, byte[] byaKey, CryptoTypes.EBlockMode eMode,CryptoTypes.EKeyLength eKeyLen) {
        switch (eMode){
            case CBC:
                m_blockCipher = new CBCBlockCipher(new AESLightEngine());
                break;
        }
        m_keyParam = new ParametersWithIV(new KeyParameter(byaKey),byaIV);
        m_nBlockSize = m_blockCipher.getBlockSize();
    }

    protected byte[] processBytes(boolean bEncrypt, byte[] byaData){
        if(m_blockCipher==null)
            throw new NullPointerException("AES not initialized");
        m_blockCipher.init(bEncrypt,m_keyParam);

        int nNumBlocks = (byaData.length / m_nBlockSize);
        byte[] byaCipher = new byte[nNumBlocks * m_nBlockSize];
        for (int i = 0; i < byaCipher.length; i += m_nBlockSize) {
            m_blockCipher.processBlock(byaData, i, byaCipher, i);
        }
        return byaCipher;
    }
    
    @Override
    public byte[] encrypt(byte[] byaData) {
       return processBytes(true,byaData);
    }

    @Override
    public byte[] decrypt(byte[] byaData) {
        return processBytes(false,byaData);
    }

    @Override
    public int getBlockSize() {
        if(m_blockCipher==null)
            throw new NullPointerException("Block cipher not initialized!");
        return m_nBlockSize;
    }
}
