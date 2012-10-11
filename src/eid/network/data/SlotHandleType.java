/*
 * SlotHandleType.java
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
 * Time: 10:57
 */

/**
 * SlotHandleType as described in TR-03112-4
 */
public class SlotHandleType {
    private String slotHandle_;
    private String recognitionInfo_;

    public SlotHandleType(String slotHandle) {
        this.slotHandle_ = slotHandle;
    }

    public SlotHandleType(String slotHandle, String recognitionInfo) {
        this.slotHandle_ = slotHandle;
        this.recognitionInfo_ = recognitionInfo;
    }

    public SlotHandleType() {
    }

    public String getSlotHandle() {
        return slotHandle_;
    }

    public void setSlotHandle(String slotHandle) {
        this.slotHandle_ = slotHandle;
    }

    public String getRecognitionInfo() {
        return recognitionInfo_;
    }

    public void setRecognitionInfo(String recognitionInfo) {
        this.recognitionInfo_ = recognitionInfo;
    }

    @Override
    public String toString() {
        return "SlotHandleType{" +
                "slotHandle_='" + slotHandle_ + '\'' +
                ", recognitionInfo_='" + recognitionInfo_ + '\'' +
                '}';
    }
}
