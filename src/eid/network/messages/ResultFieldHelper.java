/*
 * ResultMessageHelper.java
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

import eid.network.data.ResultType;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 21.07.12
 * Time: 17:27
 */

/**
 * Helper class to create the result field used in the PAOS responses
 */
public final class ResultFieldHelper {

    /**
     * Constructor
     */
    private ResultFieldHelper() {

    }

    /**
     * Add the result field with a major result code to the given element
     * @param parent SOAP Element
     * @param resultCode Result code
     * @return Element
     * @throws SOAPException
     */
    private static SOAPElement buildResultTag(SOAPElement parent, String resultCode) throws SOAPException {
        SOAPElement resultElem = parent.addChildElement("Result", ProtocolResponseBodyHelper.DSS_NS);
        SOAPElement resultMajor = resultElem.addChildElement("ResultMajor", ProtocolResponseBodyHelper.DSS_NS);
        resultMajor.setValue(resultCode);
        return resultElem;
    }

    /**
     * Add the result to an element depending on the given result data
     * @param parent Element
     * @param resultData Result data
     * @throws SOAPException
     */
    public static void appendResult(SOAPElement parent, ResultType resultData) throws SOAPException {
        ResultType.Major majorResult = resultData.getResultMajor();
        SOAPElement resultElem = buildResultTag(parent,  majorResult.toString());

        if(majorResult != ResultType.Major.OK){
            SOAPElement resultMinor = resultElem.addChildElement("ResultMinor", ProtocolResponseBodyHelper.DSS_NS);
            resultMinor.setValue(resultData.getResultMinor().toString());
            String msg = resultData.getMessage();
            if (!msg.equals("")) {
                SOAPElement messageElem = resultElem.addChildElement("ResultMessage", ProtocolResponseBodyHelper.DSS_NS);
                messageElem.setValue(msg);
            }
        }
    }

}
