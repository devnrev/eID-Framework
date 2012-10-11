/*
 * CDERTaggedObjectBC.java
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

import common.crypto.IDEREncodable;
import common.crypto.IDEROctedString;
import common.crypto.IDERTaggedObject;
import org.bouncycastle.asn1.*;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 12.12.11
 * Time: 11:42
 * To change this template use File | Settings | File Templates.
 */
public class CDERTaggedObjectBC extends CDERObjectBC<DERTaggedObject> implements IDERTaggedObject {

    public CDERTaggedObjectBC(DERTaggedObject obj){
        m_adaptedObject = obj;
    }

    public CDERTaggedObjectBC(boolean explicit, int tagNo, IDEREncodable obj){
        m_adaptedObject = new DERTaggedObject(explicit,tagNo,(ASN1Encodable)obj.getObject());
    }

    @Override
    public IDEROctedString parseAsOctectString() {
        return new CDEROctetStringBC((DEROctetString) m_adaptedObject.getObjectParser(DERTags.OCTET_STRING,false));
    }

    @Override
    public int getTagNo() {
        return m_adaptedObject.getTagNo();
    }
}
