package com.busysnail.droidserver;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * author: malong on 2016/8/25
 * email: malong_ilp@163.com
 * address: Xidian University
 */

public class ResourceInAssetsHandler implements IResourceUriHandler {

    private String acceptPrefix = "/static/";
    private Context context;

    public ResourceInAssetsHandler(Context context) {
        this.context = context;
    }

    @Override
    public boolean accept(String uri) {
        return uri.startsWith(acceptPrefix);
    }

    @Override
    public void handle(String uri, HttpContext httpContext) throws IOException {

        int startIndex = acceptPrefix.length();
        String assertPath = uri.substring(startIndex);
        InputStream is = context.getAssets().open(assertPath);
        byte[] raw = StreamToolkit.readRawFromStream(is);

        OutputStream os=httpContext.getUnderlySocket().getOutputStream();
        PrintStream printer=new PrintStream(os);
        printer.println("HTTP/1.1 200 OK");
        printer.println("Content-Length: "+raw.length);
        if(assertPath.endsWith(".html")){
            printer.println("Content-Type: text/html");
        }else if(assertPath.endsWith(".js")){
            printer.println("Content-Type: text/js");
        }else if(assertPath.endsWith(".css")){
            printer.println("Content-Type: text/css");
        }else if(assertPath.endsWith(".jpg")){
            printer.println("Content-Type: text/jpg");
        }else if(assertPath.endsWith(".png")){
            printer.println("Content-Type: text/png");
        }
        printer.println();
        printer.write(raw);
    }
}
