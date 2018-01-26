package com.example.android.asynctask;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getName();

    private static URL createURL(String stringURL){
        URL url=null;
        try {
            url=new URL(stringURL);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG,"Error in Building URL",exception);
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output=new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader=new BufferedReader(inputStreamReader);
            String line=reader.readLine();
            while (line != null){
                output.append(line);
                line=reader.readLine();
            }
        }
        return output.toString();
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResonse="";
        if (url == null){
            return jsonResonse;
        }
        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;
        try {
            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode()==200){
                inputStream=urlConnection.getInputStream();
                jsonResonse=readFromStream(inputStream);
            }
            else{
                Log.e(LOG_TAG,"Error in ResonceCode"+ urlConnection.getResponseCode());
            }
        } catch (IOException exception) {
            Log.e(LOG_TAG,"Problem retrieving the earthquake JSON results.",exception);
        }
        finally {
            if(urlConnection!=null)
                urlConnection.disconnect();
            if(inputStream!=null)
                inputStream.close();
        }
        return jsonResonse;
    }

    private static ArrayList<EarthQuake> extractFeatureFromJson(String earthquakeJSON) {
        if(TextUtils.isEmpty(earthquakeJSON))
            return null;

        ArrayList<EarthQuake> earthQuakes=new ArrayList<>();
        try {
            JSONObject root=new JSONObject(earthquakeJSON);
            JSONArray features=root.getJSONArray("features");
            for(int i=0;i<features.length();i++){
                JSONObject feature=features.getJSONObject(i);
                JSONObject properties=feature.getJSONObject("properties");
                double magnitute=properties.getDouble("mag");
               String location=properties.getString("place");
               long timeInMilliSecond=properties.getLong("time");
               String url=properties.getString("url");
                earthQuakes.add(new EarthQuake(magnitute,location,timeInMilliSecond,url));
            }
        } catch (JSONException exception) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", exception);
        }
        return  earthQuakes;
    }

    public static List<EarthQuake> fetchEarthquakeData(String requestURL){
        URL url=createURL(requestURL);
        String jsonRespose=null;
        try {
            jsonRespose=makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<EarthQuake> earthQuakes=extractFeatureFromJson(jsonRespose);
        return earthQuakes;
    }
}


