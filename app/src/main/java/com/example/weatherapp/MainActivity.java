package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText userInput;
    TextView textView;

    public void clicker(View view){
        userInput = findViewById(R.id.editTextTextPersonName);
        downloadData task = new downloadData();
        String city = userInput.getText().toString();
        try {
            String data = task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=5971357eb8a3895b6b334c3c26cc9e3a").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        InputMethodManager methodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        methodManager.hideSoftInputFromWindow(userInput.getWindowToken(),0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView2);

    }

    public class downloadData extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection httpURLConnection = null;

            try {
                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                while (data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                try {
                    String main, description;
                    JSONObject jsonObject = new JSONObject(s);
                    String weatherInfo = jsonObject.getString("weather");
                    JSONArray jsonArray = new JSONArray(weatherInfo);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject current = jsonArray.getJSONObject(i);
                        main = current.getString("main");
                        description = current.getString("description");
                        textView.setText(main + ": " + description);
                        Log.i("beeb", current.getString("main"));
                        Log.i("beeb", current.getString("description"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                textView.setText("Enter the name correctly!");
            }


        }
    }
}