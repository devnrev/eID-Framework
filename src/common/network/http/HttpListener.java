/*
 * HttpListener.java
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

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import common.util.Logger;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Axel
 * Date: 30.05.12
 * Time: 11:53
 */
public class HttpListener {
    private String context_;
    private int port_;
    private HttpServer server_;
    private IHttpListenerCallback callback_;

    public HttpListener(String context, int port, IHttpListenerCallback callback) throws IOException {
        server_ = HttpServer.create(new InetSocketAddress("127.0.0.1",port), 0);
        server_.createContext(context, new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                Logger.log("request");
                String requestMethod = httpExchange.getRequestMethod();
                Logger.log(requestMethod);
                if (httpExchange.getRequestURI().getPath().equals(context_)) {
                    InputStream in = httpExchange.getRequestBody();


                    Headers requestHeaders = httpExchange.getRequestHeaders();
                    int contentLength = 0;
                    if (requestHeaders.containsKey("Content-Length")) {
                        contentLength = Integer.parseInt(requestHeaders.getFirst("Content-Length"));
                    }
                    HttpResponseData resp = callback_.update(
                            new HttpRequestData(requestHeaders,
                                    (Map<String, String>) httpExchange.getAttribute("parameters"),
                                    in,
                                    contentLength));

                    if (resp == null) {
                        httpExchange.sendResponseHeaders(400, 0);
                    } else {
                        Headers responseHeaders = httpExchange.getResponseHeaders();
                        for (Map.Entry<String, List<String>> e : resp.getHeader().entrySet()) {
                            responseHeaders.put(e.getKey(), e.getValue());
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        OutputStream out = httpExchange.getResponseBody();
                        out.write(resp.getData());
                        out.close();
                    }
                }

            }
        }).getFilters().add(new HttpUriParamFilter());

        context_ = context;
        port_ = port;
        callback_ = callback;
    }

    public void start() {
        server_.start();
    }

    public void stop() {
        server_.stop(0);
    }

    public String getContext() {
        return context_;
    }

    public int getPort() {
        return port_;
    }

}
