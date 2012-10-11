/*
 * AuthenticationProgressEvent.java
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

package eid.core.events;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 15.08.12
 * Time: 14:12
 */

/**
 * This class represents an event describing the current authentication progress. Besides the normal event type
 * it additionally contains information about the current protocol step.
 *
 */
public class AuthenticationProgressEvent extends BasicEIDEvent {

    /**
     * Enum with the different protocol steps
     */
    public enum AuthProgress{
        STARTED,
        TRANSMITING_DATA,
        FINISHED
    }

    protected AuthProgress authProgress_;

    /**
     * Constructor
     * @param authProgress Current progress
     */
    public AuthenticationProgressEvent(AuthProgress authProgress) {
        super(eIDEventType.AuthenticationProgressEvent);
        authProgress_ = authProgress;
    }

    /**
     * Get the authentication progress
     * @return Authentication progress
     */
    public AuthProgress getAuthProgress(){
        return authProgress_;
    }


}
