/*
 * CDERApplicationSpecificBC.java
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

import common.crypto.IASN1EncodableVector;
import common.crypto.IDERApplicationSpecific;
import common.crypto.IDERSequence;
import org.bouncycastle.asn1.*;


import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 14.12.11
 * Time: 11:23
 * To change this template use File | Settings | File Templates.
 */


/**
 * BouncyCastle adapter for IDERApplicationSpecific
 */
public class CDERApplicationSpecificBC extends CDERObjectBC<DERApplicationSpecific> implements IDERApplicationSpecific{

    public CDERApplicationSpecificBC(DERApplicationSpecific obj){
        m_adaptedObject = obj;
    }

    public CDERApplicationSpecificBC(byte[] byaData) throws IOException {
        m_adaptedObject = (DERApplicationSpecific)DERApplicationSpecific.fromByteArray(byaData);
    }

    public CDERApplicationSpecificBC(int nTagNo,IASN1EncodableVector vec){
        m_adaptedObject = new DERApplicationSpecific(nTagNo,(ASN1EncodableVector)vec.getObject());
    }

    @Override
    public IDERSequence getDERSequence() throws IOException {
        return new CDERSequenceBC((ASN1Sequence)m_adaptedObject.getObject(DERTags.SEQUENCE));
    }

    @Override
    public byte[] getContents() {
        return m_adaptedObject.getContents();
    }
}


