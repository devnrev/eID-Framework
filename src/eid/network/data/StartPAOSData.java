/*
 * StartPAOSData.java
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
 * Date: 29.06.12
 * Time: 15:45
 */

/**
 * This class represents the StartPAOS data specified in TR-03112-7
 */
public class StartPAOSData {
    private String sessionIdentifier_;
    private ConnectionHandleType connectionHandle_;
    private UserAgentType userAgent_;
    private List<VersionType> supportedApiVersions_;
    private List<String> supportedDIDProtocols_;

    public StartPAOSData(String sessionIdentifier, ConnectionHandleType connectionHandle) {
        this.sessionIdentifier_ = sessionIdentifier;
        this.connectionHandle_ = connectionHandle;
    }

    public StartPAOSData(String sessionIdentifier,
                         ConnectionHandleType connectionHandle,
                         UserAgentType userAgent,
                         List<VersionType> supportedApiVersions,
                         List<String> supportedDIDProtocols) {
        this.sessionIdentifier_ = sessionIdentifier;
        this.connectionHandle_ = connectionHandle;
        this.userAgent_ = userAgent;
        this.supportedApiVersions_ = supportedApiVersions;
        this.supportedDIDProtocols_ = supportedDIDProtocols;
    }

    public void setUserAgent_(UserAgentType userAgent) {
        this.userAgent_ = userAgent;
    }

    public void setSupportedApiVersions_(List<VersionType> supportedApiVersions) {
        this.supportedApiVersions_ = supportedApiVersions;
    }

    public void setSupportedDIDProtocols_(List<String> supportedDIDProtocols) {
        this.supportedDIDProtocols_ = supportedDIDProtocols;
    }

    public String getSessionIdentifier() {
        return sessionIdentifier_;
    }

    public ConnectionHandleType getConnectionHandle() {
        return connectionHandle_;
    }

    public UserAgentType getUserAgent() {
        return userAgent_;
    }

    public List<VersionType> getSupportedApiVersions() {
        return supportedApiVersions_;
    }

    public List<String> getSupportedDIDProtocols() {
        return supportedDIDProtocols_;
    }
}
