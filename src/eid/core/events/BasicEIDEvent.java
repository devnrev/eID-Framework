/*
 * BasicEIDEvent.java
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
 * Time: 14:17
 */

/**
 *  This class implements a basic event
 */
public class BasicEIDEvent implements IeIDEvent {
    protected eIDEventType eventType_;

    /**
     * Constructor
     * @param eventType Event type
     */
    public BasicEIDEvent(eIDEventType eventType){
        eventType_ = eventType;
    }

    /**
     * Get the Event type
     * @return Event type
     */
    @Override
    public eIDEventType getEventType() {
        return eventType_;
    }
}
