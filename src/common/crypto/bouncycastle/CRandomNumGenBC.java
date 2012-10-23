/*
 * CRandomNumGenBC.java
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
import common.crypto.IRandomNumGen;
import org.bouncycastle.crypto.prng.VMPCRandomGenerator;


/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 16.12.11
 * Time: 11:42
 * To change this template use File | Settings | File Templates.
 */

/**
 * BouncyCastle adapter for IRandomNumGen
 */
public class CRandomNumGenBC extends CAdapterObj<VMPCRandomGenerator> implements IRandomNumGen{

    public CRandomNumGenBC(VMPCRandomGenerator obj){
        m_adaptedObject = obj;
    }

    public CRandomNumGenBC(){
        m_adaptedObject = new VMPCRandomGenerator();
    }

    @Override
    public void nextBytes(byte[] byaData) {
        m_adaptedObject.nextBytes(byaData);
    }
}
