/*
 * PaosMessageParser.java
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

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 18.07.12
 * Time: 14:01
 */

/**
 * PAOS message parser which decodes a byte array into a PAOS request
 */
public class PaosMessageParser {
    public static PAOSRequest parseRequest(byte[] data) throws TranscodingException {
        MessageFactory factory = null;
        try {
            factory = MessageFactory.newInstance();
            PAOSRequest request = new PAOSRequest();
            SOAPMessage msg = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(data));
            request.setMessage(msg);
            return request;
        } catch (SOAPException e) {
            e.printStackTrace();  //TODO
        } catch (BuildException e) {
            e.printStackTrace();  //TODO
        } catch (IOException e) {
            e.printStackTrace();  //TODO
        }
        throw new TranscodingException("could not parse paos request");
    }

    public static PAOSResponse parseResponse(byte[] data){

        return null; //TODO
    }
}
