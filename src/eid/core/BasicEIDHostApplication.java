/*
 * BasicEIDHostApplication.java
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

package eid.core;

import common.util.Logger;
import eid.core.events.AuthenticationProgressEvent;
import eid.core.events.IeIDEvent;
import eid.hotspots.IeIDHostApplication;
import eid.hotspots.IeIDUIComponents;
import eid.ui.BasicEIDUIComponents;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 15.08.12
 * Time: 14:25
 */

/**
 * This class represents a basic host application, used when no host application has
 * registered itself to the eID Client
 */
public class BasicEIDHostApplication implements IeIDHostApplication {
    protected IeIDUIComponents uiComponents_;

    /**
     * Constructor
     */
    public BasicEIDHostApplication(){
         uiComponents_ = new BasicEIDUIComponents();
    }

    /**
     * Get the UI components
     * @return UI components
     */
    @Override
    public IeIDUIComponents getUIComponents() {
        return uiComponents_;
    }

    /**
     * Process incoming events
     * @param event The emitted event
     */
    @Override
    public void notify(IeIDEvent event) {
        switch (event.getEventType()) {
            case AuthenticationProgressEvent:
                Logger.log("AuthProgressEvent: " +
                        ((AuthenticationProgressEvent)event).getAuthProgress().name());
                break;
        }

    }
}
