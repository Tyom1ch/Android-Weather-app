package com.tyom1ch.myweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import kotlin.Suppress;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTextPersonName;
    private Button button;
    private FloatingActionButton floatingActionButton;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        button = findViewById(R.id.button);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        textView2 = findViewById(R.id.textView2);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"By Tyom1ch" , Toast.LENGTH_SHORT).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextTextPersonName.getText().toString().trim().equals(""))
                    Toast.makeText(MainActivity.this, R.string.err_nt, Toast.LENGTH_SHORT).show();
                else {
                    String city = editTextTextPersonName.getText().toString().trim();
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=1a7fb1f031bbaa526ddc51d305cee2a5&units=metric";

                    new GetURL().execute(url);
                }
            }
        });

    }
    private class GetURL extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textView2.setText("Зачекайте...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");
                return buffer.toString();

            }
            catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null)
                    connection.disconnect();
                if(reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return "Помилка!"; //error when all gets null or other
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject obj = new JSONObject(result);
                String ct = String.valueOf(editTextTextPersonName.getText());

                textView2.setText(ct.substring(0, 1).toUpperCase() + ct.toLowerCase().substring(1) + ":\n" + obj.getJSONObject("main").getDouble("temp") + " °C");

            } catch (JSONException e) {
                e.printStackTrace();
                textView2.setText(" ");
                Toast.makeText(MainActivity.this, R.string.err_nt, Toast.LENGTH_SHORT).show();
            }

            //textView2.setText(result);
        }
    }
}