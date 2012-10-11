/*
 * CTerminalAuthentication.java
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

import common.CArrayHelper;
import common.cardio.CAPDUResponse;
import common.cardio.CCardCommands;
import common.cardio.IAPDUMessageCoding;
import common.cardio.ICardAccess;
import common.cardio.coding.CAPDUNormalMessaging;
import common.cardio.coding.CAPDUSecureMessaging;
import common.crypto.*;
import common.exceptions.CommunicationException;
import common.util.Logger;
import eid.network.data.EAC2OutputType;
import libeac.cardio.*;
import libeac.definitions.PACETypes;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;


/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 02.01.12
 * Time: 00:51
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class executes the terminal authentication.
 */
public class TerminalAuthentication {
    private ICardAccess cardAccess_;
    private IAPDUMessageCoding smCoding_;
    private EAC2OutputType outptputData_;
    private byte[] auxData_;
    private byte[] efCardSecurity_;

    /**
     * Constructor
     * @param cardAccess Card accessor
     * @param secData PACE security data
     */
    public TerminalAuthentication(ICardAccess cardAccess, PACESecurityData secData) {
        cardAccess_ = cardAccess;
        if(secData != null){
            smCoding_ = new CAPDUSecureMessaging(secData.KEnc, secData.KMac, CryptoTypes.ESymmetricBlockCipher.AES);
        }else{
            smCoding_ = new CAPDUNormalMessaging();
        }

    }

    /**
     * Set the auxiliary data
     * @param auxData Auxiliary data
     */
    public void setAuthenticateAuxiliaryData(byte[] auxData) {
        auxData_ = auxData;
    }

    /**
     * Perform the terminal authentication
     * @param dvCert DV certificate
     * @param atCert AT certificate
     * @param ephemeralPublicKey Ephemeral public key
     * @param car Certificate Authority Reference
     * @return true if successful
     */
    public boolean performTA(byte[] dvCert, byte[] atCert, byte[] ephemeralPublicKey,byte[] car) {
        //IECPointFP pubKey = null;
        Certificate dvCertificate, atCertificate = null;
        try {
            dvCertificate = new Certificate(dvCert);
            if(atCert != null){
                atCertificate = new Certificate(atCert);
            }
           // pubKey = CConverter.getECPointFPFromByteArray(ephemeralPublicKey, secData_.CurveSpec.getCurveFP());
        } catch (Exception e) {
            e.printStackTrace();  //TODO
            return false;
        }
        //Logger.log(DatatypeConverter.printHexBinary(ephemeralPublicKey));

        CAPDUResponse resp;
        Logger.log("Perform terminal authentication");
//        Logger.log("Send CAR to card: " + DatatypeConverter.printHexBinary(secData_.Car));
        try {
            resp = sendCAR(car);

            if (resp.getSW() != 0x9000) {
                Logger.log("Error with CAR command\n" + resp.toString());
                return false;
            }
            Logger.log("Send DV certificate\n" + DatatypeConverter.printHexBinary(dvCertificate.getBytes()));
            resp = sendDvCertificate(dvCertificate);
            if (resp.getSW() != 0x9000) {
                Logger.log("Error with DV certificate command\n" + resp.toString());
                return false;
            }
            if (atCertificate != null) {
                byte[] byaAtCar = atCertificate.getCar();
                Logger.log("Send AT CAR to the card: " + DatatypeConverter.printHexBinary(byaAtCar));
                resp = sendCAR(byaAtCar);
                if (resp.getSW() != 0x9000) {
                    Logger.log("Error with DV CAR command\n" + resp.toString());
                    return false;
                }

                Logger.log("Send AT certificate\n" + DatatypeConverter.printHexBinary(atCertificate.getBytes()));
                resp = sendAtCertificate(atCertificate);
                if (resp.getSW() != 0x9000) {
                    Logger.log("Error with AT certificate command\n" + resp.toString());
                    return false;
                }
            }
            /*
            Logger.log("Generate ecc key pair");
            if(!generateKeys()){
                Logger.log("Key pair could not be generated");
                return false;
            }
            */

            try {
                Logger.log("Send Mse Set AT to the card\nPublic key:" +
                        DatatypeConverter.printHexBinary(ephemeralPublicKey) + "\n" +
                        "CHR: " + DatatypeConverter.printHexBinary(atCertificate.getChr()) + "\n" +
                        "Oid: " + atCertificate.getOid().toString());

                resp = sendMseSetAT(ephemeralPublicKey, atCertificate.getChr(), atCertificate.getOid(), auxData_);
            } catch (IOException e) {
                Logger.log("Could not get terminal authentication oid from AT certificate!");
                return false;
            }
            if (resp.getSW() != 0x9000) {
                Logger.log("Card responds with an error. " + resp.toString());
                return false;
            }
            Logger.log("Mse Set AT was successfully setup");
            Logger.log("Try to get Challenge");
            resp = getChallenge();
            if (resp.getSW() != 0x9000) {
                Logger.log("Card responds with an error. " + resp.toString());
                return false;
            }
        } catch (CommunicationException e) {
            e.printStackTrace();  //TODO
            return false;
        }
        byte[] challenge = resp.getData();
        Logger.log("Challenge received: " + DatatypeConverter.printHexBinary(challenge));
        outptputData_ = new EAC2OutputType(challenge);
        return true;
    }

