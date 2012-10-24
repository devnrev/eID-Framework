/*
 * IMessageProcessor.java
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

package common.network.messaging;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 20.06.12
 * Time: 15:09
 */

/**
 * Generic interface which defines methods to encode data into a byte array and decode data from a byte array
 * @param <P> Data type of the outgoing message
 * @param <R> Data type of the incoming message
 */
public interface IByteMessageProcessor<P,R>{
    byte[] encode(P message);
    R decode(byte[] message);
}

