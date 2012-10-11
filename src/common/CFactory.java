/*
 * CFactory.java
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

package common;


import common.cardio.ECardAccessor;
import common.cardio.ICardAccess;
import common.crypto.ECryptoProvider;
import common.crypto.ICryptoProvider;
import common.crypto.bouncycastle.CBouncyCryptoProvider;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 12.12.11
 * Time: 12:10
 * To change this template use File | Settings | File Templates.
 */
public class CFactory {
    private static CFactory ourInstance = new CFactory();

    public static CFactory getInstance() {
        return ourInstance;
    }

    private ECryptoProvider m_eCryptoProvider;
    private ICryptoProvider m_cryptoProvider;

    private ECardAccessor m_eCardAccessor;
    private ICardAccess     m_cardAccess;



    private CFactory() {
        m_cryptoProvider = null;
        m_cardAccess = null;
        m_eCryptoProvider = ECryptoProvider.BouncyCastle;
        m_eCardAccessor = ECardAccessor.SmartIO;
    }


    public ICryptoProvider getCryptoProvider() {
        if (m_cryptoProvider == null) {
            switch (m_eCryptoProvider) {
                case BouncyCastle:
                    m_cryptoProvider = new CBouncyCryptoProvider();
                    break;

            }
        }
        return m_cryptoProvider;
    }

}
