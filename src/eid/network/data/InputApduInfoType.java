/*
 * InputApduInfoType.java
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
 * Date: 26.07.12
 * Time: 11:30
 */

/**
 * This class represents the InputApduInfoType specified in TR-03112-6
 */
public class InputApduInfoType {
    private byte[] apdu_;
    private List<byte[]> acceptableStatusCodes_;

    public InputApduInfoType(byte[] apdu) {
        this.apdu_ = apdu;
    }

    public InputApduInfoType(byte[] apdu, List<byte[]> acceptableStatusCodes) {
        this.apdu_ = apdu;
        this.acceptableStatusCodes_ = acceptableStatusCodes;
    }

    public byte[] getApdu() {
        return apdu_;
    }

    public List<byte[]> getAcceptableStatusCodes() {
        return acceptableStatusCodes_;
    }

    public boolean acceptsAllStatusCodes(){
        return acceptableStatusCodes_ == null;
    }
}
