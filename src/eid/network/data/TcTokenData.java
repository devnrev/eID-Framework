/*
 * TcTokenData.java
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
 * Date: 03.07.12
 * Time: 11:05
 */

/**
 * This class represents the TcToken data specified in TR-03112-7
 */
public class TcTokenData {
    private String serverAddress_;
    private String sessionIdentifier_;
    private String refreshAddress_;
    private String binding_;
    private PathSecurityData pathSecurityData_;

    public TcTokenData(String serverAddress,
                       String sessionIdentifier,
                       String refreshAddress,
                       String binding,
                       PathSecurityData pathSecurityData) {
        this.serverAddress_ = serverAddress;
        this.sessionIdentifier_ = sessionIdentifier;
        this.refreshAddress_ = refreshAddress;
        this.binding_ = binding;
        this.pathSecurityData_ = pathSecurityData;
    }

    public TcTokenData(String serverAddress,
                       String sessionIdentifier,
                       String refreshAddress,
                       String binding) {
        this.serverAddress_ = serverAddress;
        this.sessionIdentifier_ = sessionIdentifier;
        this.refreshAddress_ = refreshAddress;
        this.binding_ = binding;
    }

    public String getServerAddress() {
        return serverAddress_;
    }

    public String getSessionIdentifier() {
        return sessionIdentifier_;
    }

    public String getRefreshAddress() {
        return refreshAddress_;
    }

    public String getBinding() {
        return binding_;
    }

    public PathSecurityData getPathSecurityData() {
        return pathSecurityData_;
    }

    @Override
    public String toString() {
        return "TcTokenData{" +
                "serverAddress_='" + serverAddress_ + '\'' +
                ", sessionIdentifier_='" + sessionIdentifier_ + '\'' +
                ", refreshAddress_='" + refreshAddress_ + '\'' +
                ", binding_='" + binding_ + '\'' +
                ", pathSecurityData_=" + pathSecurityData_ +
                '}';
    }
}
