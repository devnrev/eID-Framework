/*
 * CAPDUCommand.java
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

import common.CArrayHelper;
import common.cardio.coding.CAPDUNormalMessaging;

import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: Axel
 * Date: 11.12.11
 * Time: 20:59
 * To change this template use File | Settings | File Templates.
 */
public class CAPDUCommand{
    protected int m_byCla;
    protected int m_byIns;
    protected int m_byPOne;
    protected int m_byPTwo;
    protected int  m_nLc;
    protected int  m_nLe;
    protected byte[] m_byaData;

    protected byte case_;
    protected byte[] m_byaBytes;
    protected boolean m_bExtendedAPU;
    protected IAPDUMessageCoding m_messageCoding;

    protected void initialize(int byCla, int byIns, int byPOne, int byPTwo) {
        m_byCla = byCla;
        m_byIns = byIns;
        m_byPOne = byPOne;
        m_byPTwo = byPTwo;
        m_nLc = 0;
        m_nLe = 0;
    }

    protected void initializeWithData(int byCla, int byIns, int byPOne, int byPTwo, byte[] byaData) {
        initialize(byCla, byIns, byPOne, byPTwo);
        m_byaData = byaData;
        m_nLc = byaData.length;
        m_bExtendedAPU = m_nLc > 0xFF;

    }

    public CAPDUCommand(int byCla, int byIns, int byPOne, int byPTwo) {
        case_ = 1;
        m_bExtendedAPU=false;
        initialize(byCla, byIns, byPOne, byPTwo);

    }

    public CAPDUCommand(int byCla, int byIns, int byPOne, int byPTwo, int nLe) {
        initialize(byCla, byIns, byPOne, byPTwo);
        m_nLe = nLe;
        case_ = 2;
        m_bExtendedAPU = nLe > 0xFF;

    }

    public CAPDUCommand(int byCla, int byIns, int byPOne, int byPTwo, byte[] byaData) {
        initializeWithData(byCla, byIns, byPOne, byPTwo,byaData);
        case_ = 3;
        
    }

    public CAPDUCommand(int byCla, int byIns, int byPOne, int byPTwo, byte[] byaData, int nLe) {
        initializeWithData(byCla, byIns, byPOne, byPTwo,byaData);
        case_ = 4;
        m_nLe = nLe;
        m_bExtendedAPU = (m_nLc > 0xFF) || (m_bExtendedAPU);
    }

    public CAPDUCommand(byte[] byaCommand) {
        m_byCla = byaCommand[0];
        m_byIns = byaCommand[1];
        m_byPOne = byaCommand[2];
        m_byPTwo = byaCommand[3];
        int nCmdLength = byaCommand.length;
        case_ = 1;
        if(nCmdLength<7){
           if(nCmdLength == 4){
                m_bExtendedAPU = false;
                case_ = 2;
                m_nLe =  byaCommand[4];
           }else if(nCmdLength == 7){
                m_bExtendedAPU = true;
                case_ = 2;
                m_nLe = ByteBuffer.wrap(byaCommand,5,2).getInt();
           }
        }else{
           if(byaCommand[4]==0x00){
               m_bExtendedAPU = true;
               m_nLc = ByteBuffer.wrap(byaCommand,5,2).getInt();
               System.arraycopy(byaCommand,7,m_byaData,0,m_nLc);
               m_nLe = ByteBuffer.wrap(byaCommand,8+m_nLc,2).getInt();
           }else{
               m_bExtendedAPU = false;
               m_nLc =  byaCommand[4];
               System.arraycopy(byaCommand,5,m_byaData,0,m_nLc);
               m_nLe = byaCommand[nCmdLength-1];
           }
        }
    }

    public byte[] getBytes() {
        return getMessageCoding().encode(this);
    }

    public int getCla() {
        return m_byCla;
    }

    public int getIns() {
        return m_byIns;
    }

    public int getPOne() {
        return m_byPOne;
    }

    public int getPTwo() {
        return m_byPTwo;
    }

    public int getLc() {
        return m_nLc;
    }

    public int getLe() {
       return m_nLe;
    }

    public byte[] getData() {
        if (case_ > 2) {
            return m_byaData;
        }
        throw new NullPointerException("Command format does not contain data body");
    }

    public void appendData(byte[] byaData){
        if(m_byaData != null){
            /*
            byte[] byaNewData = new byte[m_nLc+byaData.length];
            System.arraycopy(m_byaData,0,byaNewData,0,m_nLc);
            System.arraycopy(byaData,0,byaNewData,m_nLc,byaData.length);
            m_byaData = byaNewData;
            m_nLc = m_nLc+byaData.length; */
            m_byaData = CArrayHelper.concatArrays(m_byaData,byaData);
            m_nLc = m_byaData.length;
        }else{
            m_byaData = byaData;
        }
        m_nLc = m_byaData.length;


    }
    
    @Override
    public String toString(){
        return DatatypeConverter.printHexBinary(getBytes());
    }


    public byte getCase() {
        return case_;
    }

    public boolean getExtendedAPDU() {
        return m_bExtendedAPU;
    }

    public void setMessageCoding(IAPDUMessageCoding msgCoding){
        m_messageCoding = msgCoding;
    }

    public IAPDUMessageCoding getMessageCoding(){
        if(m_messageCoding==null){
            m_messageCoding = new CAPDUNormalMessaging();
        }
        return m_messageCoding;
    }
}
