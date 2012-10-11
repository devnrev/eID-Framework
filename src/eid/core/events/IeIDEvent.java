/*
 * IEIDEvent.java
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
 * Time: 14:08
 */

/**
 * Interface describing an event to inform the host application about the eID framework status
 */
public interface IeIDEvent {

    /**
     * Get the event type
     * @return Event type
     */
    eIDEventType getEventType();
}
