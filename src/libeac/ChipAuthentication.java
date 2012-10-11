/*
 * ChipAuthentication.java
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
import common.cardio.CAPDUResponse;
import common.cardio.IAPDUMessageCoding;
import common.cardio.ICardAccess;
import common.cardio.coding.CAPDUNormalMessaging;
import common.cardio.coding.CAPDUSecureMessaging;
import common.crypto.*;
import common.exceptions.CommunicationException;
import common.util.Logger;
import eid.network.data.EAC2OutputType;
import libeac.cardio.CMSEATAPDUCommand;
import libeac.cardio.GeneralAuthenticate2APDUCommand;
import libeac.definitions.PACETypes;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 25.07.12
 * Time: 16:02
 */

/**
 * This class executes the chip authentication
 */
public class ChipAuthentication {

    /**
     * This class contains the result data of the chip authentication
     */
    public class Result {
        byte[] t_picc_;
        byte[] r_picc_;

        /**
         * Constructor
         * @param t_picc Authentication token
         * @param r_picc Nonce
         */
        Result(byte[] t_picc, byte[] r_picc) {
            this.t_picc_ = t_picc;
            this.r_picc_ = r_picc;
        }

        /**
         * Get the authentication token
         * @return Authentication token
         */
        public byte[] getT_picc_() {
            return t_picc_;
        }

        /**
         * Get the nonce
         * @return Nonce
         */
        public byte[] getR_picc_() {
            return r_picc_;
        }
    }

    private ICardAccess cardAccess_;
    private PACESecurityData secData_;
    private IAPDUMessageCoding smCoding_;
    private EAC2OutputType outptputData_;
    private Result result_;

    /**
     * Constructor
     * @param cardAccess Card accessor
     * @param secData PACE security data
     */
    public ChipAuthentication(ICardAccess cardAccess, PACESecurityData secData) {
        cardAccess_ = cardAccess;
        if(secData != null){
            secData_ = secData;
            smCoding_ = new CAPDUSecureMessaging(secData_.KEnc, secData.KMac, CryptoTypes.ESymmetricBlockCipher.AES);
        }else{
            smCoding_ = new CAPDUNormalMessaging();
        }
    }

    /**
     * Perform the actual chip authentication
     * @param oid Protocol identifier
     * @param keyRef Key reference
     * @param ephemeralPublicKey Ephemeral public key
     * @return true if successful
     */
    public boolean performCA(IDERObjectIdentifier oid, byte keyRef, byte[] ephemeralPublicKey) {
        Logger.log("Set mse at for chip authentication");
        CAPDUResponse resp = null;
        try {
            resp = setMseAt(oid, keyRef);
            if (resp.getSW() != 0x9000) {
                Logger.log("Error with mse at in chip authentication\n" + resp.toString());
                return false;
            }
            Logger.log("send ephemeral public key to the card");
            resp = sendKey(ephemeralPublicKey);
            if (resp.getSW() != 0x9000) {
                Logger.log("Error with mse at in chip authentication\n" + resp.toString());
                return false;
            }
        } catch (CommunicationException e) {
            e.printStackTrace();
            return false;
        }

        Logger.log("got repsonse from card: " + DatatypeConverter.printHexBinary(resp.getData()));
        if (!parseReturnValues(resp.getData())) {
            return false;
        }

        Logger.log("Chip authentication successful");
        return true;
    }

    /**
     * Read the authentication token and the nonce from the received card data
     * @param data Data received from the card
     * @return true if successful
     */
    private boolean parseReturnValues(byte[] data) {
        byte[] t_picc;
        byte[] r_picc;
        try {
            IDERApplicationSpecific appSpec = CFactoryHelper.getCryptoProvider().createDERApplicationSpecific(data);
            r_picc = appSpec.getDERSequence().getDERTaggedObjectAt(0).parseAsOctectString().getOctets();
            t_picc = appSpec.getDERSequence().getDERTaggedObjectAt(1).parseAsOctectString().getOctets();
        } catch (IOException e) {
            return false;
        }
        result_ = new Result(t_picc, r_picc);


        return true;
    }

    /**
     * Initialize the chip authentication
     * @param oid Protocol identifier
     * @param keyRef Key reference
     * @return Card response
     * @throws CommunicationException
     */
    private CAPDUResponse setMseAt(IDERObjectIdentifier oid, byte keyRef) throws CommunicationException {
        CMSEATAPDUCommand mseAtCmd = new CMSEATAPDUCommand(PACETypes.AuthenticationType.CA);
        mseAtCmd.setProtocol(oid);
        mseAtCmd.setPrivateKeyReference(keyRef);
        mseAtCmd.setMessageCoding(smCoding_);
        return cardAccess_.sendAPDUCommand(mseAtCmd);
    }

    /**
     * Send the ephemeral public key to the card
     * @param key Ephemeral public key
     * @return Card response
     * @throws CommunicationException
     */
    private CAPDUResponse sendKey(byte[] key) throws CommunicationException {
        GeneralAuthenticate2APDUCommand generalAuthCmd = new GeneralAuthenticate2APDUCommand(key);
        generalAuthCmd.setMessageCoding(smCoding_);
        return cardAccess_.sendAPDUCommand(generalAuthCmd);
    }

    /**
     * Get the chip authentication result
     * @return Chip authentication result
     */
    public Result getResult() {
        return result_;
    }
}
