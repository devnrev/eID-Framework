/*
 * CDERSetBC.java
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

import common.crypto.IDERSet;
import common.crypto.IDERSequence;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERSet;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 13.12.11
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */

/**
 * BouncyCastle adapter for IDERSet
 */
public class CDERSetBC extends CDERObjectBC<ASN1Set> implements IDERSet {

    public CDERSetBC(byte[] byaData) throws IOException {
        m_adaptedObject = (ASN1Set)DERSet.fromByteArray(byaData);
    }

    public IDERSequence[] getDERSequences() {
        IDERSequence[] derSeq=new CDERSequenceBC[m_adaptedObject.size()];
        for(int i = 0;i < m_adaptedObject.size();i++ ){
           derSeq[i] = new CDERSequenceBC((ASN1Sequence) m_adaptedObject.getObjectAt(i));
        }
        return derSeq;
    }


}
