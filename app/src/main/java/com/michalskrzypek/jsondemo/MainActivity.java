package com.michalskrzypek.jsondemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
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
import java.util.concurrent.ExecutionException;

import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button theButton;
    EditText cityName, stateName;
    TextView cityView, weatherView,  weatherOutput;
    ImageView weatherImage;
String fullName, city, state, weather, temp, iconString;
DownloadImage downImage;
    String cityBeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        theButton = (Button) findViewById(R.id.theButton);
        cityName = (EditText) findViewById(R.id.cityName);
        stateName = (EditText) findViewById(R.id.stateName);
        weatherOutput = (TextView) findViewById(R.id.weatherOutput);
        cityView = (TextView) findViewById(R.id.cityView);
        weatherView = (TextView) findViewById(R.id.weatherView);
        weatherImage= (ImageView) findViewById(R.id.weatherImage);

    }

    public void getWeather(View view){
        cityBeta = cityName.getText().toString();
       city = cityBeta.replaceAll(" ", "_");

        InputMethodManager mngr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mngr.hideSoftInputFromWindow(cityName.getWindowToken(), 0);

        state = stateName.getText().toString().toUpperCase();

        DownloadContent downloadContent = new DownloadContent();
        downloadContent.execute("http://api.wunderground.com/api/68e30bc28522004d/conditions/q/"+state+"/"+city+".json");

    }
//http://api.wunderground.com/api/68e30bc28522004d/conditions/q/CA/San_Francisco.json
public class DownloadImage extends AsyncTask<String, Void, Bitmap>{

    @Override
    protected Bitmap doInBackground(String... params) {

        try {
            URL iconURL = new URL(params[0]);
            HttpURLConnection theConnection = (HttpURLConnection) iconURL.openConnection();
            InputStream in = null;
            in = theConnection.getInputStream();
            Bitmap iconLOL = BitmapFactory.decodeStream(in);
            return iconLOL;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}

public class DownloadContent extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {

            StringBuffer sb = new StringBuffer("");
            String result = "";
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader br = new BufferedReader(reader);
                String line = br.readLine();

                while(line != null){
                    sb.append(line);
                    line = br.readLine();
                }
                result = sb.toString();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            downImage = new DownloadImage();

            try {
                JSONObject jsonObject = new JSONObject(s);
    iconString = jsonObject.getJSONObject("current_observation").getString("icon_url");
    weatherImage.setImageBitmap(downImage.execute(iconString).get());

    fullName = jsonObject.getJSONObject("current_observation").getJSONObject("display_location").getString("full");
    cityView.setText(fullName);

    weather = jsonObject.getJSONObject("current_observation").getString("weather");
    weatherView.setText(weather);

    temp = jsonObject.getJSONObject("current_observation").getString("temperature_string");
    weatherOutput.setText(temp);


            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, "Could not find weather", Toast.LENGTH_SHORT).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }
}
