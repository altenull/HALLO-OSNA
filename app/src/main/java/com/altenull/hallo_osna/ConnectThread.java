package com.altenull.hallo_osna;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class ConnectThread extends Thread {
    private String hostName = null;
    private IntroActivity.ProgressHandler progressHandler;
    private Context introContext;


    public ConnectThread(String paramHostName, IntroActivity.ProgressHandler paramProgressHandler, Context paramContext) {
        this.hostName = paramHostName;
        this.progressHandler = paramProgressHandler;
        this.introContext = paramContext;
    }


    private String request(String paramString) {
        StringBuilder XMLContentStringBuilder = new StringBuilder();

        for(  ;  ;  ) {
            String inputLine;
            String responseString;

            try {
                HttpURLConnection conn = (HttpURLConnection)new URL(paramString).openConnection();

                if ( conn != null ) {
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/xml");
                    conn.setUseCaches(false);
                    conn.setDoInput(true);

                    if ( conn.getResponseCode() == HttpURLConnection.HTTP_OK ) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                        while ( true ) {
                            inputLine = in.readLine();
                            if ( inputLine == null ) {
                                break;
                            }
                            XMLContentStringBuilder.append(inputLine + "\n");
                        }

                        in.close();
                    }
                    conn.disconnect();
                }
                responseString = XMLContentStringBuilder.toString();
            }
            catch (Exception localException) {
                responseString = "Error : " + localException.getMessage();
                continue;
            }
            return responseString;
        }
    }


    public void run() {
        ConnectivityManager localConnectivityManager = (ConnectivityManager)this.introContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = localConnectivityManager.getActiveNetworkInfo();

        Message localMessage = this.progressHandler.obtainMessage();

        if ( activeNetwork != null ) {
            String xmlContent = request(this.hostName);

            NodeList studentNodeList = null;

            try {
                studentNodeList = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(xmlContent.getBytes("utf-8"))).getDocumentElement().getElementsByTagName("student");
            }
            catch (Exception localException) {
                localException.printStackTrace();
            }

            for ( int i = 0;  ;  i++ ) {
                if ( i >= studentNodeList.getLength() ) {
                    localMessage.obj = "";
                    this.progressHandler.sendMessage(localMessage);
                    return;
                }
                Element localElement = (Element)studentNodeList.item(i);
                DataHandler.getInstance().addData(localElement);
            }
        }
        else {
            localMessage.obj = "네트워크 연결이 불가능합니다.";
            this.progressHandler.sendMessage(localMessage);
            return;
        }
    }
}