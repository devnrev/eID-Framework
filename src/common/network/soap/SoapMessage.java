/*
 * SOAPMessage.java
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

import common.data.NodeNameFilter;
import common.data.XmlAccessor;
import common.exceptions.BuildException;
import common.exceptions.ElementNotFoundException;
import common.util.IOutputStreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.soap.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 04.07.12
 * Time: 13:57
 */

/**
 * Abstract class which represents a basic SOAP message
 */
public abstract class SoapMessage implements IOutputStreamResult{
    protected SOAPMessage message_;

    public SoapMessage(){

    }


    public void setMessage(SOAPMessage message){
        message_ = message;
    }


    public void writeTo(OutputStream out) throws BuildException {
        try {
            message_.writeTo(out);
        } catch (SOAPException e) {
            throw new BuildException("could not write soap message");
        } catch (IOException e) {
            throw new BuildException("error with output stream");
        }
    }

    public SOAPMessage getMessage(){
        return message_;
    }

    public Document getBodyDocument(){
        SOAPBody body =null;
        try {
            body = message_.getSOAPBody();
        } catch (SOAPException e) {
            e.printStackTrace();  //TODO
        }
        assert (body != null);
        return body.getOwnerDocument();
    }


    @Override
    public String toString() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            message_.writeTo(out);
            return out.toString();
        } catch (SOAPException e) {
            return "";
        } catch (IOException e) {
            return "";
        }

    }

    public String getMessageId() {
        SOAPHeader header = null;
        try {
          header = message_.getSOAPHeader();
        } catch (SOAPException e) {
            e.printStackTrace();  //TODO
            return "";
        }
        XmlAccessor accessor = new XmlAccessor(header.getOwnerDocument());
        NodeNameFilter nodeNameFilter = new NodeNameFilter("MessageID",
                NodeNameFilter.MatchingRule.EXACT_IGNORE_NAMESPACE);
        Element messageId = null;
        try {
            messageId = accessor.requireElement(nodeNameFilter);
            return messageId.getTextContent();
        } catch (ElementNotFoundException ignored) {

        }

        return "";
    }
}
