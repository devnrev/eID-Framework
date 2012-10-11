/*
 * EAC1OutputType.java
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
 * Date: 20.07.12
 * Time: 18:03
 */

/**
 * This class represents the EAC1OutputType specified in TR-03112-7
 */
public class EAC1OutputType implements AuthenticationProtocolDataType {

    private int retryCounter_;
    private byte[] chat_;
    private List<byte[]> car_;
    private byte[] efCardAccess_;
    private byte[] idPICC_;
    private byte[] challenge_;

    public EAC1OutputType() {

    }

    public int getRetryCounter() {
        return retryCounter_;
    }

    public void setRetryCounter(int retryCounter) {
        this.retryCounter_ = retryCounter;
    }

    public byte[] getChat() {
        return chat_;
    }

    public void setChat(byte[] chat) {
        this.chat_ = chat;
    }

    public List<byte[]> getCarList() {
        return car_;
    }

    public void setCar(List<byte[]> car) {
        this.car_ = car;
    }

    public byte[] getEfCardAccess() {
        return efCardAccess_;
    }

    public void setEfCardAccess(byte[] efCardAccess) {
        this.efCardAccess_ = efCardAccess;
    }

    public byte[] getIdPICC() {
        return idPICC_;
    }

    public void setIdPICC(byte[] idPICC) {
        this.idPICC_ = idPICC;
    }

    public byte[] getChallenge() {
        return challenge_;
    }

    public void setChallenge(byte[] challenge) {
        this.challenge_ = challenge;
    }
}
