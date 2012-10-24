/*
 * HttpClientMessageProcessor.java
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

package common.network.http;


import common.exceptions.ElementNotFoundException;
import common.exceptions.TranscodingException;
import common.network.messaging.IStreamMessageProcessor;
import common.util.EnumHelper;

import java.io.*;
import java.net.SocketException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 28.06.12
 * Time: 11:42
 */

/**
 * This class handles messages based on stream input and output streams.
 * A HTTP request is received read form an InputStream and a HTTP Response is written to an OutputStream
 */
public class HttpClientMessageStreamProcessor implements IStreamMessageProcessor<HttpResponse, HttpRequest> {

    /**
     * Encode HTTP response and write to the given OutputStream
     * @param message HTTP response data
     * @param out OutputStream
     * @throws TranscodingException
     */
    @Override
    public void encode(HttpResponse message, OutputStream out) throws TranscodingException {
        try {
            out.write(message.getBytes());
        } catch (IOException e) {
            throw new TranscodingException("could not not encodeAndAdd HttpResponse to outputstream");
        }
    }

    /**
     * Read data from an InputStream and decode it into a HTTP request
     * @param message InputStream
     * @return  HTTP Request object
     * @throws TranscodingException
     * @throws SocketException
     */
    @Override
    public HttpRequest decode(InputStream message) throws TranscodingException, SocketException {
        InputStreamReader reader = new InputStreamReader(message);
        BufferedReader bfr = new BufferedReader(reader);
        try {
            String data = bfr.readLine();

            if (data == null || data.equals("")) {
                return null;
            }
            String[] initialLine = data.split(" ");
            if (initialLine.length != 3) {
                throw new TranscodingException("initial line of http request has not expected format");
            }
            HttpRequest req = new HttpRequest();
            req.setResourcePath(initialLine[1]);
            req.setMethod(new EnumHelper<HttpRequest.HttpMethod>().
                    getEnumValue(HttpRequest.HttpMethod.class, initialLine[0]));
            req.setVersion(new EnumHelper<HttpMessage.HttpVersion>().
                    getEnumValue(HttpMessage.HttpVersion.class, initialLine[2]));
            HttpHeader header = new HttpHeader();
            while ((data = bfr.readLine()) != null) {
                if ((!data.equals(""))) {
                    String attr[] = data.split(": ");
                    if (attr.length != 2) {
                        throw new TranscodingException("invald http attribute:" + data);
                    }
                    List<String> values = Arrays.asList(attr[1].split(";"));
                    header.addHeaderInfo(attr[0], values);
                } else {
                    break;
                }
            }
            if (req.getMethod() == HttpRequest.HttpMethod.POST) {
                List<String> attr = header.getHeaderEntry("Content-Length");
                if (attr.size() == 1) {
                    int contentLength = Integer.valueOf(attr.get(0));
                    if (contentLength > 0) {
                        char[] buff = new char[contentLength];
                        bfr.read(buff, 0, contentLength);
                        req.setBody((new String(buff)).getBytes("UTF-8"));
                    }
                }
            }
            req.setHeader(header);

            return req;
        }catch(SocketException e){
            try {
                bfr.close();
            } catch (IOException e1) {
                throw new TranscodingException("something went wrong closing the reader");
            }
            throw new SocketException();
        }
        catch (IOException e) {

            try {
                bfr.close();
            } catch (IOException e1) {
                throw new TranscodingException("something went wrong closing the reader");
            }
            throw new TranscodingException("something went wrong during parsing the http request");
        } catch (ElementNotFoundException e) {
            e.printStackTrace();  //TODO
            throw new TranscodingException("something went wrong during parsing the http request");
        }
    }
}
