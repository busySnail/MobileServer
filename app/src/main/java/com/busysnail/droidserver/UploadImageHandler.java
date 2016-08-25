package com.busysnail.droidserver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * author: malong on 2016/8/25
 * email: malong_ilp@163.com
 * address: Xidian University
 */

public class UploadImageHandler implements IResourceUriHandler {

    private String acceptPrefix="/upload_image/";
    @Override
    public boolean accept(String uri) {
        return uri.startsWith(acceptPrefix);
    }

    @Override
    public void handle(String uri, HttpContext httpContext) throws IOException {

        String tmpPath="/mnt/sdcard/test_ipload.jpg";
        long totalLength= Long.parseLong(httpContext.getRequestHeaderValue("Content-Length"));
        FileOutputStream fos=new FileOutputStream(tmpPath);
        InputStream is=httpContext.getUnderlySocket().getInputStream();
        byte[] buffer=new byte[10240];
        int nReaded=0;
        long nLeftLength=totalLength;
        while((nReaded=is.read(buffer))>0 && nLeftLength>0){
            fos.write(buffer,0,nReaded);
            nLeftLength-=nReaded;
        }
        fos.close();

        OutputStream os=httpContext.getUnderlySocket().getOutputStream();
        PrintWriter writer=new PrintWriter(os);
        writer.println("HTTP/1.1 200 OK");

        onImageLoaded(tmpPath);

    }

    protected void onImageLoaded(String path){

    }
}
