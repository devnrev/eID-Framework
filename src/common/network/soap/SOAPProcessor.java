/*
 * SOAPProcessor.java
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

package common.network.soap;
import common.network.messaging.IStreamMessageProcessor;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 02.06.12
 * Time: 11:52
 */

public class SOAPProcessor implements IStreamMessageProcessor<SOAPMessage,SOAPMessage> {


    public SOAPProcessor(){

    }



    @Override
    public void encode(SOAPMessage message, OutputStream out) {
        //TODO
    }

    @Override
    public SOAPMessage decode(InputStream message) {
        MimeHeaders mimeHeaders = new MimeHeaders();
        mimeHeaders.addHeader("Content-Type","text/xml; charset=UTF-8");
        try {
            MessageFactory msgFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = msgFactory.createMessage(mimeHeaders, message);
            return soapMessage;
        } catch (SOAPException e) {
            throw new RuntimeException("could not decode soap message");
        } catch (IOException e) {
            throw new RuntimeException("could not decode soap message");
        }
    }
}




