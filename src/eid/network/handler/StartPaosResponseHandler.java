/*
 * StartPaosResponseHandler.java
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

package eid.network.handler;

import common.data.NodeNameFilter;
import common.data.XmlAccessor;
import common.exceptions.BuildException;
import common.exceptions.ElementNotFoundException;
import common.exceptions.ParsingException;
import common.network.paos.PAOSRequest;
import common.network.paos.PAOSResponse;
import common.util.EnumHelper;
import eid.network.data.NoResponseType;
import eid.network.data.ResultType;
import eid.network.data.StartPaosResponseType;
import org.w3c.dom.Element;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 23.07.12
 * Time: 00:15
 */

/**
* This class is used to process the StartPAOSResponse message
*/
public class StartPaosResponseHandler extends ProtocolRequestHandler<StartPaosResponseType, NoResponseType> {

    /**
     * Constructor
     * @param request PAOS request
     */
    public StartPaosResponseHandler(PAOSRequest request) {
        super(request, "StartPAOSResponse", null);
    }

    /**
     * Core parsing method. nothing to be done here since the message contains no content data
     * @param accessor XML accessor class
     * @return Response object -> no response therefor null
     */
    @Override
    public StartPaosResponseType extractDataImpl(XmlAccessor accessor) throws ParsingException {
        NodeNameFilter nodeNameFilter = new NodeNameFilter("Result",
                NodeNameFilter.MatchingRule.EXACT_IGNORE_NAMESPACE);
        Element resultElem = null;
        ResultType.Major majorResult;
        try {
            resultElem = accessor.requireElement(nodeNameFilter);
            nodeNameFilter.setMatchingName("ResultMajor");
            Element resultMajorElem = accessor.findElementAtCursorMatching(nodeNameFilter);
            String resultMajor = resultMajorElem.getTextContent();
            Pattern pattern = Pattern.compile("(/resultmajor#[a-zA-Z]*)");
            Matcher match = pattern.matcher(resultMajor);
            if (!match.find()) {
                throw new ParsingException("resultMajor data does not match expected format");
            }
            resultMajor = match.group(1);
            majorResult = new EnumHelper<ResultType.Major>().getEnumValue(ResultType.Major.class,resultMajor);
        } catch (ElementNotFoundException e) {
            e.printStackTrace();  //TODO
            throw new ParsingException("no result element found or it is malformed");
        }

        ResultType.Minor minorResult;
        try{
            nodeNameFilter.setMatchingName("ResultMinor");
            Element resultMinorElem = accessor.findElementAtCursorMatching(nodeNameFilter);
            String resultMinor = resultMinorElem.getTextContent();
            Pattern pattern = Pattern.compile("(/resultminor/.*)");
            Matcher match = pattern.matcher(resultMinor);
            if (!match.find()) {
                throw new ParsingException("resultMinor data does not match expected format");
            }
            resultMinor = match.group(1);

            minorResult = new EnumHelper<ResultType.Minor>().getEnumValue(ResultType.Minor.class,resultMinor);
        } catch (ElementNotFoundException ignored) {
            return new StartPaosResponseType(new ResultType(majorResult));
        }
        return new StartPaosResponseType(new ResultType(majorResult,minorResult));


    }

    /**
     * No response expected therefor this method should not be called
     * @param data response data
     * @return nothing
     * @throws BuildException
     */
    @Override
    public PAOSResponse generateResponse(NoResponseType data) throws BuildException {
        throw new NotImplementedException();
    }
}
