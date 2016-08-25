package com.busysnail.droidserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * author: malong on 2016/8/25
 * email: malong_ilp@163.com
 * address: Xidian University
 */

public class StreamToolkit {
    /**
     *
     * @param is 从Http连接取得的输入流
     * @return 提取Http的一条header（以"\r\n"分割）
     * @throws IOException
     */

    public static final String readLine(InputStream is) throws IOException {
        StringBuilder sb=new StringBuilder();
        int c1=0;
        int c2=0;
        while(c2!=-1 && !(c1=='\r' && c2=='\n')){
            c1=c2;
            c2=is.read();
            sb.append((char)c2);
        }
        if(sb.length()==0){
            return null;
        }
        return sb.toString();
    }


    /**
     * 读输入流返回字节数组
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] readRawFromStream(InputStream is) throws IOException {
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        byte[] buffer=new byte[10240];
        int nReaded;
        while((nReaded=is.read(buffer))>0){
            bos.write(buffer,0,nReaded);
        }
        return bos.toByteArray();
    }
}
