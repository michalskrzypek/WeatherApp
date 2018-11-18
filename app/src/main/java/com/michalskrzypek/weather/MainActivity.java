package com.michalskrzypek.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button theButton;
    private EditText etCityName, etStateName;
    private TextView tvCity, tvWeather, tvTemp;
    private ImageView imgWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCityName = (EditText) findViewById(R.id.cityName);
        etStateName = (EditText) findViewById(R.id.stateName);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvWeather = (TextView) findViewById(R.id.tvWeather);
        tvTemp = (TextView) findViewById(R.id.tvTemperature);
        imgWeather = (ImageView) findViewById(R.id.imgWeather);
        theButton = (Button) findViewById(R.id.theButton);
        theButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWeather();
            }
        });
    }

    private void getWeather() {
        if (checkConnection(this)) {
            String cityName = etCityName.getText().toString();
            cityName = cityName.replaceAll(" ", "_");
            String stateName = etStateName.getText().toString().toUpperCase();

            DownloadContent downloadContent = new DownloadContent();
            downloadContent.execute("http://api.wunderground.com/api/68e30bc28522004d/conditions/q/" + stateName + "/" + cityName + ".json");

            hideKeyboard(etStateName.getWindowToken());
        }
    }

    public boolean checkConnection(Context context) {
        if (isOnline()) {
            Toast.makeText(context, "You are connected to Internet", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private void hideKeyboard(IBinder windowToken) {
        InputMethodManager mngr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mngr.hideSoftInputFromWindow(windowToken, 0);
    }

    public class DownloadContent extends AsyncTask<String, Void, City> {

        @Override
        protected City doInBackground(String... params) {
            DownloadImage downImage = new DownloadImage();

            JSONObject jsonObject = null;
            String iconUrl = null;
            String cityName = null;
            String state = null;
            String country = null;
            String weather = null;
            String temp = null;
            try {
                jsonObject = new JSONObject(getJsonString(params[0]));
                iconUrl = jsonObject.getJSONObject("current_observation").getString("icon_url");
                downImage.execute(iconUrl);
                cityName = jsonObject.getJSONObject("current_observation").getJSONObject("display_location").getString("city");
                state = jsonObject.getJSONObject("current_observation").getJSONObject("display_location").getString("state");
                country = jsonObject.getJSONObject("current_observation").getJSONObject("display_location").getString("country");
                weather = jsonObject.getJSONObject("current_observation").getString("weather");
                temp = jsonObject.getJSONObject("current_observation").getString("temperature_string");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            return new City.Builder()
                    .name(cityName)
                    .state(state)
                    .country(country)
                    .weather(weather)
                    .temperature(temp)
                    .build();
        }

        private String getJsonString(String urlString) {
            StringBuffer sb = new StringBuffer();
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader br = new BufferedReader(reader);
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(City city) {
            super.onPostExecute(city);
            setWeather(city);
        }
    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap icon = null;
            try {
                URL iconResource = new URL(params[0]);
                HttpURLConnection theConnection = (HttpURLConnection) iconResource.openConnection();
                InputStream in = theConnection.getInputStream();
                icon = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return icon;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            setWeatherIcon(bitmap);
        }
    }

    private void setWeather(City city) {
        tvCity.setText(city.getFullName());
        tvWeather.setText(city.getWeather());
        tvTemp.setText(city.getTemperature());
    }

    private void setWeatherIcon(Bitmap icon) {
        imgWeather.setImageBitmap(icon);
    }
}
