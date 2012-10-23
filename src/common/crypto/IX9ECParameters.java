/*
 * IX9ECParameters.java
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

package common.crypto;

import java.math.BigInteger;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 19.12.11
 * Time: 16:40
 * To change this template use File | Settings | File Templates.
 */

/**
 * Interface defining X9 elliptic curve parameters
 */
public interface IX9ECParameters{
    IECPoint getG();
    BigInteger getN();
    IECCurveFP getCurveFP();


}
