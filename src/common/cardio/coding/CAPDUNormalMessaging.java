/*
 * CAPDUNormalMessaging.java
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

package common.cardio.coding;

import common.cardio.CAPDUCommand;
import common.cardio.CAPDUResponse;
import common.cardio.IAPDUMessageCoding;

import java.nio.ByteBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 26.12.11
 * Time: 23:33
 * To change this template use File | Settings | File Templates.
 */
public class CAPDUNormalMessaging implements IAPDUMessageCoding {

    @Override
    public byte[] encode(CAPDUCommand cmd) {
        byte[] byaRet;
        boolean bExtendedAPDU = cmd.getExtendedAPDU();
        int nLc = cmd.getLc();
        int nLe = cmd.getLe();
        byte[] byaData;
        switch (cmd.getCase()){
            case 1:
                byaRet = new byte[4];
                break;
            case 2:
                if(bExtendedAPDU){
                    byaRet = new byte[7];
                    byaRet[4] = 0;
                    System.arraycopy(new byte[]{(byte)((nLc>>8) & 0xFF),(byte)(nLc & 0xFF)},0,byaRet,5,2);
                }else{
                    byaRet = new byte[5];
                    byaRet[4] = (byte)nLe;
                }
                break;
            case 3:
                byaData = cmd.getData();
                if(bExtendedAPDU){
                    byaRet = new byte[7+nLc];
                    byaRet[4] = 0;
                    System.arraycopy(new byte[]{(byte)((nLc>>8) & 0xFF),(byte)(nLc & 0xFF)},0,byaRet,5,2);
                    System.arraycopy(byaData,0,byaRet,7,nLc);
                }else{
                    byaRet = new byte[5+nLc];
                    byaRet[4] = (byte)nLc;
                    System.arraycopy(byaData,0,byaRet,5,nLc);
                }
                break;
            case 4:
                byaData = cmd.getData();
                if(bExtendedAPDU){
                    byaRet = new byte[10+nLc];
                    System.arraycopy(new byte[]{(byte)((nLc>>8) & 0xFF),(byte)(nLc & 0xFF)},0,byaRet,5,2);
                    System.arraycopy(byaData,0,byaRet,7,nLc);
                    byaRet[byaRet.length-4] = 0;
                    System.arraycopy(new byte[]{(byte)((nLc>>8) & 0xFF),(byte)(nLc & 0xFF)},0,byaRet,byaRet.length-3,2);
                }else{
                    byaRet = new byte[6+nLc];
                    byaRet[4] = (byte)nLc;
                    System.arraycopy(byaData,0,byaRet,5,nLc);
                    byaRet[byaRet.length-1] = (byte) nLe;
                }
                break;
            default:
                throw new NoSuchFieldError("APDU Command case not defined! Encoding error.");
        }
        byaRet[0] = (byte)cmd.getCla();
        byaRet[1] = (byte)cmd.getIns();
        byaRet[2] = (byte)cmd.getPOne();
        byaRet[3] = (byte)cmd.getPTwo();
        //Logger.log(DatatypeConverter.printHexBinary(m_byaBytes));
        return byaRet;
    }

    @Override
    public CAPDUResponse decode(byte[] byaResp) {
        int nLength = byaResp.length;
        int bySWOne = byaResp[nLength-2] & 0xFF;
        int bySWTwo = byaResp[nLength-1] & 0xFF;
        byte[] byaData = new byte[nLength-2];
        System.arraycopy(byaResp,0,byaData,0,nLength-2);
        return new CAPDUResponse(byaData,bySWOne,bySWTwo);
    }
}
