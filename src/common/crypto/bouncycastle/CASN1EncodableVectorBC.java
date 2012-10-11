/*
 * CASN1EncodableVectorBC.java
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
import common.crypto.IDEREncodable;
import common.crypto.IDERTaggedObject;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERTaggedObject;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 14.12.11
 * Time: 11:31
 * To change this template use File | Settings | File Templates.
 */
public class CASN1EncodableVectorBC implements IASN1EncodableVector {
    private ASN1EncodableVector m_obj;

    public CASN1EncodableVectorBC(ASN1EncodableVector obj){
        m_obj = obj;
    }

    public CASN1EncodableVectorBC(){
        m_obj = new ASN1EncodableVector();
    }


    @Override
    public void add(IDEREncodable obj) {
        m_obj.add((ASN1Encodable)obj.getObject());
    }

    @Override
    public IDERTaggedObject getTaggeObjectAt(int nIndex) {
        return new CDERTaggedObjectBC((DERTaggedObject)m_obj.get(nIndex));
    }


    @Override
    public int size() {
        return m_obj.size();
    }

    @Override
    public Object getObject() {
        return m_obj;
    }
}
