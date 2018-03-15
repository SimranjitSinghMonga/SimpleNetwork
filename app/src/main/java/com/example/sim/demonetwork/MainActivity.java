package com.example.sim.demonetwork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String USGS_REQUEST_URL =
            "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-12-01&minmagnitude=7";
    InputStream inputStream;
    String jsonData;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        StringBuilder output=new StringBuilder();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
        /*  Create a new URL */
            URL url = new URL(USGS_REQUEST_URL);
        /* Make HTTP Request */

            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if(urlConnection.getResponseCode()==200)
            {
               inputStream=urlConnection.getInputStream();
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader=new BufferedReader(inputStreamReader);
                String line=reader.readLine();
                while(line!=null)
                {
                    output.append(line);
                    line=reader.readLine();
                }
                jsonData=output.toString();
                JSONObject baseJsonResponse=new JSONObject(jsonData);
                JSONArray featureArray=baseJsonResponse.getJSONArray("features");
                if(featureArray.length()>0)
                {
                    JSONObject firstFeature=featureArray.getJSONObject(0);
                    JSONObject properties=firstFeature.getJSONObject("properties");
                    String title=properties.getString("title");
                    long time=properties.getLong("time");
                    Event e=new Event(title,time);
                    SimpleDateFormat dtFormat=new SimpleDateFormat("EEE, d MM YYYY 'at' HH:mm:ss z");
                    String ntime=dtFormat.format(time);
                    TextView txttitle=(TextView)findViewById(R.id.txtname);
                    txttitle.setText(e.title);

                    TextView txttime=(TextView)findViewById(R.id.txttime);
                    txttime.setText(ntime);


                }
                                Log.e(LOG_TAG, "Success");
            }
            else
            {
                Log.e(LOG_TAG, "Error response code : " + urlConnection.getResponseCode());
            }


        }
        catch(MalformedURLException m)
        {

        }
        catch(IOException e)
        {

        }
        catch(JSONException j)
        {

        }


    }

}
