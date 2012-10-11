/*
 * CPACERoutine.java
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

import common.CConverter;
import common.cardio.CAPDUResponse;
import common.cardio.CCardCommands;
import common.cardio.ICardAccess;
import common.exceptions.CommunicationException;
import common.exceptions.ParameterException;
import common.util.Logger;
import eid.network.data.EAC1OutputType;
import libeac.cardio.CGeneralAuthenticateAPDUCommand;
import libeac.cardio.CGetNonceADPUCommand;
import libeac.cardio.CMSEATAPDUCommand;
import libeac.cardio.CPACEFinalAPDUCommand;
import common.crypto.IDERObjectIdentifier;
import common.crypto.IECPoint;
import common.crypto.IECPointFP;
import libeac.definitions.PACETypes;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 11.12.11
 * Time: 01:30
 * To change this template use File | Settings | File Templates.
 */

/**
 * This executes the PACE protocol as defined in TR-03110
 */
public class PACERoutine {
    private ICardAccess cardAccess_;
    private PACECore paceCore_;
    private PACESecurityData secDat_;
    private byte[] auxData_;
    private EAC1OutputType eac1OutputData_;
    private SecurityInfos secInfos_;


    /**
     * Constructor
     * @param cardIO Card access object
     */
    public PACERoutine(ICardAccess cardIO) {
        cardAccess_ = cardIO;
    }

