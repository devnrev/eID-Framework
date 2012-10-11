/*
 * HttpUriParamFilter.java
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

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import common.util.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 30.05.12
 * Time: 12:53
 */
public class HttpUriParamFilter extends Filter {
    private Map<String, String> parameters_;


    @Override
    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {
        parameters_ = new HashMap<String, String>();
        extractGetParameters(httpExchange.getRequestURI().getRawQuery());
        httpExchange.setAttribute("parameters",parameters_);
        chain.doFilter(httpExchange);
    }

    private void extractGetParameters(String uri){
        if(uri != null){
            parseQuery(uri);
        }
    }

    private void parseQuery(String query){
        String pairs[] = query.split("&");
        for(String pair : pairs){
            String elements[] = pair.split("=");
            String key = null;
            String val = "";
            if(elements.length > 0){
                try {
                    key = URLDecoder.decode(elements[0],"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Logger.log("Uri parameter key can not be parsed correctly: " + elements[0]);
                    e.printStackTrace();
                    continue;
                }
            }
            if(elements.length == 2){
                try {
                    val = URLDecoder.decode(elements[1],"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Logger.log("Uri parameter val can not be parsed correctly: " + elements[0]);
                    e.printStackTrace();
                    continue;
                }
            }
            if(elements.length == 2){
                if(!parameters_.containsKey(elements[0])){
                    parameters_.put(elements[0],elements[1]);
                }else{
                    Logger.log("WARNING: Duplicate key in parameter map! key: " + elements[0]);
                }
            }
        }
    }


    @Override
    public String description() {
        return "Extracts parameters from the uri";
    }
}
