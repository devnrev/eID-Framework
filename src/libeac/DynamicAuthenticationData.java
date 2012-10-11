/*
 * CDynamicAthenticationData.java
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

package libeac;

import common.CFactoryHelper;
import common.crypto.IASN1EncodableVector;
import common.crypto.IDERSequence;
import common.crypto.IDERTaggedObject;
import common.util.Logger;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 13.12.11
 * Time: 23:26
 * To change this template use File | Settings | File Templates.
 */

/*
 * Tags from BSI Worked Example
 *
 * encrypted_nonce                  0x80
 * mapping_data_terminal            0x81
 * mapping_data_chip                0x82
 * terminal_ephemeral_pk            0x83
 * chip_ephemeral_pk                0x84
 * terminal_authentication_token    0x85
 * chip_authentication_token        0x86
 * chip_authority_reference         0x87
 */

/**
 * Helper class which is used during the PACE protocol. En- and decodes data
 * into specific formats interpretable by the client or the card
 */
public class DynamicAuthenticationData {

    private IDERTaggedObject encryptedNonce_;
    private IDERTaggedObject mappingDataTerminal_;
    private IDERTaggedObject mappingDataChip_;
    private IDERTaggedObject terminalEphemeralPK_;
    private IDERTaggedObject chipEphemeralPK_;
    private IDERTaggedObject terminalAuthenticationToken_;
    private IDERTaggedObject chipAuthenticationToken_;
    private IDERTaggedObject certificationAuthorityReference7_;
    private IDERTaggedObject certificationAuthorityReference8_;

    /**
     * Set the terminal mapping data
     * @param byaData Terminal mapping data
     */
    public void setTerminalMapping(byte[] byaData) {
        mappingDataTerminal_ = CFactoryHelper.getCryptoProvider().createDERTaggetObject(
                false, 0x01, CFactoryHelper.getCryptoProvider().createDEROctedString(byaData)
        );
    }

    /**
     * Set the terminal ephemeral public key
     * @param byaData Ephemeral public key
     */
    public void setTerminalEphemeralPK(byte[] byaData) {
        terminalEphemeralPK_ = CFactoryHelper.getCryptoProvider().createDERTaggetObject(
                false, 0x03, CFactoryHelper.getCryptoProvider().createDEROctedString(byaData)
        );
    }

    /**
     * Set the terminal authentication token
     * @param byaData Authentication token
     */
    public void setTerminalAuthToken(byte[] byaData) {
        terminalAuthenticationToken_ = CFactoryHelper.getCryptoProvider().createDERTaggetObject(
                false, 0x05, CFactoryHelper.getCryptoProvider().createDEROctedString(byaData)
        );
    }

    /**
     * Get the encrypted nonce
     * @return Encrypted nonce
     */
    public byte[] getEncryptedNonce() {
        if (encryptedNonce_ == null)
            throw new NullPointerException("Encrypted nonce not set");
        return encryptedNonce_.parseAsOctectString().getOctets();
    }

    /**
     * Get the terminal mapping
     * @return Terminal mapping
     */
    public byte[] getTerminalMapping() {
        if (mappingDataTerminal_ == null)
            throw new NullPointerException("Terminal mapping  not set");
        return mappingDataTerminal_.parseAsOctectString().getOctets();
    }

    /**
     * Get the chip mapping
     * @return Chip mapping
     */
    public byte[] getChipMapping() {
        if (mappingDataChip_ == null)
            throw new NullPointerException("Chip mapping  not set");
        return mappingDataChip_.parseAsOctectString().getOctets();
    }

    /**
     * get the terminal ephemeral public key
     * @return Ephemeral public key
     */
    public byte[] getTerminalEphemeralPK() {
        if (terminalEphemeralPK_ == null)
            throw new NullPointerException("Terminal ephemeral public key not set");
        return terminalEphemeralPK_.parseAsOctectString().getOctets();
    }

    /**
     * Get the chip ephemeral public key
     * @return Ephemeral public key
     */
    public byte[] getChipEphemeralPK() {
        if (chipEphemeralPK_ == null)
            throw new NullPointerException("Chip ephemeral public key not set");
        return chipEphemeralPK_.parseAsOctectString().getOctets();
    }