    /**
     * Start the PACE protocol
     * @param szPwd Password
     * @param ePassword Password type
     * @param chat Certificate Holder Authorization Template
     * @return true if protocol was successful
     */
    public boolean doPACE(String szPwd, PACETypes.Password ePassword, CertHolderAuthTemplate chat) {
        Logger.log("Get security information from the card");
        try {
            secInfos_ = getSecurityInformation();
        } catch (Exception e) {
            Logger.log("Error while getting security information from the card: " + e.toString());
            return false;
        }

        Logger.log("Setup MSESetAT");
        PACEInfos paceInfo = new PACEInfos(secInfos_.getPaceInfo());
        CAPDUResponse mseResp;
        try {
            mseResp = setupMSESetAT(PACETypes.AuthenticationType.PACE, paceInfo.getProtocol(), ePassword, chat);
            if (mseResp.getSW() != 0x9000) {
                Logger.log("Could not initialize PACE protocol. MSE Set AT failed.\n" + mseResp.toString());
                return false;
            }
            Logger.log("Get nonce from card");
            byte[] byaNonce = getNonce();

            Logger.log("Nonce got from card: " + DatatypeConverter.printHexBinary(byaNonce));


            paceCore_ = new PACECore(paceInfo);
            paceCore_.calcKPassword(szPwd);
            paceCore_.decryptNonce(byaNonce);

            Logger.log("Decrypted nonce s: " + DatatypeConverter.printHexBinary(paceCore_.getDecryptedNonce()));
            CAPDUResponse apduResp = doTerminalMapping();
            if (apduResp.getSW() != 0x9000) {
                Logger.log("Error doing PACE terminal mapping");
                return false;
            }
            Logger.log("Got mapping response Y from card: " +
                    DatatypeConverter.printHexBinary(apduResp.getData()));

            apduResp = doTerminalEphemeralKeyGen(apduResp);

            if (apduResp.getSW() != 0x9000) {
                Logger.log("Terminal ephemeral key command was not accepted by the card!");
                apduResp.toString();
                return false;
            }

            Logger.log("Got data from card: " + DatatypeConverter.printHexBinary(apduResp.getData()));


            apduResp = finalizeAgreement(apduResp);

            if (apduResp.getSW() != 0x9000) {
                Logger.log("Terminal authentication token command was not accepted by the card!");
                Logger.log(apduResp.toString());
                return false;
            }

            Logger.log("Got authentication token from card: " +
                    DatatypeConverter.printHexBinary(apduResp.getData()));

            DynamicAuthenticationData dynDat = new DynamicAuthenticationData();
            dynDat.initialize(apduResp.getData());


            if (!paceCore_.Verify(dynDat.getChipAuthenticationToken())) {
                Logger.log("Authentication response could not be verified");
                return false;
            }

            Logger.log("PACE connection established");

            secDat_ = new PACESecurityData();
            secDat_.KMac = paceCore_.getKMac();
            secDat_.KEnc = paceCore_.getKEnc();
            secDat_.Car = dynDat.getCertificateAuthorityReferenceToken1();
            secDat_.CurveSpec = paceCore_.getCurveSpec();
            secDat_.IdPICC = CConverter.convertBigIntegerToByteArray(paceCore_.getPK_PICC().getX());

            eac1OutputData_ = new EAC1OutputType();
            eac1OutputData_.setChat(chat.getEncoded());
            eac1OutputData_.setIdPICC(secDat_.IdPICC);
            eac1OutputData_.setEfCardAccess(secInfos_.getEncodedData());
            List<byte[]> carList = new ArrayList<byte[]>();
            carList.add(dynDat.getCertificateAuthorityReferenceToken1());
            byte[] car2 = dynDat.getCertificateAuthorityReferenceToken2();
            if (car2 != null) {
                carList.add(car2);
            }
            eac1OutputData_.setCar(carList);
            return true;
        } catch (CommunicationException e) {
            e.printStackTrace();
            return false;
        } catch (ParameterException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Do the terminal mapping
     * @return Card response
     * @throws CommunicationException
     */
    private CAPDUResponse doTerminalMapping() throws CommunicationException {
        Logger.log("Calculate terminal mapping");
        IECPoint X = paceCore_.calcX();
        Logger.log("Send to card - ECPoint X: " + DatatypeConverter.printHexBinary(X.getEncoded()));
        DynamicAuthenticationData dynDat = new DynamicAuthenticationData();
        dynDat.setTerminalMapping(X.getEncoded());
        CGeneralAuthenticateAPDUCommand gaCmd = new CGeneralAuthenticateAPDUCommand();
        gaCmd.appendData(dynDat.getDERTerminalAuthenticationData());
        CAPDUResponse gaResp = cardAccess_.sendAPDUCommand(gaCmd);
        return gaResp;
    }

    /**
     * Calculate an ephemeral public key
     * @param resp Previous APDU message received from the card
     * @return Card response
     * @throws CommunicationException
     */
    private CAPDUResponse doTerminalEphemeralKeyGen(CAPDUResponse resp) throws CommunicationException {
        Logger.log("Calculate terminal ephemeral key");
        DynamicAuthenticationData dynDat = new DynamicAuthenticationData();
        dynDat.initialize(resp.getData());
        IECPointFP Y = CConverter.getECPointFPFromByteArray(dynDat.getChipMapping(), paceCore_.getCurve());
        IECPoint PK_PCD = paceCore_.calcPkPcd(Y);
        Logger.log("Send to Card - ECPoint PK_PCD: " + DatatypeConverter.printHexBinary(PK_PCD.getEncoded()));
        dynDat.reset();
        dynDat.setTerminalEphemeralPK(PK_PCD.getEncoded());
        CGeneralAuthenticateAPDUCommand gaCmd = new CGeneralAuthenticateAPDUCommand();
        gaCmd.appendData(dynDat.getDERTerminalAuthenticationData());
        return cardAccess_.sendAPDUCommand(gaCmd);
    }

    /**
     * Do the last step in the PACE protocol. Send the terminal authentication token.
     * @param resp Previous data received form the card
     * @return Card response
     * @throws CommunicationException
     */
    private CAPDUResponse finalizeAgreement(CAPDUResponse resp) throws CommunicationException {
        Logger.log("Finalize PACE protocol");
        DynamicAuthenticationData dynDat = new DynamicAuthenticationData();
        dynDat.initialize(resp.getData());
        IECPointFP PK_PICC = CConverter.getECPointFPFromByteArray(dynDat.getChipEphemeralPK(), paceCore_.getCurve());
        Logger.log("Chip ephemeral key PK_PICC: " + DatatypeConverter.printHexBinary(PK_PICC.getEncoded()));
        byte[] T_PCD = paceCore_.calcTPcd(PK_PICC);
        Logger.log("SecretKey K: " + DatatypeConverter.printHexBinary(paceCore_.getK().toByteArray()));
        Logger.log("KMac: " + DatatypeConverter.printHexBinary(paceCore_.getKMac()));
        Logger.log("KEnc: " + DatatypeConverter.printHexBinary(paceCore_.getKEnc()));
        Logger.log("Send to Card - T_PCD: " + DatatypeConverter.printHexBinary(T_PCD));
        dynDat.reset();
        dynDat.setTerminalAuthToken(T_PCD);
        CPACEFinalAPDUCommand gaCmd = new CPACEFinalAPDUCommand();
        gaCmd.appendData(dynDat.getDERTerminalAuthenticationData());
        return cardAccess_.sendAPDUCommand(gaCmd);

    }

    /**
     * Get the nonce from the card
     * @return Card response
     * @throws CommunicationException
     */
    private byte[] getNonce() throws CommunicationException {
        CAPDUResponse resp = cardAccess_.sendAPDUCommand(new CGetNonceADPUCommand());
        if (resp.getData() == null)
            throw new NullPointerException("Nonce response is empty: " + resp.toString());
        DynamicAuthenticationData dynDat = new DynamicAuthenticationData();
        dynDat.initialize(resp.getData());
        return dynDat.getEncryptedNonce();
    }

    /*
     * standard test eid card protocol is 0.4.0.127.0.7.2.2.4.2.2 version 2 paramid 13
     */

    /**
     * Initialize the PACE protocol and tell send parameters to the card
     * @param eAuth Authentication type
     * @param protocol Protocol identifier
     * @param ePassword Password type
     * @param chat Certificate Holder Authorization Template
     * @return Card response
     * @throws CommunicationException
     */
    private CAPDUResponse setupMSESetAT(PACETypes.AuthenticationType eAuth, IDERObjectIdentifier protocol,
                                        PACETypes.Password ePassword, CertHolderAuthTemplate chat)
            throws CommunicationException {
        Logger.log("MSESetAT parameters \nAuthentication Type: " + eAuth +
                "\nProtocol: " + protocol + "\nPassword: " + ePassword);
        CMSEATAPDUCommand cmd = new CMSEATAPDUCommand(eAuth);
        cmd.setProtocol(protocol);
        cmd.setPassword(ePassword);
        cmd.setPrivateKeyReference(0xD);
        //cmd.setAuxData(auxData_); // breaks mse:at
        cmd.setCHAT(chat);
        Logger.log(DatatypeConverter.printHexBinary(cmd.getBytes()));

        CAPDUResponse resp = cardAccess_.sendAPDUCommand(cmd);
        return resp;
    }

    /**
     * Get the EF.CardAccess file from the card
     * @return File contents wrapped in a security object
     * @throws CommunicationException
     * @throws IOException
     */
    private SecurityInfos getSecurityInformation() throws CommunicationException, IOException {
        SecurityInfos secInfo = new SecurityInfos();
        CCardCommands cardCmd = new CCardCommands(cardAccess_);
        secInfo.initialize(cardCmd.readCardFile(new byte[]{(byte) 0x01, (byte) 0x1C}));
        return secInfo;
    }

    /**
     * Get final PACE protocol data including the agreed keys
     * @return PACE security data
     */
    public PACESecurityData getPaceSecData() {
        return secDat_;
    }

    /**
     * Set auxiliary data
     * @param auxData Auxiliary data
     */
    public void setAuxData(byte[] auxData) {
        auxData_ = auxData;
    }

    /**
     * Get the PACE protocol data required in DIDAuthenticate step 1
     * @return
     */
    public EAC1OutputType getEac1OutputData() {
        return eac1OutputData_;
    }

    /**
     * Get the EF.CardAccess data wrapped in a security object
     * @return Security object
     */
    public SecurityInfos getSecurityInfos() {
        return secInfos_;
    }
}
