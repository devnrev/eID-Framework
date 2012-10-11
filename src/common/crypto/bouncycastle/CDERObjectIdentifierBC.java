/*
 * CDERObjectIdentifierBC.java
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

package common.crypto.bouncycastle;

import common.crypto.IDERObjectIdentifier;
import org.bouncycastle.asn1.DERObjectIdentifier;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 12.12.11
 * Time: 12:02
 * To change this template use File | Settings | File Templates.
 */
public class CDERObjectIdentifierBC extends CDERObjectBC<DERObjectIdentifier> implements IDERObjectIdentifier{

    public CDERObjectIdentifierBC(DERObjectIdentifier obj){
        m_adaptedObject = obj;
    }

    public CDERObjectIdentifierBC(String szVal){
        m_adaptedObject = new DERObjectIdentifier(szVal);
    }

    public CDERObjectIdentifierBC(byte[] bytes) throws IOException {
        m_adaptedObject = (DERObjectIdentifier)DERObjectIdentifier.fromByteArray(bytes);

    }

    public String getId(){
        return m_adaptedObject.getId();
    }

}
