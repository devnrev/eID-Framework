/*
 * InitFrameworkResponseMessageBuilder.java
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
import eid.network.data.*;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 05.07.12
 * Time: 10:46
 */

/**
 * Message builder for the InitializeFramework response message
 */
public class InitFrameworkResponseMessageBuilder
        extends ProtocolResponseMessageBuilder<InitializeFrameworkResponseType> {

    /**
     * Constructor
     */
    public InitFrameworkResponseMessageBuilder(){
        super("InitializeFrameworkResponse", ProtocolResponseBodyHelper.EC_NS, ProtocolResponseBodyHelper.EC_NS_DEC);
    }

    /**
     * Specific implementation of the content insertion
     * @param payload Content data
     * @param content Body element
     * @throws BuildException
     * @throws SOAPException
     */
    @Override
    protected void buildBodyContent(InitializeFrameworkResponseType payload, SOAPElement content)
            throws BuildException,SOAPException {

            SOAPElement version = content.addChildElement("Version", ProtocolResponseBodyHelper.EC_NS);
            SOAPElement majorVers = version.addChildElement("Major", ProtocolResponseBodyHelper.EC_NS);
            majorVers.setValue(String.valueOf(payload.getVersion().getVersionMajor()));
            SOAPElement minorVers = version.addChildElement("Minor", ProtocolResponseBodyHelper.EC_NS);
            minorVers.setValue(String.valueOf(payload.getVersion().getVersionMinor()));
            SOAPElement subMinorVers = version.addChildElement("SubMinor", ProtocolResponseBodyHelper.EC_NS);
            subMinorVers.setValue(String.valueOf(payload.getVersion().getVersionSubMinor()));
    }
}
