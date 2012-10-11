/*
 * DIDAuthenticate.java
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
 * Date: 20.07.12
 * Time: 18:13
 */

/**
 * This class describes the DIDAuthenticate message in a generic way, since the DIDAuthenticate message itself stays
 * the same, only the content has a different format
 * @param <T> Data type of the contained content
 */
public class DIDAuthenticateType<T extends AuthenticationProtocolDataType> {
    private ConnectionHandleType connectionHandle_;
    private String didName_;
    private T authProtocolData_;

    public DIDAuthenticateType(ConnectionHandleType connectionHandle,
                               String didName,
                               T authProtocolData) {
        this.connectionHandle_ = connectionHandle;
        this.didName_ = didName;
        this.authProtocolData_ = authProtocolData;
    }

    public ConnectionHandleType getConnectionHandle() {
        return connectionHandle_;
    }

    public String getDidName() {
        return didName_;
    }

    public T getAuthProtocolData() {
        return authProtocolData_;
    }

    @Override
    public String toString() {
        return "DIDAuthenticateType{" +
                "connectionHandle_=" + connectionHandle_ +
                ", didName_='" + didName_ + '\'' +
                ", authProtocolData_=" + authProtocolData_ +
                '}';
    }
}
