/*
 * CPACEInfos.java
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

import common.crypto.IDERObjectIdentifier;
import common.crypto.IDERSequence;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 13.12.11
 * Time: 21:27
 * To change this template use File | Settings | File Templates.
 */

/**
 * Class which contains general information about the PACE protocol
 */
public class PACEInfos {
    private IDERObjectIdentifier protocol_;
    private int version_;
    private int parameterId_;

    /**
     * Constructor
     * @param derProtocol Protocol identifier
     * @param nVersion Version
     * @param nParamId Curve parameter id
     */
    public PACEInfos(IDERObjectIdentifier derProtocol, int nVersion, int nParamId) {
        this.protocol_ = derProtocol;
        this.version_ = nVersion;
        this.parameterId_ = nParamId;
    }

    /**
     * Constructor
     * @param derSeq Information sequence
     */
    public PACEInfos(IDERSequence derSeq){
        protocol_ = derSeq.getDERObjectIdentifierAt(0);
        version_ = derSeq.getDERIntegerAt(1).getValue().intValue();
        if(derSeq.size()>2){
            parameterId_ = derSeq.getDERIntegerAt(2).getValue().intValue();
        }
    }

    /**
     * Get the protocol identifier
     * @return Protocol identifier
     */
    public IDERObjectIdentifier getProtocol() {
        return protocol_;
    }

    /**
     * Get the protocol version
     * @return Protocol version
     */
    public int getVersion() {
        return version_;
    }

    /**
     * Get the curve parameter id
     * @return Curve parameter id
     */
    public int getParameterId() {
        return parameterId_;
    }

}
