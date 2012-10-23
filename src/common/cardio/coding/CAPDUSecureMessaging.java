/*
 * CAPDUSecureMessaging.java
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

import common.CFactoryHelper;
import common.CArrayHelper;
import common.CMathHelper;
import common.cardio.CAPDUCommand;
import common.cardio.CAPDUResponse;
import common.cardio.IAPDUMessageCoding;
import common.crypto.CPadding;
import common.crypto.CryptoTypes;
import common.crypto.ISymmetricBlockCipher;
import common.crypto.ISymmetricMAC;
import common.util.Logger;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 26.12.11
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class provides the secure message coding for APDU messages. It needs to be initialized with two keys.
 * One used to en- and decrypt the message and a second one to calculate the MAC of the message
 */
public class CAPDUSecureMessaging implements IAPDUMessageCoding {
    private byte[] m_byaKMac;
    private byte[] m_byaKEnc;
    private ISymmetricBlockCipher m_blockCipher;
    private ISymmetricMAC m_symMAC;
    private static byte[] s_byaSSC;
    private CAPDUCommand m_command;
    private CryptoTypes.ESymmetricBlockCipher m_eCipherType;

    private class TagStruct{
        int offset;
        int dataLength;
        int dataoffset;


        public TagStruct(int off,int len,int doff){
            offset = off;
            dataLength = len;
            dataoffset = doff;
        }
    }

    public CAPDUSecureMessaging(byte[] byaKEnc, byte[] byaKMac, CryptoTypes.ESymmetricBlockCipher eCipher) {
        if (s_byaSSC == null) {
            s_byaSSC = new byte[16];            
        }
        switch (eCipher) {
            case AES:
                m_blockCipher = CFactoryHelper.getCryptoProvider().createAESProcessor();
                m_symMAC = CFactoryHelper.getCryptoProvider().createCMacAuthenticator();
                m_symMAC.initialize(CryptoTypes.EKeyLength.LEN_128);
                break;
        }
        m_eCipherType = eCipher;
        m_byaKEnc = byaKEnc;
        m_byaKMac = byaKMac;


    }

    private void initCipher(){
        CMathHelper.increment(s_byaSSC);
        switch (m_eCipherType) {
            case AES:
                byte[] byaIV = new byte[16];
                m_blockCipher.initialize(byaIV, m_byaKEnc, CryptoTypes.EBlockMode.CBC, CryptoTypes.EKeyLength.LEN_128);
                byaIV = m_blockCipher.encrypt(s_byaSSC);
              //  Logger.log(DatatypeConverter.printHexBinary(byaIV));
              //  Logger.log(DatatypeConverter.printHexBinary(s_byaSSC));
                m_blockCipher.initialize(byaIV, m_byaKEnc, CryptoTypes.EBlockMode.CBC, CryptoTypes.EKeyLength.LEN_128);
                break;
        }
    }

    public void reset() {
        for(int i = 0; i < s_byaSSC.length; ++i){
            s_byaSSC[i] = 0x0;
        }
    }

    @Override
    public byte[] encode(CAPDUCommand cmd) {
        initCipher();
        m_command = cmd;
        byte[] byaHeader = new byte[]{
                (byte) (cmd.getCla() | 0x0C),
                (byte) cmd.getIns(),
                (byte) cmd.getPOne(),
                (byte) cmd.getPTwo()};

        byte[] byaPaddedHeader = CPadding.applySecMsgPadding(byaHeader, m_blockCipher.getBlockSize());

        byte[] byaRet;
        switch (cmd.getCase()) {
            case 1:
                byaRet = new byte[]{};
                break;
            case 2:
                byaRet = createDO97((byte) cmd.getLe());
                break;
            case 3:
                byaRet = createDO87(cmd.getData());
                break;
            case 4:
                byte[] byaDO97 = createDO97((byte) cmd.getLe());
                byte[]  byaDO87 = createDO87(cmd.getData());
                byaRet = CArrayHelper.concatArrays(byaDO87, byaDO97);
                break;
            default:
                throw new NoSuchFieldError("APDU Command case not defined! Encoding error.");
        }
        if (byaRet == null)
            throw new NullPointerException("Error during command encoding encountered");

        byte[] byaCmd = CArrayHelper.concatArrays(byaPaddedHeader,byaRet);
        byte[] byaDO8E = createDO8E(CPadding.applySecMsgPadding(CArrayHelper.concatArrays(s_byaSSC, byaCmd),
                                                                m_blockCipher.getBlockSize()));

        if(!cmd.getExtendedAPDU()){
            byte byLC_Dash =(byte)(byaRet.length+byaDO8E.length);
            byaRet = CArrayHelper.concatArrays(byaHeader,new byte[]{byLC_Dash},byaRet, byaDO8E, new byte[]{0x00});
        }else{
            int nLC_Dash = (byaRet.length+byaDO8E.length);
            byaRet = CArrayHelper.concatArrays(byaHeader,new byte[]{0,(byte)((nLC_Dash>>0x8) & 0xFF),(byte)(nLC_Dash & 0xFF)},
                                               byaRet, byaDO8E, new byte[]{0x00,0x00});
        }
        Logger.log(DatatypeConverter.printHexBinary(byaRet));
        return byaRet;
    }

