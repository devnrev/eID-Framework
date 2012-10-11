/*
 * CAPDUResponse.java
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

package common.cardio;

import javax.xml.bind.DatatypeConverter;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 11.12.11
 * Time: 22:30
 * To change this template use File | Settings | File Templates.
 */
public class CAPDUResponse{
    private int m_bySWOne;
    private int m_bySWTwo;
    private byte[] m_byaData;


    public CAPDUResponse(int bySWOne,int bySWTwo){
        m_bySWOne = bySWOne;
        m_bySWTwo = bySWTwo;

    }

    public CAPDUResponse(byte[] byaData,int bySWOne,int bySWTwo){
        m_bySWOne = bySWOne;
        m_bySWTwo = bySWTwo;
        m_byaData = byaData;

    }

    public int getSWOne(){
        return m_bySWOne;
    }

    public int getSWTwo(){
        return m_bySWTwo;
    }
    
    public int getSW(){
        int nVal = m_bySWOne<<8;
        nVal |= m_bySWTwo;
        return nVal;
    }

    public void setData(byte[] byaData){
        m_byaData = byaData;
    }

    public byte[] getData(){
        if(m_byaData!=null){
            return m_byaData;
        }
        throw new NullPointerException("ADPU response does not contain a data body");
    }
    
    @Override
    public String toString(){
        String szSW = "SW1: " + Integer.toHexString(m_bySWOne) + "\nSW2: " + Integer.toHexString(m_bySWTwo)+"\n";
        if(m_byaData == null){
           return "Response APDU: Data: empty\n" +  szSW;
        }
        return "ResponseADPU: Data: "+ DatatypeConverter.printHexBinary(m_byaData) + "\n" + szSW ;
    }

}
