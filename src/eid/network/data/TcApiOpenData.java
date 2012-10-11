/*
 * TcApiOpenData.java
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
 * Date: 21.06.12
 * Time: 16:01
 */

/**
 * This class represents the Tc_Api_Open data specified in TR-03112-7
 */
public class TcApiOpenData {
    public String protocolTerminationPoint_;
    public String sessionIdentifier_;
    public String binding_;
    public PathSecurityData pathSecurity_;

    public TcApiOpenData(String protocolTerminationPoint, String sessionIdentifier, String binding, PathSecurityData pathSecurity) {
        this.protocolTerminationPoint_ = protocolTerminationPoint;
        this.sessionIdentifier_ = sessionIdentifier;
        this.binding_ = binding;
        this.pathSecurity_ = pathSecurity;
    }

    public String getProtocolTerminationPoint() {
        return protocolTerminationPoint_;
    }

    public String getSessionIdentifier() {
        return sessionIdentifier_;
    }

    public String getBinding() {
        return binding_;
    }

    public PathSecurityData getPathSecurity() {
        return pathSecurity_;
    }
}


