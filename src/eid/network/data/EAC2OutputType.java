/*
 * EAC2OutputType.java
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

package eid.network.data;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 23.07.12
 * Time: 11:52
 */

/**
 * This class represents the EAC2OutputType specified in TR-03112-7
 */
public class EAC2OutputType implements AuthenticationProtocolDataType {

    private boolean signatureNeeded_;
    private byte[] challenge_;
    private byte[] efCardSecurity_;
    private byte[] authenticationToken_;
    private byte[] nonce_;

    public EAC2OutputType(byte[] challenge) {
        challenge_ = challenge;
        signatureNeeded_ = true;
    }

    public EAC2OutputType(byte[] efCardSecurity, byte[] authenticationToken, byte[] nonce) {
        this.efCardSecurity_ = efCardSecurity;
        this.authenticationToken_ = authenticationToken;
        this.nonce_ = nonce;
        signatureNeeded_ = false;
    }

    public byte[] getChallenge() {
        return challenge_;
    }

    public boolean isSignatureNeeded() {
        return signatureNeeded_;
    }

    public byte[] getEfCardSecurity() {
        return efCardSecurity_;
    }

    public byte[] getAuthenticationToken() {
        return authenticationToken_;
    }

    public byte[] getNonce() {
        return nonce_;
    }
}
