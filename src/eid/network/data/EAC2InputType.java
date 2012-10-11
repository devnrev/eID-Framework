/*
 * EAC2InputType.java
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

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 22.07.12
 * Time: 00:33
 */

/**
 * This class represents the EAC2InputType specified in TR-03112-7
 */
public class EAC2InputType implements AuthenticationProtocolDataType {

    private List<byte[]> certificateList_;
    private byte[] ephemeralPublicKey_;

    public EAC2InputType(List<byte[]> certificateList, byte[] ephemeralPublicKey) {
        this.certificateList_ = certificateList;
        this.ephemeralPublicKey_ = ephemeralPublicKey;
    }

    public List<byte[]> getCertificateList() {
        return certificateList_;
    }

    public byte[] getEphemeralPublicKey() {
        return ephemeralPublicKey_;
    }
}
