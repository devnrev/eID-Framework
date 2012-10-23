/*
 * CDERObjectBC.java
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

import common.CAdapterObj;
import common.crypto.IDEREncodable;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1Primitive;



import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 13.12.11
 * Time: 20:15
 * To change this template use File | Settings | File Templates.
 */

/**
 * BouncyCastle adapter for IDEREncodable. Implements a basic object which supports DER encoding
 */
public class CDERObjectBC<T extends ASN1Primitive> extends CAdapterObj<T> implements IDEREncodable {

    @Override
    public byte[] getEncoded() throws IOException {
        return m_adaptedObject.getEncoded();
    }

    @Override
    public byte[] getDEREncoded(){
        try {
            return m_adaptedObject.getEncoded(ASN1Encoding.DER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString(){
        return  m_adaptedObject.toString();
    }
}
