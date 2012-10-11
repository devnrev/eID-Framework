/*
 * PathSecurityData.java
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

import javax.xml.bind.DatatypeConverter;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 21.06.12
 * Time: 16:05
 */

/**
 * This class represents the PathSecurity type specified in TR-03112-4
 */
public class PathSecurityData {
    public String protocol_;
    public byte[] psk_;
    public byte[] tlsCertificate_;

    public PathSecurityData(String protocol, byte[] psk) {
        this.protocol_ = protocol;
        this.psk_ = psk;
    }

    public PathSecurityData(String protocol, byte[] psk, byte[] tlsCertificate) {
        this.protocol_ = protocol;
        this.psk_ = psk;
        this.tlsCertificate_ = tlsCertificate;
    }

    public String getProtocol() {
        return protocol_;
    }

    public byte[] getPsk() {
        return psk_;
    }

    public byte[] getTlsCertificate() {
        return tlsCertificate_;
    }

    @Override
    public String toString() {
        return "PathSecurityData{" +
                "protocol_='" + protocol_ + '\'' +
                ", psk_=" + DatatypeConverter.printHexBinary(psk_) +
                ", tlsCertificate_=" + tlsCertificate_ +
                '}';
    }
}
