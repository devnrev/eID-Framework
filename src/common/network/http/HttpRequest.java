/*
 * HttpRequest.java
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

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 20.06.12
 * Time: 15:15
 */

/**
 * This class extends the basic HTTP message to a HTTP request and provides the required attributes
 */
public class HttpRequest extends HttpMessage {
    protected String resourcePath_;
    protected HttpMethod method_;

    public enum HttpMethod{
        POST{
            public String toString() {
                return "POST";
            }
        },
        GET{
            public String toString() {
                return "GET";
            }
        }
    }

    public HttpMethod getMethod(){
        return method_;
    }


    public void setMethod(HttpMethod method) {
        this.method_ = method;
    }

    public HttpRequest(){
        super();
    }

    public HttpRequest(HttpHeader header, byte[] body) {
        super(header, body);
    }

    @Override
    public byte[] getBytes() {
        if(method_ == HttpMethod.POST){
            header_.setHeaderInfo("Content-Length",String.valueOf(body_.length));
        }
        return super.getBytes();
    }

    @Override
    protected String getInitialLine() {
        return method_ + " " + resourcePath_ + " " + version_+"\n";
    }


    public void setResourcePath(String resourcePath){
        resourcePath_ = resourcePath;
    }

    public String getRessourcePath(){
        return resourcePath_;
    }

}
