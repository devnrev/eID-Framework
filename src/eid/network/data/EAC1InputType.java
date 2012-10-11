/*
 * EAC1InputType.java
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
 * Time: 17:53
 */

/**
 * This class represents the EAC1InputType specified in TR-03112-7
 */
public class EAC1InputType implements AuthenticationProtocolDataType {

    private List<byte[]> certificateList_;
    private List<byte[]> certificateDescriptionList_;
    private byte[] providerInfo_;
    private byte[] requiredCHAT_;
    private byte[] optionalCHAT_;
    private byte[] authenticatedAuxiliaryData_;
    private byte[] transactionInfo_;

    public EAC1InputType(List<byte[]> certificateList) {
        this.certificateList_ = certificateList;
    }

    public EAC1InputType(List<byte[]> certificateList,
                         List<byte[]> certificateDescriptionList,
                         byte[] requiredCHAT,
                         byte[] optionalCHAT,
                         byte[] authenticatedAuxiliaryData) {

        this.certificateList_ = certificateList;
        this.certificateDescriptionList_ = certificateDescriptionList;
        this.requiredCHAT_ = requiredCHAT;
        this.optionalCHAT_ = optionalCHAT;
        this.authenticatedAuxiliaryData_ = authenticatedAuxiliaryData;
    }

    public List<byte[]> getCertificateList() {
        return certificateList_;
    }

    public void setCertificateList(List<byte[]> certificateList) {
        this.certificateList_ = certificateList;
    }

    public List<byte[]> getCertificateDescriptionList() {
        return certificateDescriptionList_;
    }

    public void setCertificateDescriptionList(List<byte[]> certificateDescriptionList) {
        this.certificateDescriptionList_ = certificateDescriptionList;
    }

    public byte[] getProviderInfo() {
        return providerInfo_;
    }

    public void setProviderInfo(byte[] providerInfo) {
        this.providerInfo_ = providerInfo;
    }

    public byte[] getRequiredCHAT() {
        return requiredCHAT_;
    }

    public void setRequiredCHAT(byte[] requiredCHAT) {
        this.requiredCHAT_ = requiredCHAT;
    }

    public byte[] getOptionalCHAT() {
        return optionalCHAT_;
    }

    public void setOptionalCHAT(byte[] optionalCHAT) {
        this.optionalCHAT_ = optionalCHAT;
    }

    public byte[] getAuthenticatedAuxiliaryData() {
        return authenticatedAuxiliaryData_;
    }

    public void setAuthenticatedAuxiliaryData(byte[] authenticatedAuxiliaryData) {
        this.authenticatedAuxiliaryData_ = authenticatedAuxiliaryData;
    }

    public byte[] getTransactionInfo() {
        return transactionInfo_;
    }

    public void setTransactionInfo(byte[] transactionInfo) {
        this.transactionInfo_ = transactionInfo;
    }

    @Override
    public String toString() {
        return "EAC1InputType{" +
                "certificateList_=" + certificateList_ +
                ", certificateDescriptionList_=" + certificateDescriptionList_ +
                ", providerInfo_=" + providerInfo_ +
                ", requiredCHAT_=" + requiredCHAT_ +
                ", optionalCHAT_=" + optionalCHAT_ +
                ", authenticatedAuxiliaryData_=" + authenticatedAuxiliaryData_ +
                ", transactionInfo_=" + transactionInfo_ +
                '}';
    }
}
