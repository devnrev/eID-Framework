/*
 * TransmissionData.java
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

package common.network.http;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 30.05.12
 * Time: 09:21
 */
public class TransmissionData {
    private char[] data_;
    private int length_;

    public TransmissionData(char[] data,int length){
        data_ = new char[length];
        System.arraycopy(data,0,data_,0,length);
        length_ = length;
    }

    public char[] getData(){
        return data_;
    }

    public int getLength(){
        return length_;
    }

    @Override
    public String toString() {
        return new String(data_,0,length_);
    }
}