    private byte[] createDO87(byte[] byaData) {
        byte[] byaPaddedData = CPadding.applySecMsgPadding(byaData, m_blockCipher.getBlockSize());
        int nLength = byaPaddedData.length;
        byte[] byaEncData = m_blockCipher.encrypt(byaPaddedData);
        byte[] byaRet;
        byte byTag = (byte)0x87;
        if(m_command.getExtendedAPDU()){
            if(byaPaddedData.length < 0x100){
               byaRet = new byte[]{
                        byTag,
                        (byte)0x81,
                        (byte)((byaPaddedData.length + 1) & 0xFF),0x01};

            }else if(byaPaddedData.length < 0x10000){
                byaRet = new byte[]{
                        byTag,
                        (byte)0x82,
                        (byte)(((byaPaddedData.length + 1)>>0x8) & 0xFF),
                        (byte)((byaPaddedData.length + 1) & 0xFF),0x01};
            }else{
                byaRet = new byte[]{
                        byTag,
                        (byte)0x83,
                        (byte)(((byaPaddedData.length + 1)>>0x10) & 0xFF),
                        (byte)(((byaPaddedData.length + 1)>>0x8) & 0xFF),
                        (byte)((byaPaddedData.length + 1) & 0xFF),0x01};

            }
            byaRet = CArrayHelper.concatArrays(byaRet,byaEncData);
        }else{
            byaRet = new byte[nLength + 3];
            byaRet[0] = (byte) byTag;
            byaRet[1] = (byte) (byaPaddedData.length + 1);
            byaRet[2] = (byte) 0x01;
            System.arraycopy(byaEncData, 0, byaRet, 3, byaEncData.length);
        }
        return byaRet;

    }

    private byte[] createDO8E(byte[] byaData) {
        byte[] byaRes = m_symMAC.calculate(m_byaKMac, byaData);
        byte[] byaRet = new byte[2 + 8];
        byaRet[0] = (byte) 0x8E;
        byaRet[1] = (byte) 0x08;

        System.arraycopy(byaRes, 0, byaRet, 2, 8);

        return byaRet;
    }

    private byte[] createDO97(byte byLe) {
        if(m_command.getExtendedAPDU()){
            return new byte[]{(byte) 0x97, (byte) 0x01, 0,(byte)((byLe>>0x8) & 0xFF),(byte)(byLe & 0xFF)};
        }else{
            return new byte[]{(byte) 0x97, (byte) 0x01, byLe};
        }

    }

    @Override
    public CAPDUResponse decode(byte[] byaResp) throws Exception{
        Logger.log(DatatypeConverter.printHexBinary(byaResp));
        initCipher();        
        byte[] byaDO8E = parseData((byte) 0x8E, byaResp);
        if (byaDO8E == null)
            throw new NullPointerException("No valid secure messaging apdu response");
        
        byaResp = Arrays.copyOfRange(byaResp, 0, getTagPos((byte) 0x8E, byaResp).offset);

        byte[] byaRespPadded = CPadding.applySecMsgPadding(CArrayHelper.concatArrays(s_byaSSC, byaResp),
                                                           m_blockCipher.getBlockSize());

        byte[] byaMAC = Arrays.copyOf(m_symMAC.calculate(m_byaKMac, byaRespPadded), 8);

        if (!Arrays.equals(byaMAC, byaDO8E)){
           throw new Exception("Computed MAC differs from the one in the apdu response");
        }

        byte[] byaDO99 = CPadding.removeSecMsgPadding(parseData((byte) 0x99, byaResp));

        if (byaDO99 == null)
            throw new NullPointerException("No status field detected in apdu response");

        CAPDUResponse resp = new CAPDUResponse(byaDO99[0] & 0xFF, byaDO99[1] & 0xFF);
        byte[] byaDO87 = parseData((byte) 0x87, byaResp);
        if (byaDO87 != null) {
            byte[] decryptedData = m_blockCipher.decrypt(Arrays.copyOfRange(byaDO87, 1, byaDO87.length));
            resp.setData(CPadding.removeSecMsgPadding(decryptedData));
        }



        return resp;
    }

    private TagStruct getTagPos(byte byTag,byte[] byaData){
        int nLen = byaData.length;
        int nPos = 0;
        int dataLength;
        int dataOffset;
        while (nPos < nLen) {
            if(byaData[nPos] == (byte)0x87){
                switch (byaData[nPos+1]){
                    case (byte)0x81:
                        dataLength =  byaData[nPos+2] ;
                        dataOffset = 3;
                        break;
                    case (byte)0x82:
                        dataLength = (byaData[nPos+2]<< 0x8) | (byaData[nPos+3]) ;
                        dataOffset = 4;
                        break;
                    case (byte)0x83:
                        dataLength = (byaData[nPos+2]<< 0x10) | (byaData[nPos+3] << 0x8) | byaData[nPos+4] ;
                        dataOffset = 5;
                        break;
                    default:
                        dataLength =byaData[nPos+1];
                        dataOffset = 2;
                        break;
                }

            }else{
               dataLength = byaData[nPos+1];
               dataOffset = 2;
            }

            if (byaData[nPos] == byTag) {
                return new TagStruct(nPos, dataLength,dataOffset);
            }
            nPos += dataOffset+dataLength;

        }
        return null;
    }


    
    private byte[] parseData(byte byTag, byte[] byaData) {
        TagStruct ts = getTagPos(byTag,byaData);
        if(ts!=null){
            int nDataLen = ts.dataLength;
            byte[] byaRet = new byte[nDataLen];
            System.arraycopy(byaData, ts.offset+ts.dataoffset, byaRet, 0, nDataLen);
            return byaRet;
        }
        return null;

    }

    //increment SSC for testing only
    public void incSSC(){
        CMathHelper.increment(s_byaSSC);
    }

    public byte[] getSSC(){
        return s_byaSSC;
    }
}
