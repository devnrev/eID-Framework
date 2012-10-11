/*
 * PaosToHttpProcessor.java
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

package common.network.paos;

import common.exceptions.BuildException;
import common.exceptions.TranscodingException;
import common.network.http.GenericHttpRequestMessageBuilder;
import common.network.http.HttpResponse;
import common.network.http.HttpServerStreamMessageProcessor;
import common.network.messaging.IStreamMessageProcessor;
import common.util.IOutputStreamResult;
import common.util.Logger;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 02.07.12
 * Time: 17:40
 */
public class PaosToHttpProcessor implements IStreamMessageProcessor<PAOSResponse,PAOSRequest> {
    protected GenericHttpRequestMessageBuilder<IOutputStreamResult> messageBuilder_;
    protected HttpServerStreamMessageProcessor httpMessageProcessor_;



    public PaosToHttpProcessor(GenericHttpRequestMessageBuilder<IOutputStreamResult> messageBuilder){
        messageBuilder_ = messageBuilder;
        httpMessageProcessor_ = new HttpServerStreamMessageProcessor();
    }

    @Override
    public void encode(PAOSResponse message, OutputStream out) throws TranscodingException {
        try {
            messageBuilder_.initializeProduct();
            messageBuilder_.buildHeader();
            messageBuilder_.buildBody(message);
            byte[] bytes = messageBuilder_.getProduct().getBytes();
            Logger.log(bytes);
            Logger.log("");
            out.write(bytes);
        } catch (BuildException e) {
            throw new TranscodingException("error while building http request\n" + e.getMessage());
        } catch (IOException e) {
            throw new TranscodingException("error while building http request\n" + e.getMessage());
        }
    }

    @Override
    public PAOSRequest decode(InputStream message) throws TranscodingException {
        HttpResponse resp = httpMessageProcessor_.decode(message);
        Logger.log(resp.getMessage());
        if(resp.getStatus() == 200){
            return PaosMessageParser.parseRequest(resp.getBody());
        }else{
            Logger.log("fail");
        }
        throw new TranscodingException("could not decode PAOS request");

    }
}
