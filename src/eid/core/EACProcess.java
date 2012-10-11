/*
 * CEAC.java
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

package eid.core;

import common.Globals;
import common.cardio.ControlCommand;
import common.cardio.ICardAccess;
import common.exceptions.CommunicationException;
import common.exceptions.ElementNotFoundException;
import common.exceptions.ParsingException;
import common.util.EnumHelper;
import eid.hotspots.IeIDUIComponents;
import eid.network.data.*;
import libeac.*;
import libeac.cardio.reader.*;
import libeac.definitions.PACETypes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 02.01.12
 * Time: 00:35
 * To change this template use File | Settings | File Templates.
 */

/**
 * This facade class provide methods to perform EAC Version 2 which includes PACE
 *
 */
public class EACProcess {
    private PACESecurityData secData_;
    private ICardAccess cardAccess_;
    private EAC1OutputType eac1OutputType_;
    private EAC1InputType eac1InputType_;
    private EAC2InputType eac2InputType_;
    private EAC2OutputType eac2OutputType_;
    private SecurityInfos secInfos_;
    private byte[] efCardSecurity_;
    private FeatureTable featureTable_;
    private IeIDUIComponents uiComponents_;

    /**
     * Constructor
     * @param cardAccess Card access handle
     * @param uiComponents Reference to the ui implementations
     */
    public EACProcess(ICardAccess cardAccess, IeIDUIComponents uiComponents) {
        cardAccess_ = cardAccess;
        uiComponents_ = uiComponents;

    }

    /**
     * Check if a comfort card reader is used in conjunction with the eID card
     * @return true if comfort card reader is used
     * @throws CommunicationException
     * @throws ParsingException
     */
    public boolean checkForComfortReader() throws CommunicationException, ParsingException {
        if(Globals.getOSType() == Globals.OS.OSX){
            return false; //TODO: transmit control command seems not to work under osx!
        }
        GetFeatureCodesCommand getFeatureCodesCommand = new GetFeatureCodesCommand();
        byte[] res = cardAccess_.sendControlCommand(getFeatureCodesCommand);
        if(res == null || res.length == 0){
            return false;
        }
        featureTable_ = new FeatureTable(res);
        ControlCommand ctlCmd = new ControlCommand(featureTable_.getControlCodeForFeature(0x20),
                new ReaderPaceCapabilitiesRequest().getBytes());
        ExecutePaceResponse executePaceResponse =
                new ExecutePaceResponse(cardAccess_.sendControlCommand(ctlCmd));

        PaceCapabilities cap = new PaceCapabilities(executePaceResponse.getData());
        if (cap.canDoPace() && cap.supportsEId()) {
            return true;
        }
        return false;
    }

    /**
     * Get the result object containing the PACE security data
     * @return PACE Result
     */
    public EAC1OutputType getPACEResult() {
        return eac1OutputType_;
    }

    /**
     * Get the result object containing
     * @return Terminal autentication result
     */
    public EAC2OutputType getTerminalAuthenticationResult() {
        return eac2OutputType_;
    }


