/*
 * CECFielElementBC.java
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
import common.crypto.IECFieldElement;
import org.bouncycastle.math.ec.ECFieldElement;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 19.12.11
 * Time: 15:47
 * To change this template use File | Settings | File Templates.
 */
public class CECFielElementBC extends CAdapterObj<ECFieldElement> implements IECFieldElement{
    public CECFielElementBC(ECFieldElement obj){
        m_adaptedObject = obj;
    }


}
