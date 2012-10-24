/*
 * HttpServerStreamMessageProcessor.java
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
import common.util.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 11.07.12
 * Time: 15:02
 */

/**
 * HTTP message processor used by a client. It processes incoming HTTP responses and encodes outgoing HTTP requests
 */
public class HttpServerStreamMessageProcessor implements IStreamMessageProcessor<HttpRequest, HttpResponse> {

    /**
     * encode HTTP request and write it to the given OutputStream
     * @param message HTTP request
     * @param out OutputStream
     * @throws TranscodingException
     */
    @Override
    public void encode(HttpRequest message, OutputStream out) throws TranscodingException {
        byte[] bytes = message.getBytes();
        Logger.log(bytes);
        Logger.log("");
        try {
            out.write(bytes);
        }catch (IOException e) {
            throw new TranscodingException("error while sending http request\n" + e.getMessage());
        }
    }

    /**
     * Read InputStream and decode it into an HTTP response object
     * @param message
     * @return
     * @throws TranscodingException
     */
    @Override
    public HttpResponse decode(InputStream message) throws TranscodingException {
        InputStreamReader reader = new InputStreamReader(message);
        BufferedReader bfr = new BufferedReader(reader);
        try {
            String data = bfr.readLine();

            if (data == null || data.equals("")) {
                return null;
            }
            Pattern pskPattern = Pattern.compile("(HTTP/\\d.\\d)\\s(\\d{3})\\s(.*)");
            Matcher match = pskPattern.matcher(data);

            if (!match.find()) {
                throw new TranscodingException("initial line of http response has not expected format");
            }

            String version = match.group(1);
            int status = Integer.valueOf(match.group(2));
            String reason = match.group(3);

            HttpMessage.HttpVersion vertsion_t = (new EnumHelper<HttpMessage.HttpVersion>().
                    getEnumValue(HttpMessage.HttpVersion.class, version));
            HttpResponse resp = new HttpResponse(vertsion_t,
                                                 status,
                                                 reason);

            HttpHeader header = new HttpHeader();
            String body = "";

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
            List<String> attr = header.getHeaderEntry("Content-Length");
            if(attr == null){
                throw new TranscodingException("no content in response");
            }
            if (attr.size() == 1) {
                int contentLength = Integer.valueOf(attr.get(0));
                if (contentLength > 0) {
                    char[] buff = new char[contentLength];
                    bfr.read(buff, 0, contentLength);
                    resp.setBody((new String(buff)).getBytes("UTF-8"));
                }

            }
            resp.setHeader(header);
            Logger.log(resp.toString() );
            return resp;
        } catch (IOException e) {
            e.printStackTrace();  //TODO
            try {
                bfr.close();
            } catch (IOException e1) {
                e1.printStackTrace();  //TODO
            }
            throw new TranscodingException("something went wrong during parsing the http response");
        } catch (ElementNotFoundException e) {
            e.printStackTrace();  //TODO
            throw new TranscodingException("something went wrong during parsing the http response");
        }
    }
}
