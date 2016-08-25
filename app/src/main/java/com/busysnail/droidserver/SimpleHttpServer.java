package com.busysnail.droidserver;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;

/**
 * author: malong on 2016/8/25
 * email: malong_ilp@163.com
 * address: Xidian University
 */

public class SimpleHttpServer {

    private boolean isEnable=false;
    private WebConfiguration configuration;
    private ServerSocket socket;
    private ExecutorService threadPool;
    private HttpContext httpContext;
    private Set<IResourceUriHandler> resourceUriHandlers;

    public SimpleHttpServer(WebConfiguration configuration) {
        this.configuration = configuration;
        threadPool= Executors.newCachedThreadPool();
        httpContext=new HttpContext();
        resourceUriHandlers=new HashSet<>();
    }

    /**
     * 启动server（异步）
     */
    public void startAsync(){
        isEnable=true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                processAsync();
            }
        }).start();
    }


    /**
     * 停止server（异步）
     */
    public void stopAsync(){
        if(isEnable){
            isEnable=false;
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket=null;
            ;

        }

    }

    private void processAsync() {
        InetSocketAddress socketAddr = new InetSocketAddress(configuration.getPort());
        try {
            socket=new ServerSocket();
            socket.bind(socketAddr);
            while(isEnable){
                final Socket remotepeer = socket.accept();
                threadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("busySnial","a remote peer accepted..."+remotepeer.getRemoteSocketAddress());
                        onAcceptRemotePeer(remotepeer);
                    }
                });

            }
        } catch (Exception e) {
            Log.e("busySnail",e.toString());
            e.printStackTrace();
        }
    }

    public void registerResourceHandler(IResourceUriHandler handler){
        resourceUriHandlers.add(handler);

    }

    private void onAcceptRemotePeer(Socket remotePeer) {

        try {
            InputStream is=remotePeer.getInputStream();
            httpContext.setUnderlySocket(remotePeer);
            String headLine=null;
            //提取相对路径
            String resourceUri=headLine=StreamToolkit.readLine(is).split(" ")[1];
            while((headLine=StreamToolkit.readLine(is))!=null){
                if(headLine.equals("\r\n")){
                    break;
                }
                //header格式headerkey:(空格)headerValue
                String[] pair=headLine.split(": ");
                if(pair.length>1){
                    httpContext.addRequestHeader(pair[0],pair[1]);
                }
                Log.d("busySnail",headLine);
            }

            for(IResourceUriHandler handler:resourceUriHandlers){
                if(!handler.accept(resourceUri)){
                    continue;
                }
                handler.handle(resourceUri,httpContext);
            }
        } catch (IOException e) {
            Log.e("busySnail",e.toString());
        }
    }



}
