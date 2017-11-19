package com.example.android.quakereport;

import android.util.Log;
import android.widget.ArrayAdapter;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;

/**
 * Created by sejal on 20-10-2017.
 */

public final class QueryUtils {
    private QueryUtils(){

    }
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makehttpRequest(URL url) throws IOException {
     String jsonResponse = "";
        if (url==null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;

        try{
            urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }
        finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if (inputStream!=null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static ArrayList<earthquake> getearthdata(String earthquakeJSON){
        ArrayList<earthquake>earthquakes=new ArrayList<>();
        try {


            // build up a list of Earthquake objects with the corresponding data.
            JSONObject JSONobj=new JSONObject( earthquakeJSON);
            JSONArray JSONarr=JSONobj.getJSONArray("features");
            for (int i=0;i<JSONarr.length();i++){
             JSONObject fea=JSONarr.getJSONObject(i);
                JSONObject prop=fea.getJSONObject("properties");
                double mag=prop.getDouble("mag");
                String loc=prop.getString("place");
                Long datemilli=prop.getLong("time");
                String url=prop.getString("url");
                earthquake details=new earthquake(mag,loc,datemilli,url);
                earthquakes.add(details);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;

    }
    public static ArrayList<earthquake> fetchData(String requestUrl){

        URL url=createUrl(requestUrl);

        String JSONResponse=null;
         try {
             JSONResponse=makehttpRequest(url);
         } catch (IOException e) {
             Log.e(LOG_TAG, "Problem making the HTTP request.", e);
         }

         ArrayList<earthquake> current=getearthdata(JSONResponse);
        return current;
    }
}
