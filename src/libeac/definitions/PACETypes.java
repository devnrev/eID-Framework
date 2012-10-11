/*
 * PACETypes.java
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

package libeac.definitions;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 12.12.11
 * Time: 00:44
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class contains types used in the PACE protocol
 */
public class PACETypes {

    /**
     * Enum conatining the different authentication types
     */
    public static enum AuthenticationType{
        PACE,
        CA,
        TA
    }

    /**
     * Enum containing the different password types
     */
    public static enum Password{
        MRZ,
        CAN,
        PIN,
        PUK


    }

    /**
     * Enum containing the different terminal roles
     */
    public static enum ERoleType{
        IS,
        AT
    }

}