    /**
     * Get the terminal authentication token
     * @return Authentication token
     */
    public byte[] getTerminalAuthenticationToken() {
        if (terminalAuthenticationToken_ == null)
            throw new NullPointerException("Terminal authentication token not set");
        return terminalAuthenticationToken_.parseAsOctectString().getOctets();
    }

    /**
     * Get the chip authentication token
     * @return Authentication token
     */
    public byte[] getChipAuthenticationToken() {
        if (chipAuthenticationToken_ == null)
            throw new NullPointerException("Chip authentication token not set");
        return chipAuthenticationToken_.parseAsOctectString().getOctets();
    }

    /**
     * Get the certificate authority reference 1
     * @return Certificate Authority Refernce 1
     */
    public byte[] getCertificateAuthorityReferenceToken1() {
        if (certificationAuthorityReference7_ == null)
            throw new NullPointerException("Certificate authority reference 1 not set");
        return certificationAuthorityReference7_.parseAsOctectString().getOctets();
    }

    /**
     * Get the certificate authority reference 2
     * @return Certificate Authority Refernce 2
     */
    public byte[] getCertificateAuthorityReferenceToken2() {
        if (certificationAuthorityReference8_ == null)
            throw new NullPointerException("Certificate authority reference 2 not set");
        return certificationAuthorityReference8_.parseAsOctectString().getOctets();
    }

    /**
     * Initialize the dynamic authentication data
     * @param byaData Data
     * @return true if successful
     */
    public boolean initialize(byte[] byaData) {
        IDERSequence derSeq;
        try {
            derSeq = CFactoryHelper.getCryptoProvider().createDERApplicationSpecific(byaData).getDERSequence();
        } catch (IOException e) {
            Logger.log("Error reading bytes int DERSequence:" + e.toString());
            return false;
        }

        IDERTaggedObject tagObj;
        int nSize = derSeq.size();
        for (int i = 0; i < nSize; i++) {
            tagObj = derSeq.getDERTaggedObjectAt(i);
            switch (tagObj.getTagNo()) {
                case 0:
                    encryptedNonce_ = tagObj;
                    break;
                case 1:
                    mappingDataTerminal_ = tagObj;
                    break;
                case 2:
                    mappingDataChip_ = tagObj;
                    break;
                case 3:
                    terminalEphemeralPK_ = tagObj;
                    break;
                case 4:
                    chipEphemeralPK_ = tagObj;
                    break;
                case 5:
                    terminalAuthenticationToken_ = tagObj;
                    break;
                case 6:
                    chipAuthenticationToken_ = tagObj;
                    break;
                case 7:
                    certificationAuthorityReference7_ = tagObj;
                    break;
                case 8:
                    certificationAuthorityReference8_ = tagObj;
                    break;
                default:
                    throw new NoSuchFieldError("Dynamic authentication data DER tag not in the list");

            }

        }
        return true;
    }

    /* TerminalData which will be sent to the card
     * Special tag used by the eid card is 0x1C
     *
     */

    /**
     * Get the encoded terminal authentication data
     * @return Encoded data
     */
    public byte[] getDERTerminalAuthenticationData() {
        IASN1EncodableVector vec = CFactoryHelper.getCryptoProvider().createASN1EncodableVector();
        if (mappingDataTerminal_ != null)
            vec.add(mappingDataTerminal_);
        if (terminalEphemeralPK_ != null)
            vec.add(terminalEphemeralPK_);
        if (terminalAuthenticationToken_ != null)
            vec.add(terminalAuthenticationToken_);
        return CFactoryHelper.getCryptoProvider().createDERApplicationSpecific(0x1C, vec).getDEREncoded();
    }

    /**
     * Reset all values
     */
    public void reset() {
        chipAuthenticationToken_ = null;
        certificationAuthorityReference7_ = null;
        chipEphemeralPK_ = null;
        encryptedNonce_ = null;
        mappingDataChip_ = null;
        terminalEphemeralPK_ = null;
        terminalAuthenticationToken_ = null;
        mappingDataTerminal_ = null;
    }

}
