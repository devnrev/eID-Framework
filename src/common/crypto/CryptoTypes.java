/*
 * CryptoTypes.java
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
 * Date: 15.12.11
 * Time: 12:09
 * To change this template use File | Settings | File Templates.
 */
public class CryptoTypes {
    public static enum EBlockMode{
        CBC
    }

    public static enum EKeyLength{
        LEN_128,
        LEN_256
    }

    public static enum ESHAMode{
        SHA1,
        SHA256,
        SHA512
    }

    public static int getKeyLen(EKeyLength eKey) {
        switch (eKey){
            case LEN_128:
                return 128;
            case LEN_256:
                return 256;
        }
        throw new NoSuchFieldError("Keylen not found");
    }

    public static enum ESymmetricBlockCipher{
        AES
    }

    public static enum ESymmetricMAC{
        CMAC
    }

}
