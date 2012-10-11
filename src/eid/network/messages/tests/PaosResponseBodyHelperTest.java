/*
 * PaosResponseBodyHelperTest.java
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

package eid.network.messages.tests;

import common.util.Logger;
import eid.network.messages.ProtocolResponseBodyHelper;
import junit.framework.TestCase;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 05.07.12
 * Time: 13:31
 */
public class PaosResponseBodyHelperTest extends TestCase {
    private final String resVec[]={
                    "<iso:StartPAOS ",
                    " xmlns:ecdsa=\"http://www.w3.org/2001/04/xmldsig-more#\"",
                    " xmlns:xenc=\"http://www.w3.org/2001/04/xmlenc#\"" ,
                    " xmlns:iso=\"urn:iso:std:iso-iec:24727:tech:schema\"" ,
                    " xmlns:saml=\"urn:oasis:names:tc:SAML:1.0:assertion\"" ,
                    " xmlns:vr=\"urn:oasis:names:tc:dss-x:1.0:profiles:verificationreport:schema#\"" ,
                    " xmlns:dss=\"urn:oasis:names:tc:dss:1.0:core:schema\"" ,
                    " xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\"" ,
                    " xmlns:dsse=\"urn:oasis:names:tc:dss-x:1.0:profiles:encryption:schema#\"" ,
                    " xmlns:ec=\"http://www.bsi.bund.de/ecard/api/1.1\"" ,
                    " xmlns:tsl=\"http://uri.etsi.org/02231/v2#\"" ,
                    " xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"" ,
                    " xmlns:XAdES=\"http://uri.etsi.org/01903/v1.3.2#\"" ,
                    " xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\"" ,
                    " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" ,
                    " xmlns:ers=\"http://www.setcce.org/schemas/ers\"" ,
                    " xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"" ,
                    " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" ,
                    "</iso:StartPAOS>"};

    public void testNamespaces() throws Exception {
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();
        SOAPElement elem = ProtocolResponseBodyHelper.buildContentTag("StartPAOS");
        elem.setValue("test");
        msg.getSOAPBody().addChildElement(elem);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        msg.writeTo(out);
        String str = out.toString();
        Logger.log(str);
        for(String res: resVec ){
            boolean found = str.contains(res);
            Logger.log(res+" found:"+found);
            assertTrue(found);
        }


    }
}
