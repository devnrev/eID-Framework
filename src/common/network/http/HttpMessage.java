/*
 * HttpMessage.java
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

package common.network.http;

import common.CArrayHelper;

import javax.xml.bind.DatatypeConverter;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 20.06.12
 * Time: 15:14
 */
public abstract class HttpMessage {
    protected String initialLine;
    protected HttpHeader header_;
    protected byte[] body_;

    protected HttpVersion version_;


    public enum HttpVersion{
        VER_1_0{
            public String toString() {
                return "HTTP/1.0";
            }
        },
        VER_1_1{
            public String toString() {
                return "HTTP/1.1";
            }
        }
    };

    public void setVersion(HttpVersion version){
        version_ = version;
    }


    protected abstract String getInitialLine();

    public HttpMessage(){
          header_ = new HttpHeader();
            body_ = new byte[0];
    }

    protected HttpMessage(HttpHeader header, byte[] body){
        header_ = header;
        body_ = body;
    }

    public void setHeader(HttpHeader header) {
        this.header_ = header;
    }

    public void setBody(byte[] body){
        body_ = body;
    }

    public HttpVersion getVersion(){
        return version_;
    }

    public HttpHeader getHeader(){
        return header_;
    }

    public byte[] getBody(){
        return body_;
    }

    public byte[] getBytes(){
        String message = getInitialLine();
        message+= header_.toString()+"\n";
        byte data[] = message.getBytes();
        if(body_.length>0){
            return CArrayHelper.concatArrays(data,body_);
        }
        return data;
    }

    public String getMessage(){  /*
        String message = getInitialLine();
        message+= header_.toString()+"\n";
        if(body_ != null){
            message+= new String(body_);
        }
        return message;            */
        return new String(getBytes());
    }

    @Override
    public String toString() {
        return new String(getBytes());
    }
}
