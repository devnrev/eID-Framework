/*
 * IAuthProtocolDataParser.java
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

package eid.network.messages.parser;

import common.data.XmlAccessor;
import common.exceptions.ParsingException;
import eid.network.data.AuthenticationProtocolDataType;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 21.07.12
 * Time: 23:43
 */

/**
 * Class which can parse authentication protocol data
 * @param <T> Content data type
 */
public abstract class AuthProtocolDataParser<T extends AuthenticationProtocolDataType>
        implements IMessageParser<T> {
    protected String dataType_;

    /**
     * Constructor
     * @param dataType Data type as string
     */
    protected AuthProtocolDataParser(String dataType){
        dataType_ = dataType;
    }

    /**
     * Check if the message can be processed by the current parser.
     * @param accessor XML accessor
     * @return true if the parser can process it
     * @throws ParsingException
     */
    public boolean checkDataType(XmlAccessor accessor) throws ParsingException {
        Map<String, String> attributeMap = accessor.getAttributesAtCursor();
        if (attributeMap == null || attributeMap.size() == 0) {
            throw new ParsingException("could not find AuthenticateProtocol attributes");
        }
        String type = attributeMap.get("xsi:type");
        if (type == null) {
            throw new ParsingException("could not find AuthenticateProtocol content type");
        }
        Pattern pattern = Pattern.compile("(.*:)?(.*)");
        Matcher match = pattern.matcher(type);
        if (!match.find()) {
            throw new ParsingException("could not find AuthenticateProtocol content type");
        }
        return match.group(2).equals(dataType_);
    }
}
