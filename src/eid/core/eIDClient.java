/*
 * eIDProtocol.java
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

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import common.cardio.ITerminalAccess;
import common.cardio.TerminalAccess;
import common.exceptions.ParsingException;
import common.exceptions.TranscodingException;
import common.network.IHttpMessageObserver;
import common.network.http.*;
import common.network.paos.PAOSRequest;
import common.network.paos.PaosMessageParser;
import common.network.paos.PaosToHttpProcessor;
import common.network.transport.NetworkHttpServer;
import common.util.Logger;
import eid.hotspots.IeIDHostApplication;
import eid.network.data.TcApiOpenData;
import eid.network.data.TcTokenData;
import eid.network.handler.TcApiOpenHandler;
import eid.network.handler.TcTokenHandler;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 02.07.12
 * Time: 17:17
 */

/**
 * Main class which initializes the eID program.
 * When required it starts a http server to listen for incoming authentification requests.
 * This should be the only class used by external applications.
 */
public class eIDClient implements IHttpMessageObserver{

    private NetworkHttpServer httpServer_;

    /**
     * Normal context used for eID authentication queries
     */
    //private final String CONTEXT = "/eID-Client";

    /**
     * Context used for temporary support for old eID authentication queries
     */
    private final String CONTEXT = "/";

    private final int PORT = 24727;
    private List<eIDProcess> eIDProcessList_;
    private ITerminalAccess terminalAccess_;
    private IeIDHostApplication hostApplication_;

    /**
     * Constructor
     */
    public eIDClient(){
        eIDProcessList_ = new ArrayList<eIDProcess>();
        terminalAccess_ = new TerminalAccess();
        terminalAccess_.observeTerminals();
        hostApplication_ = new BasicEIDHostApplication();
    }

    /**
     * The application which uses the library can register itself here, to receive events and for applying
     * ui customizations
     * @param hostApplication Host application
     */
    public void registerHostApplication(IeIDHostApplication hostApplication){
        hostApplication_ = hostApplication;
    }

    /**
     * Starts the http server and register to it in order to receive authentication requests
     * @return true if the server is successfully started
     */
    public boolean startListener(){
        try {
            httpServer_ = new NetworkHttpServer(PORT,CONTEXT,this);
        } catch (IOException e) {
            Logger.log("could not create http listener for context: '" + CONTEXT + "' and port: " + PORT);
            return false;
        }
        httpServer_.start();
        return true;
    }

    /**
     * Stop the newtork http server and wait till all  eID processes have finished
     */
    public void waitAndStop(){
        if(httpServer_!= null){
            httpServer_.stop();
        }
        terminalAccess_.stop();
        for(eIDProcess process : eIDProcessList_){
            process.interrupt();
            try {
                process.join();
            } catch (InterruptedException e) {
                //interupted
            }
        }


    }

    /**
     * Retrieve a TcToken from a given url
     * @param url URL as String
     * @return TcTokenData object
     * @throws Exception
     */
    private TcTokenData getTcTokenFromUrl(String url) throws Exception {
        TcTokenHandler tch = new TcTokenHandler(url);
        return tch.getTcToken();
    }

    /**
     * Update method which the http server calls, if a message was received
     * @param request Http data of the authentication request
     * @return Http response
     */
    @Override
    public HttpResponse update(HttpRequest request) {
        String url = request.getRessourcePath();
        Pattern pattern = Pattern.compile("tcTokenURL=(.*)");
        Matcher match = pattern.matcher(url);
        if(!match.find()){
            return processAlternativeHttpRequest(request);
        }
        try {
            startWithTokenUrl(URLDecoder.decode(match.group(1), "UTF-8"));
            return new HttpResponse(request.getVersion(),200,"OK");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    /**
     * Support for old eID authentication requests
     * Search for TC_API_OPEN part
     * @param request Http request
     * @return Http response
     */
    protected HttpResponse processAlternativeHttpRequest(HttpRequest request){
        HttpHeader header = request.getHeader();
        List<String> entry =  header.getHeaderEntry("SOAPAction");
        if(entry.size()> 0){
            if(entry.get(0).contains("TC_API_Open")){
                Logger.log("Got TC_API_Open message");
                try {
                    PAOSRequest paosRequest = PaosMessageParser.parseRequest(request.getBody());
                    TcApiOpenHandler tcApiOpenHandler = new TcApiOpenHandler(paosRequest);
                    if(tcApiOpenHandler.checkRequest()){
                        TcApiOpenData tcApiOpenData = tcApiOpenHandler.extractData();
                        TcTokenData tcToken = new TcTokenData(
                                tcApiOpenData.getProtocolTerminationPoint(),
                                tcApiOpenData.getSessionIdentifier(),
                                "",
                                tcApiOpenData.getBinding(),
                                tcApiOpenData.getPathSecurity());
                        startWithTcToken(tcToken);
                    }
                } catch (TranscodingException e) {
                    e.printStackTrace();
                }  catch (ParsingException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Start a new authentication process with a given TcTokenData object
     * @param tcToken TcTokenData object
     */
    public void startWithTcToken(TcTokenData tcToken){
        Logger.log("Received TcToken: \n"+tcToken.toString()+"\n");
        eIDProcess process = new eIDProcess(tcToken, terminalAccess_,hostApplication_);
        eIDProcessList_ .add(process);
        process.start();
    }

    /**
     * Start a new authentication process with a given TcToken URL
     * @param url TcToken URL as a String
     * @return true if TcToken could be retrieved and the process could be started
     */
    public boolean startWithTokenUrl(String url){
        TcTokenData tcToken;
        try {
            tcToken = getTcTokenFromUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Logger.log("Received TcToken: \n"+tcToken.toString()+"\n");
        eIDProcess process = new eIDProcess(tcToken,terminalAccess_,hostApplication_);
        eIDProcessList_ .add(process);
        process.start();
        return true;
    }

}
