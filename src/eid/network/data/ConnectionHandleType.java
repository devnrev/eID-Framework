/*
 * ConenctionHandleType.java
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
 * Time: 15:47
 */

/**
 * Connection handle class as described in TR-03112-4
 */
public class ConnectionHandleType {
    private ChannelHandleType channelHandle_;
    private SlotHandleType slotHandle_;

    public ConnectionHandleType(ChannelHandleType channelHandle, SlotHandleType slotHandle) {
        this.channelHandle_ = channelHandle;
        this.slotHandle_ = slotHandle;
    }

    public ChannelHandleType getChannelHandle(){
        return channelHandle_;
    }

    public SlotHandleType getSlotHandle(){
        return slotHandle_;
    }

    @Override
    public String toString() {
        return "ConnectionHandleType{" +
                "channelHandle_=" + channelHandle_ +
                ", slotHandle_=" + slotHandle_ +
                '}';
    }
}
