/*
 * ProtocolRequestType.java
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
 * Date: 26.07.12
 * Time: 15:29
 */

/**
 * Class which ensures type safety for request data types used in the generic message processing units
 * Additionally stores the name of the type to allow the matching of XML Data against target container classes
 */
public class ProtocolRequestType {
    private String requestType_;

    public ProtocolRequestType(String requestType) {
        this.requestType_ = requestType;
    }

    public String getRequestType() {
        return requestType_;
    }
}
