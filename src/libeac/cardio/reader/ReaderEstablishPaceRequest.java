/*
 * ReaderEstablishPaceRequest.java
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

package libeac.cardio.reader;

import common.CArrayHelper;
import libeac.CertHolderAuthTemplate;
import libeac.definitions.PACETypes;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 31.07.12
 * Time: 15:18
 * To change this template use File | Settings | File Templates.
 */


/**
 * This class is used to tell the card reader to establish a PACE channel according to the given parameters.
 * TR-03119 page 35
 */
public class ReaderEstablishPaceRequest  extends CommandRequest{

    /**
     * Constructor
     * @param passwordType Password type
     * @param chat Certificate Holder Authorization Template
     * @param password Password
     * @param certDesc Certificate description
     */
    public ReaderEstablishPaceRequest(PACETypes.Password passwordType,
                                      CertHolderAuthTemplate chat,
                                      byte[] password,
                                      byte[] certDesc) {
        super((byte)0x2);
        if(chat != null){
        byte[] chatData = chat.getEncoded();
        data_ = new byte[]{(byte)(passwordType.ordinal()+1),(byte)chatData.length};
        data_ = CArrayHelper.concatArrays(data_,chatData);
        }else{
            data_ = new byte[]{(byte)(passwordType.ordinal()+1),(byte)0};
        }
        if(password != null){
            data_ = CArrayHelper.concatArrays(data_,new byte[]{(byte)password.length},password);
        }else{
            data_ = CArrayHelper.concatArrays(data_,new byte[]{(byte)0});
        }
        if(certDesc!=null){
           data_ = CArrayHelper.concatArrays(data_,new byte[]{
                   (byte)(certDesc.length & 0xFF),(byte)((certDesc.length & 0xFF00)>>0x8)},certDesc);
        }else{
            data_ = CArrayHelper.concatArrays(data_,new byte[]{(byte)0,
                    (byte)0});
        }
    }
}
