/*
 * BodyNamespaceBuilder.java
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

package eid.network.messages;

import common.exceptions.BuildException;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 05.07.12
 * Time: 13:15
 */

/**
 * Helper class which generates the namespace definitions for the PAOS messages
 */
public class ProtocolResponseBodyHelper {
    public static final String ECDSA_NS = "ecdsa";
    public static final String ECDSA_NS_DEC = "http://www.w3.org/2001/04/xmldsig-more#";
    public static final String OLSC_NS = "olsc";
    public static final String OLSC_NS_DEC = "http://www.openlimit.com/ecard/api/ext/acbc";
    public static final String XENC_NS = "xenc";
    public static final String XENC_NS_DEC = "http://www.w3.org/2001/04/xmlenc#";
    public static final String ISO_NS = "iso";
    public static final String ISO_NS_DEC = "urn:iso:std:iso-iec:24727:tech:schema";
    public static final String SAML_NS = "saml";
    public static final String SAML_NS_DEC = "urn:oasis:names:tc:SAML:1.0:assertion";
    public static final String VR_NS = "vr";
    public static final String VR_NS_DEC = "urn:oasis:names:tc:dss-x:1.0:profiles:verificationreport:schema#";
    public static final String DSS_NS = "dss";
    public static final String DSS_NS_DEC = "urn:oasis:names:tc:dss:1.0:core:schema";
    public static final String SOAP_ENC_NS = "SOAP-ENC";
    public static final String SOAP_ENC_NS_DEC = "http://schemas.xmlsoap.org/soap/encoding/";
    public static final String DSSE_NS = "dsse";
    public static final String DSSE_NS_DEC = "urn:oasis:names:tc:dss-x:1.0:profiles:encryption:schema#";
    public static final String EC_NS = "ec";
    public static final String EC_NS_DEC = "http://www.bsi.bund.de/ecard/api/1.1";
    public static final String TSL_NS = "tsl";
    public static final String TSL_NS_DEC = "http://uri.etsi.org/02231/v2#";
    public static final String SOAP_ENV_NS = "SOAP-ENV";
    public static final String SOAP_EVNV_NS_DEC = "http://schemas.xmlsoap.org/soap/envelope/";
    public static final String XADES_NS = "XAdES";
    public static final String XADES_NS_DEC = "http://uri.etsi.org/01903/v1.3.2#";
    public static final String SAML12_NS = "saml2";
    public static final String SAML12_NS_DEC = "urn:oasis:names:tc:SAML:2.0:assertion";
    public static final String DSSADES_NS = "dssades";
    public static final String DSSADES_NS_DEC = "urn:oasis:names:tc:dss:1.0:profiles:AdES:schema#";
    public static final String XSD_NS = "xsd";
    public static final String XSD_NS_DEC = "http://www.w3.org/2001/XMLSchema";
    public static final String DSSX_NS = "dssx";
    public static final String DSSX_NS_DEC = "urn:oasis:names:tc:dss-x:1.0:profiles:SignaturePolicy:schema#";
    public static final String ERS_NS = "ers";
    public static final String ERS_NS_DEC = "http://www.setcce.org/schemas/ers";
    public static final String DS_NS = "ds";
    public static final String DS_NS_DEC = "http://www.w3.org/2000/09/xmldsig#";
    public static final String XSI_NS = "xsi";
    public static final String XSI_NS_DEC = "http://www.w3.org/2001/XMLSchema-instance";

    /**
     * Create a SOAP element configured with the namespaces, pre configured belongingto the ISO namespace
     * @param tag Element name
     * @return Element
     * @throws BuildException
     */
    public static SOAPElement buildContentTag(String tag) throws BuildException {
         return buildContentTag(tag,ISO_NS,ISO_NS_DEC);
    }

    /**
     * Build the SOAP Element with the namespaces
     * @param tag Tag name
     * @param ns Namespace of the tag
     * @param nsDefinition Namespace definition
     * @return Element
     * @throws BuildException
     */
    public static SOAPElement buildContentTag(String tag,String ns,String nsDefinition) throws BuildException {
        try {
            SOAPFactory sf = SOAPFactory.newInstance();
            SOAPElement elem = sf.createElement(tag,ns,nsDefinition);
            elem.addNamespaceDeclaration(ECDSA_NS,ECDSA_NS_DEC);
            elem.addNamespaceDeclaration(OLSC_NS,OLSC_NS_DEC);
            elem.addNamespaceDeclaration(XENC_NS,XENC_NS_DEC);
            elem.addNamespaceDeclaration(SAML_NS,SAML_NS_DEC);
            elem.addNamespaceDeclaration(VR_NS,VR_NS_DEC);
            elem.addNamespaceDeclaration(DSS_NS,DSS_NS_DEC);
            elem.addNamespaceDeclaration(SOAP_ENC_NS,SOAP_ENC_NS_DEC);
            elem.addNamespaceDeclaration(DSSE_NS,DSSE_NS_DEC);
            elem.addNamespaceDeclaration(EC_NS,EC_NS_DEC);
            elem.addNamespaceDeclaration(TSL_NS,TSL_NS_DEC);
            elem.addNamespaceDeclaration(SOAP_ENV_NS,SOAP_EVNV_NS_DEC);
            elem.addNamespaceDeclaration(XADES_NS,XADES_NS_DEC);
            elem.addNamespaceDeclaration(SAML12_NS,SAML12_NS_DEC);
            elem.addNamespaceDeclaration(DSSADES_NS,DSSADES_NS_DEC);
            elem.addNamespaceDeclaration(XSD_NS,XSD_NS_DEC);
            elem.addNamespaceDeclaration(DSSX_NS,DSSX_NS_DEC);
            elem.addNamespaceDeclaration(ERS_NS,ERS_NS_DEC);
            elem.addNamespaceDeclaration(DS_NS,DS_NS_DEC);
            elem.addNamespaceDeclaration(XSI_NS, XSI_NS_DEC);
            return elem;
        } catch (SOAPException e) {
            throw new BuildException("could not generate content tag");
        }
    }
}