    /**
     * Send the Certificate Authority Reference to the card
     * @param byaCar Certificate Authority Reference
     * @return Card response
     * @throws CommunicationException
     */
    public CAPDUResponse sendCAR(byte[] byaCar) throws CommunicationException {
        CMSEDSTAPDUCommand carMSE = new CMSEDSTAPDUCommand();
        carMSE.appendData(CArrayHelper.concatArrays(new byte[]{(byte) 0x83, (byte) byaCar.length}, byaCar));
        carMSE.setMessageCoding(smCoding_);
        return cardAccess_.sendAPDUCommand(carMSE);
    }

    /**
     * Send the DV certificate to the card
     * @param cert DV certificate
     * @return Card response
     * @throws CommunicationException
     */
    public CAPDUResponse sendDvCertificate(Certificate cert) throws CommunicationException {
        return sendCertificate(cert);
    }

    /**
     * Send the AT certificate to the card
     * @param cert AT certificate
     * @return Card response
     * @throws CommunicationException
     */
    public CAPDUResponse sendAtCertificate(Certificate cert) throws CommunicationException {
        return sendCertificate(cert);
    }

    /**
     * Initialize the terminal authentication part 1
     * @param pk_pcd PK_PCD
     * @param byaChr Certificate Holder Reference
     * @param oid Protocol identifier
     * @param auxData Auxiliary data
     * @return Card response
     * @throws CommunicationException
     */
    public CAPDUResponse sendMseSetAT(byte[] pk_pcd, byte[] byaChr, IDERObjectIdentifier oid, byte[] auxData)
            throws CommunicationException {
        CMSEATAPDUCommand mseSetAt = new CMSEATAPDUCommand(PACETypes.AuthenticationType.TA);
        mseSetAt.setProtocol(oid);
        mseSetAt.setEphemeralPublicKey(pk_pcd);
        mseSetAt.setPublicKeyReference(byaChr);
        if (auxData != null) {
            mseSetAt.setAuxData(auxData);
        }
        mseSetAt.setMessageCoding(smCoding_);
        return cardAccess_.sendAPDUCommand(mseSetAt);

    }

    /**
     * Perform terminal authentication part 2. Get EF.CardSecurity file from the card
     * @param signature Signature of the challenge
     * @return true if successful
     */
    public boolean performTA2(byte[] signature) {
        Logger.log("Send signature: " + DatatypeConverter.printHexBinary(signature));
        CAPDUResponse response = null;
        try {
            response = sendSignature(signature);
            if (response.getSW() != 0x9000) {
                Logger.log("Card responds with an error. " + response.toString());
                return false;
            }
            Logger.log("Signature sent to card\nTerminal Authentication successful");
            Logger.log("try to read EFCardSecurity");
            byte[] efCardSecurity;

            efCardSecurity = readEfCardSecurity();
            Logger.log(DatatypeConverter.printHexBinary(efCardSecurity));
            efCardSecurity_ = efCardSecurity;
        } catch (CommunicationException e) {
            e.printStackTrace();  //TODO
            Logger.log("Could not read EFCardSecurity");
            return false;
        }

        return true;
    }

    /**
     * Get the EF.CardSecurity file from the card
     * @return EF.CardSecurity data
     * @throws CommunicationException
     */
    private byte[] readEfCardSecurity() throws CommunicationException {
        CCardCommands cardCmd = new CCardCommands(cardAccess_, smCoding_);
        return cardCmd.readCardFile(new byte[]{(byte) 0x01, (byte) 0x1D});
    }

    /**
     * Get the challenge from the card
     * @return Card response
     * @throws CommunicationException
     */
    public CAPDUResponse getChallenge() throws CommunicationException {
        CGetChallengeAPDUCommand challengeCmd = new CGetChallengeAPDUCommand();
        challengeCmd.setMessageCoding(smCoding_);
        return cardAccess_.sendAPDUCommand(challengeCmd);
    }

    /**
     * Send the signature to the card
     * @param signature Challenge signature
     * @return Card repsonse
     * @throws CommunicationException
     */
    private CAPDUResponse sendSignature(byte[] signature) throws CommunicationException {
        ExternalAuthenticateAPDUCommand extAuthCmd = new ExternalAuthenticateAPDUCommand(signature);
        extAuthCmd.setMessageCoding(smCoding_);
        return cardAccess_.sendAPDUCommand(extAuthCmd);
    }

    /*
    private boolean generateKeys(){
        IEccKeyGenerator keyGen;
        try {
            keyGen = CFactoryHelper.getCryptoProvider().createEccKeyGenerator(secData_.CurveSpec);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        }
        m_eccKeyPair = keyGen.generate();
        return true;
    }
      */

    /**
     * Send a certificate to the card
     * @param cert Certificate
     * @return Card repsonse
     * @throws CommunicationException
     */
    private CAPDUResponse sendCertificate(Certificate cert) throws CommunicationException {
        CPSOVerifyCertAPDUCommand secOP = new CPSOVerifyCertAPDUCommand();
        secOP.appendData(cert.getBytes());
        secOP.setMessageCoding(smCoding_);
        return cardAccess_.sendAPDUCommand(secOP);
    }

    /**
     * Get the terminal authentication data used in DIDAuthenticate step 2 and 3
     * @return EAC2OutputType data
     */
    public EAC2OutputType getOutptputData() {
        return outptputData_;
    }

    /**
     * Get the EF.CardSecurity data
     * @return EF.CardSecurity data
     */
    public byte[] getEfCardSecurity() {
        return efCardSecurity_;
    }
}
