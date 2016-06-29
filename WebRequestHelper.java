package com.owndevs.neele.neelepossiblemobilechallenge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Neele on 6/29/2016.
 */
public class WebRequestHelper {

    static String response = "";
    public final static int GET = 1;
    public final static int POST = 2;

    public WebRequestHelper(){

    }

    public String makeWebServiceCall(String url, int requestmethod){
        return this.makeWebServiceCall(url, requestmethod, null);
    }

    public String makeWebServiceCall(String urlad, int requestmethod, HashMap<String, String> params){
        URL url;
        String response = "";

        try{
            url = new URL(urlad);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(10000);
            con.setConnectTimeout(10000);
            con.setDoInput(true);
            con.setDoOutput(true);
            if (requestmethod == POST){
                con.setRequestMethod("POST");
            }else if(requestmethod == GET){
                con.setRequestMethod("GETRequest");
            }
            if (params != null){
                OutputStream os = con.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                StringBuilder requestresult = new StringBuilder();
                boolean first = true;

                for (Map.Entry<String, String> entry : params.entrySet()){
                    if (first){
                        first = false;
                    }else{
                        requestresult.append("&");
                        requestresult.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                        requestresult.append("=");
                        requestresult.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                    }
                    bw.write(requestresult.toString());
                    bw.flush();
                    bw.close();
                    os.close();
                }
                int responseCode = con.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK){
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                }else{
                    response = "";
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        return response;
    }

}