    /**
     * Perform pace using a comfort card reader. The password input takes place through the PIN pad on the card reader.
     * @param passwordType Password type
     * @param chat Certificate holder Authorization Template
     * @param certDesc Certificate description
     * @return true if no error occurs
     */
    protected boolean establishPaceChannel(PACETypes.Password passwordType,
                                           CertHolderAuthTemplate chat,
                                           byte[] certDesc) {
        ControlCommand ctlCmd = new ControlCommand(featureTable_.getControlCodeForFeature(0x20),
                new ReaderEstablishPaceRequest(passwordType, chat, null, certDesc).getBytes());
        try {
            ExecutePaceResponse executePaceResponse =
                    new ExecutePaceResponse(cardAccess_.sendControlCommand(ctlCmd));
            EstablishPaceChannelResponseParser respParser =
                    new EstablishPaceChannelResponseParser(executePaceResponse.getData());
            if (respParser.getStatus() == 0x9000) {
                secInfos_ = new SecurityInfos();
                secInfos_.initialize(respParser.getEfCardAccess());
                eac1OutputType_ = new EAC1OutputType();
                List<byte[]> carList = new ArrayList<byte[]>();
                carList.add(respParser.getCar());
                carList.add(respParser.getCarPev());
                eac1OutputType_.setCar(carList);
                eac1OutputType_.setEfCardAccess(respParser.getEfCardAccess());
                eac1OutputType_.setIdPICC(respParser.getIdPicc());
                eac1OutputType_.setChat(chat.getEncoded());
                eac1OutputType_.setRetryCounter(0);


                return true;
            }
        } catch (ParsingException e) {
            e.printStackTrace();
        } catch (CommunicationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Perform PACE, either through a comfort card reader if connected or by an own implementation. for the latter
     * use the  UI components to display the corresponding user interface elements
     * @param inputType Received data from the eID Server
     * @param pwType Password type
     * @return true if PACE could be done successfully
     */
    public boolean performPACE(EAC1InputType inputType, String pwType) {

        String didName = pwType;
        PACETypes.Password passwordType;
        try {
            passwordType =
                    new EnumHelper<PACETypes.Password>().getEnumValue(PACETypes.Password.class, didName);

        } catch (ElementNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        CertHolderAuthTemplate chat = null;
        try {
            chat = CertHolderAuthTemplate.fromBytes(
                    inputType.getRequiredCHAT());
        } catch (ParsingException e) {
            e.printStackTrace();
            return false;
        }

        boolean comfortReader = false;
        try {
            comfortReader = checkForComfortReader();
        } catch (CommunicationException e) {
            e.printStackTrace();
            return false;
        } catch (ParsingException e) {
            e.printStackTrace();
            return false;
        }
        if (comfortReader) {

            List<byte[]> certDescList = inputType.getCertificateDescriptionList();
            byte[] certDesc = null;
            if (certDescList.size() > 0) {
                certDesc = certDescList.get(0);
            }
            if(establishPaceChannel(passwordType, chat, certDesc)){
                eac1InputType_ = inputType;
                return true;
            }
        } else {

            String pin = uiComponents_.getPassword();
            if(pin.equals("")){
                return false;
            }

            PACERoutine paceRoutine = new PACERoutine(cardAccess_);
            paceRoutine.setAuxData(inputType.getAuthenticatedAuxiliaryData());
            if (paceRoutine.doPACE(pin, passwordType, chat)) {
                secData_ = paceRoutine.getPaceSecData();
                eac1OutputType_ = paceRoutine.getEac1OutputData();
                eac1InputType_ = inputType;
                secInfos_ = paceRoutine.getSecurityInfos();
                return true;
            }

        }
        return false;
    }

    /**
     * Old style PACE call. not used anymore
     * @param szPassword The password
     * @param ePwType Password type
     * @param chat Certificate Holder Authorization Template
     * @return true if successful
     */
    public boolean performPACE(String szPassword, PACETypes.Password ePwType, CertHolderAuthTemplate chat) {
        PACERoutine paceRoutine = new PACERoutine(cardAccess_);
        if (paceRoutine.doPACE(szPassword, ePwType, chat)) {
            secData_ = paceRoutine.getPaceSecData();
            return true;
        }
        return false;

    }

    /**
     * Perform terminal authentication. Certificate chain is used by previously received EAC1InputType
     * 1. Certificate in the list is the ATCertificate
     * 2. Certificate in the list is the DVCertificate
     * Additional terminal step is needed if challenge hasn't been sent to eID Server before
     *
     * @param inputData EAC2 AuthenticationProtocolData data structure
     * @return true if action was successful, otherwise false
     */
    public boolean performTA(EAC2InputType inputData) {
        //if (secData_ == null)
            //return false;
        TerminalAuthentication terminalAuth = new TerminalAuthentication(cardAccess_, secData_);
        List<byte[]> certificateListEac1 = eac1InputType_.getCertificateList();
        if (certificateListEac1.size() >= 2) {
            terminalAuth.setAuthenticateAuxiliaryData(eac1InputType_.getAuthenticatedAuxiliaryData());
            if (terminalAuth.performTA(certificateListEac1.get(1),
                    certificateListEac1.get(0),
                    inputData.getEphemeralPublicKey(),eac1OutputType_.getCarList().get(0))) {
                eac2InputType_ = inputData;
                eac2OutputType_ = terminalAuth.getOutptputData();
                return true;
            }
        }
        return false;
    }

    /**
     * Do the additional terminal authentication step, in order to get the ef card security file.
     * @param inputData Signature of the challenge received from the Server
     * @return true if successful
     */
    public boolean finalizeTA(EACAdditionalInputType inputData) {
        TerminalAuthentication terminalAuth = new TerminalAuthentication(cardAccess_, secData_);
        if (terminalAuth.performTA2(inputData.getSignature())) {
            efCardSecurity_ = terminalAuth.getEfCardSecurity();
            return true;
        }
        return false;
    }

    /**
     * Perform chip authentication. It is assumed, that terminal authentication was done before.
     * @return true if successful
     */
    public boolean performCA() {
        ChipAuthentication chipAuth = new ChipAuthentication(cardAccess_, secData_);

        if (chipAuth.performCA(secInfos_.getCaOid(),
                secInfos_.getCaPrivateKeyRef(),
                eac2InputType_.getEphemeralPublicKey())) {

            ChipAuthentication.Result res = chipAuth.getResult();
            if (efCardSecurity_ != null && res != null) {
                eac2OutputType_ = new EAC2OutputType(efCardSecurity_, res.getT_picc_(), res.getR_picc_());
                return true;
            }
        }
        return false;
    }

    /**
     * Get the chip authentication result
     * @return Result object
     */
    public EAC2OutputType getChipAuthenticationResult() {
        return eac2OutputType_;
    }
}
