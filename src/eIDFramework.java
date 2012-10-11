/*
 * eIDFramework.java
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

import common.util.Logger;
import eid.core.eIDClient;

import java.io.Console;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 11.10.12
 * Time: 17:02
 */

/**
 * eID framework main method
 * Starts a local http server to receive eID authentication requests
 */
public class eIDFramework {
    public static void main(String[] args) {
        eIDClient client = new eIDClient();
        Logger.log("start local server on port 24727");
        if(client.startListener()){
        Logger.log("listening...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        } else{
            Logger.log("could not start server");
        }
        Logger.log("stopping...");
        client.waitAndStop();
        Logger.log("end");

    }
}
