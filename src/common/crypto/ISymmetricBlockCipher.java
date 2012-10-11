/*
 * ISymmetricBlockCipher.java
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

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 15.12.11
 * Time: 11:40
 * To change this template use File | Settings | File Templates.
 */
public abstract interface ISymmetricBlockCipher {
    public void initialize(byte[] byaIV, byte[] byaKey,CryptoTypes.EBlockMode eMode,CryptoTypes.EKeyLength eKeyLen);

    public abstract byte[] encrypt(byte[] byaData);
    public abstract byte[] decrypt(byte[] byaData);
    
    public int getBlockSize();


}
