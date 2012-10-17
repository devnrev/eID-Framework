/*
 * eIDProcess.java
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


import common.IState;
import common.IStateContext;
import common.cardio.ICardAccess;
import common.cardio.ITerminalAccess;
import common.data.NodeNameFilter;
import common.data.XmlAccessor;
import common.exceptions.*;
import common.network.paos.PAOSConnection;
import common.network.paos.PAOSRequest;
import common.network.paos.PAOSResponse;
import common.network.transport.PskTlsClientSocket;
import common.network.transport.TlsPskInfo;
import common.util.Logger;
import eid.core.events.AuthenticationProgressEvent;
import eid.hotspots.IeIDHostApplication;
import eid.network.data.*;
import eid.network.handler.*;


import javax.lang.model.type.NullType;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 09.07.12
 * Time: 11:02
 */


/**
 * Class which executes the whole eID protocol
 * based on the state pattern --> each state represents one protocol step
 */
public class eIDProcess extends Thread implements IStateContext {
    private PAOSConnection paosConnection_;
    private TcTokenData tcToken_;
    private IState state_;
    private EACProcess eac_;
    private ITerminalAccess terminalAccess_;
    private ICardAccess cardAccess_;
    private IeIDHostApplication hostApplication_;

    /**
     * Constructor
     * @param tcToken TcTokenData object
     * @param terminalAccess Handle of the card reader manager
     * @param hostApplication Reference of the registered host application
     */
    public eIDProcess(TcTokenData tcToken, ITerminalAccess terminalAccess, IeIDHostApplication hostApplication) {
        tcToken_ = tcToken;
        terminalAccess_ = terminalAccess;
        hostApplication_ = hostApplication;
    }

    /**
     * Thread worker loop
     */
    @Override
    public void run() {
        executeStates();
    }

    /**
     * Set the next state according to the state pattern
     * @param state Next state
     */
    @Override
    public void setNextState(IState state) {
        state_ = state;
    }

