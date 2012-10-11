/*
 * EAC1OutputDataEncoderTest.java
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

package eid.network.messages.transcoding.tests;

import common.network.paos.PAOSResponse;
import common.network.soap.SoapMessage;
import eid.network.data.EAC1OutputType;
import eid.network.messages.ProtocolResponseBodyHelper;
import eid.network.messages.transcoding.EAC1OutputDataEncoder;
import junit.framework.TestCase;

import javax.xml.soap.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 21.07.12
 * Time: 15:48
 */
public class EAC1OutputDataEncoderTest extends TestCase {
    public void testEncode() throws Exception {
        EAC1OutputType outputType = new EAC1OutputType();
        outputType.setChat(new byte[]{0x70,0x20,0x33});
        outputType.setIdPICC(new byte[]{0x71, 0x20, 0x33});
        outputType.setRetryCounter(4);
        outputType.setEfCardAccess(new byte[]{0x72,0x20,0x33});
        List<byte[]> carList = new ArrayList<byte[]>();
        carList.add(new byte[]{0x73, 0x20, 0x33});
        outputType.setCar(carList);
        EAC1OutputDataEncoder enc = new EAC1OutputDataEncoder();

        MessageFactory mf = MessageFactory.newInstance();

        SOAPMessage message = mf.createMessage();
        SOAPBody body = message.getSOAPBody();
        SOAPElement elem = body.addChildElement(ProtocolResponseBodyHelper.buildContentTag("DIDAuthenticateResponse"));

        enc.encodeAndAdd(outputType,elem);
        String testVec = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<SOAP-ENV:Header/><SOAP-ENV:Body><iso:DIDAuthenticateResponse xmlns:SOAP-ENC=" +
                "\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:XAdES=\"http://uri.etsi.org/01903/v1.3.2#\"" +
                " xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:dss=\"urn:oasis:names:tc:dss:1.0:core:schema\"" +
                " xmlns:dssades=\"urn:oasis:names:tc:dss:1.0:profiles:AdES:schema#\" xmlns:dsse=" +
                "\"urn:oasis:names:tc:dss-x:1.0:profiles:encryption:schema#\" xmlns:dssx=" +
                "\"urn:oasis:names:tc:dss-x:1.0:profiles:SignaturePolicy:schema#\" xmlns:ec=" +
                "\"http://www.bsi.bund.de/ecard/api/1.1\" xmlns:ecdsa=\"http://www.w3.org/2001/04/xmldsig-more#\"" +
                " xmlns:ers=\"http://www.setcce.org/schemas/ers\" xmlns:iso=\"urn:iso:std:iso-iec:24727:tech:schema\"" +
                " xmlns:olsc=\"http://www.openlimit.com/ecard/api/ext/acbc\" xmlns:saml=" +
                "\"urn:oasis:names:tc:SAML:1.0:assertion\" xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\"" +
                " xmlns:tsl=\"http://uri.etsi.org/02231/v2#\" xmlns:vr=" +
                "\"urn:oasis:names:tc:dss-x:1.0:profiles:verificationreport:schema#\" xmlns:xenc=" +
                "\"http://www.w3.org/2001/04/xmlenc#\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
        String testVec1 = testVec + "<iso:AuthenticationProtocolData " +
        "xsi:type=\"iso:EAC1OutputType\">" +
                "<iso:RetryCounter>4</iso:RetryCounter><iso:CertificateHolderAuthorizationTemplate>" +
                "702033</iso:CertificateHolderAuthorizationTemplate><iso:CertificationAuthorityReference>" +
                "732033</iso:CertificationAuthorityReference><iso:EFCardAccess>722033</iso:EFCardAccess>" +
                "<iso:IDPICC>712033</iso:IDPICC></iso:AuthenticationProtocolData></iso:DIDAuthenticateResponse>" +
                "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
        SoapMessage msg = new PAOSResponse();
        msg.setMessage(message);
        assertEquals(testVec1, msg.toString());
        outputType.setChallenge(new byte[]{0x74, 0x20, 0x33});
        String testVec2 = testVec + "<iso:AuthenticationProtocolData " +
                "xsi:type=\"iso:EAC1OutputType\">" +
                "<iso:RetryCounter>4</iso:RetryCounter><iso:CertificateHolderAuthorizationTemplate>" +
                "702033</iso:CertificateHolderAuthorizationTemplate><iso:CertificationAuthorityReference>" +
                "732033</iso:CertificationAuthorityReference><iso:EFCardAccess>722033</iso:EFCardAccess>" +
                "<iso:IDPICC>712033</iso:IDPICC><iso:Challenge>742033</iso:Challenge></iso:AuthenticationProtocolData></iso:DIDAuthenticateResponse>" +
                "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
        message = mf.createMessage();
        body = message.getSOAPBody();
        elem = body.addChildElement(ProtocolResponseBodyHelper.buildContentTag("DIDAuthenticateResponse"));
        enc.encodeAndAdd(outputType,elem);
        msg.setMessage(message);
        assertEquals(testVec2,msg.toString());

    }
}
