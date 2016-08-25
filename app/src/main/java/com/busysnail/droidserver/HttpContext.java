package com.busysnail.droidserver;

import java.net.Socket;
import java.util.HashMap;

/**
 * author: malong on 2016/8/25
 * email: malong_ilp@163.com
 * address: Xidian University
 */

public class HttpContext {
    private final HashMap<String,String> requestHeaders;

    private Socket underlySocket;

    public HttpContext() {
        requestHeaders=new HashMap<>();
    }

    public Socket getUnderlySocket() {
        return underlySocket;
    }

    public void setUnderlySocket(Socket underlySocket) {
        this.underlySocket = underlySocket;
    }

    public void addRequestHeader(String headerName,String headerValue){
        requestHeaders.put(headerName,headerValue);
    }


    public String getRequestHeaderValue(String headername){
       return requestHeaders.get(headername);
    }
}
