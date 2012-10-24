/*
 * HttpHeader.java
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


import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 21.06.12
 * Time: 11:02
 */

/**
 * This class represents a HTTP header which contains several attributes
 */
public class HttpHeader {

    protected Map<String,List<String>> header_;

    public HttpHeader(){
       header_ = new LinkedHashMap<String, List<String>>();
    }

    public HttpHeader(Map<String,List<String>> header){
        header_ = header;
    }

    public void addHeaderInfo(String key, List<String> values){
        if(header_.containsKey(key)){
            header_.get(key).addAll(values);
        }else{
            header_.put(key, values);
        }

    }

    public void addHeaderInfo(String key, String val){
        if(header_.containsKey(key)){
            header_.get(key).add(val);
        }else{
            List<String> values = new ArrayList<String>();
            values.add(val);
            header_.put(key, values);
        }
    }

    public void setHeaderInfo(String key, String val){
        List<String> values = new ArrayList<String>();
        values.add(val);
        header_.put(key, values);
    }

    public void setHeaderInfo(String key, List<String> values){
        header_.put(key, values);
    }

    public List<String> getHeaderEntry(String key){
        return header_.get(key);
    }

    public Map<String,List<String>> getHeader(){
        return header_;
    }


    @Override
    public String toString() {
        String content = "";
        for(String key : header_.keySet())
        {
            content+=key+": ";
            List<String> values = header_.get(key);
            int j = 0;
            final int valueCount = values.size();
            for(String value : values){
                content+=value;
                j++;
                if(j < valueCount){
                    content+=";";
                }
            }
            content+="\n";

        }
        return content;
    }
}
