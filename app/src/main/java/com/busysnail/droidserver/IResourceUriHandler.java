package com.busysnail.droidserver;

import java.io.IOException;

/**
 * author: malong on 2016/8/25
 * email: malong_ilp@163.com
 * address: Xidian University
 */

public interface IResourceUriHandler {
    /**
     * 该uri是否路由到此handler
     */
    boolean accept(String uri);

    /**
     *处理逻辑
     */
    void handle(String uri,HttpContext httpContext) throws IOException;
}