    /**
     * this method runs all states beginning with the establishment of the TLS channel
     *
     * @return true if all states run well, false if not
     */
    @Override
    public boolean executeStates() {
        state_ = new TlsEstablishmentState();
        while (hasNextState()) {
            if (!state_.handle()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Lookup if there is another state which needs to be executed
     * @return true if a state is pending
     */
    @Override
    public boolean hasNextState() {
        return (state_ != null);
    }

    /**
     * This class generalizes the function used in every state of the PAOS protocol. Basically it handles incoming
     * requests, and according to the process status, it sends back the response or an error.
     * Also it checks if StartPAOSResponse message is received, to terminate the protocol
     *
     * @param <E> Type of the incoming message data
     * @param <T> Type of the response data
     */
    public abstract class ProtocolRequestState<E, T extends ProtocolResponseType> implements IState {
        protected ProtocolRequestHandler<E, T> handler_;
        protected boolean reply_;

        /**
         * Constructor
         * @param handler Reference to the processing unit which handles incoming and outgoing messages
         */
        protected ProtocolRequestState(ProtocolRequestHandler<E, T> handler) {
            handler_ = handler;
            reply_ = true;
        }

        /**
         * Constructor
         * @param handler Reference to the processing unit which handles incoming and outgoing messages
         * @param reply false if the current state does not send a response message
         */
        protected ProtocolRequestState(ProtocolRequestHandler<E, T> handler, boolean reply) {
            handler_ = handler;
            reply_ = reply;
        }

        /**
         * Core functionality of the current state.
         * First check if the state can handle the message. then process it and send a reponse back.
         * @return true if the sate exits successful
         */
        @Override
        public boolean handle() {
            T response;
            try {
                if (handler_.checkRequest()) {
                    try {
                        response = handleImpl(handler_.extractData());
                        if (reply_) {
                            if (response.getResultType().getResultMajor() == ResultType.Major.OK) {
                                PAOSResponse resp = handler_.generateResponse(response);
                                PAOSRequest req = paosConnection_.sendMessage(resp);
                                if (req != null) {
                                    if (!checkForStartPAOSResponse(req)) {
                                        setNextState(getNextState(req));
                                    } else {
                                        setNextState(new StartPaosResponseState(req));
                                    }
                                    return true;
                                }
                            }
                        } else {
                            setNextState(getNextState(null));
                            return response.getResultType().getResultMajor() == ResultType.Major.OK;
                        }
                    } catch (BuildException e) {
                        e.printStackTrace();
                        response = getResponse(new ResultType(ResultType.Major.ERROR,
                                ResultType.Minor.INTERNAL_ERROR));
                    } catch (TranscodingException e) {
                        e.printStackTrace();
                        response = getResponse(new ResultType(ResultType.Major.ERROR,
                                ResultType.Minor.INTERNAL_ERROR));
                    } catch (ParsingException e) {
                        e.printStackTrace();
                        response = getResponse(new ResultType(ResultType.Major.ERROR,
                                ResultType.Minor.INTERNAL_ERROR));
                    }

                } else {
                    response = getResponse(new ResultType(ResultType.Major.ERROR,
                            ResultType.Minor.UNKNOWN_PROTOCOL));
                }
                if (reply_) {
                    PAOSResponse resp = handler_.generateResponse(response);
                    PAOSRequest req = paosConnection_.sendMessage(resp);
                    if (req != null) {
                        setNextState(new StartPaosResponseState(req));
                        return true;
                    }
                }
            } catch (BuildException e) {
                e.printStackTrace();
            } catch (TranscodingException e) {
                e.printStackTrace();
            }
            setNextState(null);
            return false;
        }

        /**
         * Check if the current request is equal to the StartPAOSResponse
         * @param request Received request
         * @return true if StartPAOSResponse is the message type
         */
        private boolean checkForStartPAOSResponse(PAOSRequest request) {
            XmlAccessor accessor = new XmlAccessor(request.getBodyDocument());
            NodeNameFilter nodeNameFilter = new NodeNameFilter("StartPAOSResponse",
                    NodeNameFilter.MatchingRule.EXACT_IGNORE_NAMESPACE);
            try {
                accessor.requireElement(nodeNameFilter);
                return true;
            } catch (ElementNotFoundException e) {
                return false;
            }

        }

        /**
         * Pure virtual method to specify the concrete processing method for the message content
         * @param data Message data
         * @return Response data
         * @throws BuildException
         * @throws TranscodingException
         */
        protected abstract T handleImpl(E data) throws BuildException, TranscodingException;

        /**
         * Pure virtual method which should return a new response object initialized with a result
         * @param result Result status code
         * @return Result object
         */
        protected abstract T getResponse(ResultType result);

        /**
         * Pure virtual method which should declare the following state
         * @param req Request object to initialize the next state
         * @return Object of the next state
         */
        protected abstract IState getNextState(PAOSRequest req);
    }


    /**
     * establish the the TLS channel
     */
    protected class TlsEstablishmentState implements IState {

        /**
         * Create a psk tls connection with the connection data inside the TcToken
         * @return true if successful
         */
        @Override
        public boolean handle() {
            TlsPskInfo tpi = new TlsPskInfo(tcToken_.getSessionIdentifier().getBytes(),
                    tcToken_.getPathSecurityData().getPsk());
            PskTlsClientSocket pskTlsSocket = new PskTlsClientSocket(tpi);
            String ptp = tcToken_.getServerAddress();
            String pattern="^((.*:)?//)?([a-zA-Z0-9\\-.]*)(:([0-9]+))?(.*)$";
            Pattern pskPattern = Pattern.compile(pattern);
            Matcher match = pskPattern.matcher(ptp);

            if (!match.find()) {
                Logger.log("could not parse server address");
                return false;
            }

            String hostAddress = match.group(3);
            int port = Integer.parseInt(match.group(5));
            String resourcePath =  match.group(6);
            if(resourcePath.isEmpty()){
                resourcePath="/";
            }
            try {
                pskTlsSocket.connect(hostAddress,port);
            } catch (Exception e) {
                Logger.log("Could not establish tls socket connection to eID server");
                return false;
            }
            paosConnection_ = new PAOSConnection(pskTlsSocket, tcToken_.getSessionIdentifier(),
                    hostAddress+":"+port,resourcePath);
            setNextState(new StartPaosState());
            return true;
        }
    }

    /**
     * StartPAOS step
     */
    protected class StartPaosState implements IState {

        /**
         * Generate the StartPAOS message
         * @return true if successful
         */
        @Override
        public boolean handle() {
            StartPaosHandler paosHandler = new StartPaosHandler(tcToken_);
            StartPAOSData paosData = paosHandler.generateStartPaosToken();
            try {
                PAOSResponse resp = paosHandler.generateStartPaosMessage(paosData);
                PAOSRequest req = paosConnection_.sendMessage(resp);
                if (req != null) {
                    setNextState(new InitializeFrameWorkState(req));
                    return true;
                }

            } catch (BuildException e) {
                e.printStackTrace();  //TODO
            } catch (TranscodingException e) {
                e.printStackTrace();  //TODO
            }
            setNextState(null);
            return false;
        }
    }

    /**
     * InitializeFramework step
     */
    protected class InitializeFrameWorkState extends ProtocolRequestState<NullType, InitializeFrameworkResponseType> {

        /**
         * Constructor
         * @param req PAOS Request
         */
        public InitializeFrameWorkState(PAOSRequest req) {
            super(new InitializeFrameworkHandler(req));
        }

        /**
         * Get the framework version
         * @param data Message data
         * @return Response object
         * @throws BuildException
         * @throws TranscodingException
         */
        @Override
        protected InitializeFrameworkResponseType handleImpl(NullType data)
                throws BuildException, TranscodingException {
            Logger.log("got InitializeFramework message");
            return new InitializeFrameworkResponseType(new VersionType(1, 8, 0),
                    new ResultType());
        }

        /**
         * Create new response object
         * @param result Result status code
         * @return Response object
         */
        @Override
        protected InitializeFrameworkResponseType getResponse(ResultType result) {
            return new InitializeFrameworkResponseType(result);
        }

        /**
         * Create successor state
         * @param req Request object to initialize the next state
         * @return Next state
         */
        @Override
        protected IState getNextState(PAOSRequest req) {
            return new DIDAuthenticateOneState(req);
        }

    }

    /**
     * DIDAuthenticate step with EAC1InputType
     */
    protected class DIDAuthenticateOneState extends ProtocolRequestState<DIDAuthenticateType<EAC1InputType>,
            DIDAuthenticateResponseType<EAC1OutputType>> {

        /**
         * Constructor
         * @param req PAOS Request
         */
        public DIDAuthenticateOneState(PAOSRequest request) {
            super(new DIDAuthenticateOneHandler(request));
        }

        /**
         * Do DIDAuthenticate step 1, perform PACE
         * @param data Message data
         * @return DIDAuthenticate 1 response
         * @throws BuildException
         * @throws TranscodingException
         */
        @Override
        protected DIDAuthenticateResponseType<EAC1OutputType> handleImpl(DIDAuthenticateType<EAC1InputType> data)
                throws BuildException, TranscodingException {

            List<String> connectedCards = terminalAccess_.getConnectedCards(1000, 30);
            if (connectedCards.size() > 0) {
                cardAccess_= terminalAccess_.getCardAccessor(connectedCards.get(0));
                if (cardAccess_.connect()) {
                    eac_ = new EACProcess(cardAccess_,hostApplication_.getUIComponents());
                    if (eac_.performPACE(data.getAuthProtocolData(), data.getDidName())) {
                        EAC1OutputType outputData = eac_.getPACEResult();
                        return new DIDAuthenticateResponseType<EAC1OutputType>(outputData, new ResultType());
                    }
                }
            }
            return getResponse(new ResultType(ResultType.Major.ERROR, ResultType.Minor.COMMUNICATION_ERROR));
        }

        /**
         * Create new response object
         * @param result Result status code
         * @return Response object
         */
        @Override
        protected DIDAuthenticateResponseType<EAC1OutputType> getResponse(ResultType result) {
            return new DIDAuthenticateResponseType<EAC1OutputType>(result);
        }

        /**
         * Create successor state
         * @param req Request object to initialize the next state
         * @return Next state
         */
        @Override
        protected IState getNextState(PAOSRequest req) {
            return new DIDAuthenticateTwoState(req);
        }

    }

    /**
     * DIDAuthenticate step with EAC2InputType
     */
    protected class DIDAuthenticateTwoState extends ProtocolRequestState<DIDAuthenticateType<EAC2InputType>,
            DIDAuthenticateResponseType<EAC2OutputType>> {

        private boolean signatureNeeded_;

        /**
         * Constructor
         * @param req PAOS Request
         */
        public DIDAuthenticateTwoState(PAOSRequest request) {
            super(new DIDAuthenticateTwoHandler(request));
        }

        /**
         * Perform DIDAuthenticate step 2. Do terminal authentication
         * @param data Message data
         * @return DIDAuthenticate 2 response
         * @throws BuildException
         * @throws TranscodingException
         */
        @Override
        protected DIDAuthenticateResponseType<EAC2OutputType> handleImpl(DIDAuthenticateType<EAC2InputType> data)
                throws BuildException, TranscodingException {
            if (eac_.performTA(data.getAuthProtocolData())) {
                EAC2OutputType outputData = eac_.getTerminalAuthenticationResult();
                signatureNeeded_ = outputData.isSignatureNeeded();
                if (outputData.isSignatureNeeded()) {
                    return new DIDAuthenticateResponseType<EAC2OutputType>(outputData, new ResultType());
                }
            }
            return getResponse(new ResultType(ResultType.Major.ERROR, ResultType.Minor.INTERNAL_ERROR));
        }

        /**
         * Create new response object
         * @param result Result status code
         * @return Response object
         */
        @Override
        protected DIDAuthenticateResponseType<EAC2OutputType> getResponse(ResultType result) {
            return new DIDAuthenticateResponseType<EAC2OutputType>(result);
        }

        /**
         * Create successor state
         * @param req Request object to initialize the next state
         * @return Next state
         */
        @Override
        protected IState getNextState(PAOSRequest req) {
            if (signatureNeeded_) {
                return new DIDAuthenticateThreeState(req);
            }
            return null;
        }
    }

    /**
     * DIDAuthenticate step with EACAdditionalInputType (optional, but always done in practical applications)
     */
    protected class DIDAuthenticateThreeState extends ProtocolRequestState<DIDAuthenticateType<EACAdditionalInputType>,
            DIDAuthenticateResponseType<EAC2OutputType>> {

        /**
         * Constructor
         * @param req PAOS Request
         */
        public DIDAuthenticateThreeState(PAOSRequest request) {
            super(new DIDAuthenticateThreeHandler(request));
        }

        /**
         * Do DIDAuthenticate step 3. perform chip authentication
         * @param data Message data
         * @return DIDAuthenticate 3 response
         * @throws BuildException
         * @throws TranscodingException
         */
        @Override
        protected DIDAuthenticateResponseType<EAC2OutputType>
        handleImpl(DIDAuthenticateType<EACAdditionalInputType> data) throws BuildException, TranscodingException {
            if (eac_.finalizeTA(data.getAuthProtocolData())) {
                if (eac_.performCA()) {
                    EAC2OutputType outputType = eac_.getChipAuthenticationResult();
                    return new DIDAuthenticateResponseType<EAC2OutputType>(outputType, new ResultType());
                }
            }
            return getResponse(new ResultType(ResultType.Major.ERROR, ResultType.Minor.INTERNAL_ERROR));
        }

        /**
         * Create new response object
         * @param result Result status code
         * @return Response object
         */
        @Override
        protected DIDAuthenticateResponseType<EAC2OutputType> getResponse(ResultType result) {
            return new DIDAuthenticateResponseType<EAC2OutputType>(result);
        }

        /**
         * Create successor state
         * @param req Request object to initialize the next state
         * @return Next state
         */
        @Override
        protected IState getNextState(PAOSRequest req) {
            return new TransmitState(req);
        }
    }

    /**
     * Transmit Step
     */
    protected class TransmitState extends ProtocolRequestState<TransmitRequestType,
            TransmitResponseType> {

        /**
         * Constructor
         * @param req PAOS Request
         */
        public TransmitState(PAOSRequest request) {
            super(new TransmitHandler(request));
        }

        /**
         * Send incoming apdu commands to the eID card and get results back
         * @param data Message data
         * @return Transmit response
         * @throws BuildException
         * @throws TranscodingException
         */
        @Override
        protected TransmitResponseType handleImpl(TransmitRequestType data)
                throws BuildException, TranscodingException {
            eIDCommunication cardCommunication = new eIDCommunication(cardAccess_);
            List<InputApduInfoType> inputApduInfos = data.getInputApduInfos();
            List<byte[]> outputApdus_ = new ArrayList<byte[]>();
            for (InputApduInfoType inputApduInfo : inputApduInfos) {
                byte[] responseData = null;
                try {
                    responseData = cardCommunication.sendApduCommand(inputApduInfo.getApdu());
                } catch (CommunicationException e) {
                    e.printStackTrace();
                    break;
                }
                if (!inputApduInfo.acceptsAllStatusCodes()) {
                    if (!inputApduInfo.getAcceptableStatusCodes().contains(responseData)) {
                        break;
                    }
                }
                outputApdus_.add(responseData);
            }
            if (inputApduInfos.size() == outputApdus_.size()) {
                return new TransmitResponseType(outputApdus_, new ResultType());
            }
            return getResponse(new ResultType(ResultType.Major.ERROR, ResultType.Minor.INTERNAL_ERROR));
        }

        /**
         * Create new response object
         * @param result Result status code
         * @return Response object
         */
        @Override
        protected TransmitResponseType getResponse(ResultType result) {
            return new TransmitResponseType(result);
        }

        /**
         * Create successor state
         * @param req Request object to initialize the next state
         * @return Next state
         */
        @Override
        protected IState getNextState(PAOSRequest req) {
            return new TransmitState(req);
        }
    }


    /**
     * StartPAOSResponse step - end of the protocol
     */
    protected class StartPaosResponseState extends ProtocolRequestState<StartPaosResponseType, NoResponseType> {

        /**
         * Constructor
         * @param req PAOS Request
         */
        public StartPaosResponseState(PAOSRequest request) {
            super(new StartPaosResponseHandler(request), false);
        }

        /**
         * Readout the status code in the final message and notify the host application if one has registred
         * @param data Message data
         * @return No response
         * @throws BuildException
         * @throws TranscodingException
         */
        @Override
        protected NoResponseType handleImpl(StartPaosResponseType data) throws BuildException, TranscodingException {
            Logger.log("StartPAOSResponse received: " + data.getResultType().getResultMajor().toString());
            if(cardAccess_ != null){
                cardAccess_.disconnect();
            }
            hostApplication_.notify(new AuthenticationProgressEvent(
                    AuthenticationProgressEvent.AuthProgress.FINISHED));
            return getResponse(data.getResultType());
        }

        /**
         * Create new response object
         * @param result Result status code
         * @return Response object
         */
        @Override
        protected NoResponseType getResponse(ResultType result) {
            return new NoResponseType(result);
        }

        /**
         * Create successor state
         * @param req Request object to initialize the next state
         * @return Next state
         */
        @Override
        protected IState getNextState(PAOSRequest req) {
            return null;
        }


    }
}
