/*
 * SPACESecurityData.java
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

package libeac;

import common.crypto.IX9ECParameters;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 02.01.12
 * Time: 00:55
 * To change this template use File | Settings | File Templates.
 */

/**
 * This value class contains the PACE security parameters after the protocol run successfully
 */
public class PACESecurityData {
    public byte[] KMac;
    public byte[] KEnc;
    public byte[] Car;
    public IX9ECParameters CurveSpec;
    public byte[] IdPICC;
}
