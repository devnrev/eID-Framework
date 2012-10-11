/*
 * TerminalAccessTest.java
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

package common.cardio;

import common.util.Logger;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 27.07.12
 * Time: 11:47
 */
public class TerminalAccessTest extends TestCase {
    public void testTerminalAccess() throws Exception {
        TerminalAccess ta = new TerminalAccess();
        ta.observeTerminals();

        Thread.sleep(5000);
        List<String> termIds = ta.getConnectedCards();
        if (termIds.size() > 0) {
            Logger.log("card present at: " + termIds.get(0));
        }

        ICardAccess ca = ta.getCardAccessor(termIds.get(0));
        Thread.sleep(10000);
        Logger.log("connected " + String.valueOf(ca.isConnected()));


    }


}

