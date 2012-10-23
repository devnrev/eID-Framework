/*
 * CDERSequenceBC.java
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

import common.crypto.*;
import org.bouncycastle.asn1.*;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 13.12.11
 * Time: 12:22
 * To change this template use File | Settings | File Templates.
 */

/**
 * BouncyCastle adapter for IDERSequence
 */
public class CDERSequenceBC extends CDERObjectBC<ASN1Sequence> implements IDERSequence {


    public CDERSequenceBC(ASN1Sequence obj){
        m_adaptedObject = obj;
    }

    public  CDERSequenceBC(IDERObjectIdentifier pData){
        m_adaptedObject = ASN1Sequence.getInstance(((DERObjectIdentifier)pData.getObject()).toASN1Primitive());
    }

    public CDERSequenceBC(byte[] byaData) throws IOException {
        m_adaptedObject = (ASN1Sequence)ASN1Sequence.fromByteArray(byaData);
    }

    protected ASN1Encodable getObjectAt(int nIndex){
       return m_adaptedObject.getObjectAt(nIndex);
    }


    @Override
    public IDERObjectIdentifier getDERObjectIdentifierAt(int nIndex) {
        return new CDERObjectIdentifierBC((DERObjectIdentifier)getObjectAt(nIndex));
    }

    @Override
    public IDERInteger getDERIntegerAt(int nIndex) {
        return new CDERIntegerBC((DERInteger)getObjectAt(nIndex));
    }

    @Override
    public IDERApplicationSpecific getDERApplicationSpecificAt(int nIndex) {
        return new CDERApplicationSpecificBC((DERApplicationSpecific)getObjectAt(nIndex));
    }

    @Override
    public IDERSequence getDERSequenceAt(int nIndex) {
        return new CDERSequenceBC((ASN1Sequence)getObjectAt(nIndex));
    }

    @Override
    public IDERTaggedObject getDERTaggedObjectAt(int nIndex) {
        return new CDERTaggedObjectBC((DERTaggedObject)getObjectAt(nIndex));
    }

    @Override
    public int size() {
        return m_adaptedObject.size();
    }
}
