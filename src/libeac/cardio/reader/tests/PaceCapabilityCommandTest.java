/*
 * PaceCapabilityCommandTest.java
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

package libeac.cardio.reader.tests;

import common.cardio.ControlCommand;
import common.cardio.ICardAccess;
import common.cardio.ITerminalAccess;
import common.cardio.TerminalAccess;
import common.util.Logger;
import junit.framework.TestCase;
import libeac.cardio.reader.*;
import libeac.definitions.PACETypes;

import javax.xml.bind.DatatypeConverter;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 27.07.12
 * Time: 16:02
 */
public class PaceCapabilityCommandTest extends TestCase {
    public void testMethod() throws Exception {

        ITerminalAccess terminalAccess_ = new TerminalAccess();
        terminalAccess_.observeTerminals();
        List<String> connectedCards = terminalAccess_.getConnectedCards(1000, 30);
        if (connectedCards.size() > 0) {
            ICardAccess cardAccess_= terminalAccess_.getCardAccessor(connectedCards.get(0));
            if (cardAccess_.connect()) {
                GetFeatureCodesCommand getFeatureCodesCommand = new GetFeatureCodesCommand();
                byte[] res = cardAccess_.sendControlCommand(getFeatureCodesCommand);
                Logger.log(DatatypeConverter.printHexBinary(res));

                FeatureTable ft = new FeatureTable(res);
                ControlCommand ctlCmd = new ControlCommand(ft.getControlCodeForFeature(0x20),
                        new ReaderPaceCapabilitiesRequest().getBytes());
                ExecutePaceResponse executePaceResponse =
                        new ExecutePaceResponse(cardAccess_.sendControlCommand(ctlCmd));
                Logger.log(String.valueOf(executePaceResponse.getStatus()));
                Logger.log(DatatypeConverter.printHexBinary(executePaceResponse.getData()));
                PaceCapabilities cap = new PaceCapabilities(executePaceResponse.getData());
                assertTrue(cap.canDoPace());
                assertTrue(cap.supportsEId());

                ReaderEstablishPaceRequest repr = new ReaderEstablishPaceRequest(PACETypes.Password.PIN,null,null,null);
                ControlCommand cmd = new ControlCommand(ft.getControlCodeForFeature(0x20),repr.getBytes());
                Logger.log(DatatypeConverter.printHexBinary(cmd.getControl()));
                res = cardAccess_.sendControlCommand(cmd);
                assertNotNull(res);


            }
        }

    }
}
