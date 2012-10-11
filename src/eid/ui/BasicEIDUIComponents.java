/*
 * BasicEIDUIComponents.java
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

package eid.ui;

import eid.hotspots.IeIDUIComponents;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 15.08.12
 * Time: 14:27
 */

/**
 * This class represents a basic implementation of the used UI components. This class is used if no host application
 * is registered to the eIDClient
 */
public class BasicEIDUIComponents implements IeIDUIComponents {

    /**
     * Create a simple dialog to ask the user for the eID password
     * @return eID password
     */
    @Override
    public String getPassword() {
        PinDialog dialog = PinDialog.showDialog();
        if (dialog.getResult() != PinDialog.DialogResult.OK) {
            return "";
        }
        return new String(dialog.getPin());
    }
}
