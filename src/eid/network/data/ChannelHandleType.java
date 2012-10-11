/*
 * ChannelHandleType.java
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
 * Time: 10:53
 */

/**
 * Channel handle class as described in TR-03112-4
 */
public class ChannelHandleType {
    private String channelHandle_;
    private String contextHandle_;
    private String ifdName_;
    private int slotIndex_;
    private String cardApplication_;

    public ChannelHandleType(String channelHandle,
                             String contextHandle,
                             String ifdName,
                             int slotIndex,
                             String cardApplication) {
        this.channelHandle_ = channelHandle;
        this.contextHandle_ = contextHandle;
        this.ifdName_ = ifdName;
        this.slotIndex_ = slotIndex;
        this.cardApplication_ = cardApplication;
    }

    public ChannelHandleType() {

    }

    public String getChannelHandle() {
        return channelHandle_;
    }

    public void setChannelHandle(String channelHandle) {
        this.channelHandle_ = channelHandle;
    }

    public String getContextHandle() {
        return contextHandle_;
    }

    public void setContextHandle(String contextHandle) {
        this.contextHandle_ = contextHandle;
    }

    public String getIfdName_() {
        return ifdName_;
    }

    public void setIfdName(String ifdName) {
        this.ifdName_ = ifdName;
    }

    public int getSlotIndex() {
        return slotIndex_;
    }

    public void setSlotIndex(int slotIndex) {
        this.slotIndex_ = slotIndex;
    }

    public String getCardApplication() {
        return cardApplication_;
    }

    public void setCardApplication(String cardApplication) {
        this.cardApplication_ = cardApplication;
    }

    @Override
    public String toString() {
        return "ChannelHandleType{" +
                "channelHandle_='" + channelHandle_ + '\'' +
                ", contextHandle_='" + contextHandle_ + '\'' +
                ", ifdName_='" + ifdName_ + '\'' +
                ", slotIndex_=" + slotIndex_ +
                ", cardApplication_='" + cardApplication_ + '\'' +
                '}';
    }
}
