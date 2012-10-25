/*
 * TlsPskInfo.java
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

package common.network.transport;

import org.bouncycastle.crypto.tls.TlsPSKIdentity;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 20.06.12
 * Time: 16:45
 */

/**
 * Value class containing information about the TLS pre shared key and it's identity
 */
public class TlsPskInfo implements TlsPSKIdentity {
    private byte[] psk_;
    private byte[] pskIdentity_;

    public TlsPskInfo(byte[] pskIdentity,byte[] psk){
        psk_ = psk;
        pskIdentity_ = pskIdentity;
    }


    @Override
    public void skipIdentityHint() {
        //not needed
    }

    @Override
    public void notifyIdentityHint(byte[] bytes) {
        //not needed
    }

    @Override
    public byte[] getPSKIdentity() {
        return pskIdentity_;
    }

    @Override
    public byte[] getPSK() {
        return psk_;
    }

}
