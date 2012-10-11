/*
 * HttpListenerTest.java
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

package common.network.http.tests;

import com.sun.net.httpserver.Headers;
import common.network.http.HttpListener;
import common.network.http.HttpRequestData;
import common.network.http.HttpResponseData;
import common.network.http.IHttpListenerCallback;
import common.network.soap.SOAPProcessor;
import common.network.transport.PskTlsClientSocket;
import common.network.transport.TlsPskInfo;
import common.util.Logger;
import eid.core.eIDClient;
import eid.network.data.PathSecurityData;
import eid.network.data.ProtocolDataExtractor;
import eid.network.data.TcApiOpenData;
import eid.network.data.TcTokenData;
import junit.framework.TestCase;
import org.w3c.dom.Node;

import javax.xml.bind.DatatypeConverter;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 30.05.12
 * Time: 12:18
 */
public class HttpListenerTest extends TestCase implements IHttpListenerCallback {
    private final String CONTEXT = "/";
    private final int PORT = 24727;


    public void testStart() throws InterruptedException {
        HttpListener listener;
        try {
            listener = new HttpListener(CONTEXT, PORT, this);
        } catch (IOException e) {
            fail("could not create http listener for context: '" + CONTEXT + "' and port: " + PORT);
            return;
        }
        assertNotNull(listener);
        assertEquals(listener.getContext(), CONTEXT);
        assertEquals(listener.getPort(), PORT);
        listener.start();

        Thread.sleep(3000000);
        listener.stop();
    }


    @Override
    public HttpResponseData update(HttpRequestData in){
        int buffSize = in.getDataSize();
        String res = "";
        if (buffSize > 0) {
            /*
            BufferedReader buffInput = new BufferedReader(new InputStreamReader(in.getData()));


            char[] data = new char[buffSize];
            try {
                buffInput.read(data, 0, buffSize);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            res += String.valueOf(data);
                */
            SOAPProcessor soapPoc = new SOAPProcessor();
            SOAPMessage msg = soapPoc.decode(in.getData());
            try {
                msg.writeTo(System.out);
            } catch (SOAPException e) {
                e.printStackTrace();  //TODO
            } catch (IOException e) {
                e.printStackTrace();  //TODO
            }
            SOAPBody body;

            try {
                body =  msg.getSOAPBody();

            } catch (SOAPException e) {
                e.printStackTrace();  //TODO
                return null;
            }

            Node n = body.getFirstChild();
            if(n.getLocalName().equals("TC_API_Open")){
                TcApiOpenData taod = ProtocolDataExtractor.extractTcApiOpenData(body);

                Logger.log("got request");

                TcTokenData tcTokenData = new TcTokenData(taod.getProtocolTerminationPoint(),
                        taod.getSessionIdentifier(),"","",taod.getPathSecurity());
                eIDClient client = new eIDClient();
                client.startWithTcToken(tcTokenData);
                client.waitAndStop();



            }


        }
        res += "\nNumber of params: " + String.valueOf(in.getParameters().size());
        Logger.log(res);

        Headers h = new Headers();
        h.set("Content-Type", "text/plain");

        return new HttpResponseData(h, res.getBytes());

    }
}
