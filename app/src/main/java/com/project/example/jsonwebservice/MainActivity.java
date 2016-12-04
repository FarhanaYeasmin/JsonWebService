package com.project.example.jsonwebservice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import static com.android.volley.Request.*;

public class MainActivity extends AppCompatActivity {
    EditText cityEt;
    TextView tempTv;
    TextView humidityTv;
    Button btn;
    private ImageView iconIv;

    private String baseUrl = "http://api.openweathermap.org/data/2.5/weather?q=";
    //private String url ="http://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=486a950b581a502bd98d2532b61d1b85";
    private String urlA = "http://api.openweathermap.org/data/2.5/weather?q=";
    private String urlB = "&appid=486a950b581a502bd98d2532b61d1b85";
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tempTv = (TextView) findViewById(R.id.tempTv);
        humidityTv = (TextView) findViewById(R.id.humidityTv);
        iconIv = (ImageView) findViewById(R.id.iconIv);
        cityEt = (EditText) findViewById(R.id.cityName);
    }

    private void setImage(Drawable drawable) {
        iconIv.setBackgroundDrawable(drawable);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Drawable> {

        @Override
        protected Drawable doInBackground(String... params) {
            URL url;
            BufferedOutputStream out;
            InputStream in;
            BufferedInputStream buf;

            try {
                url = new URL(params[0]);
                in = url.openStream();

                // Read the inputstream
                buf = new BufferedInputStream(in);

                // Convert the BufferedInputStream to a Bitmap
                Bitmap bMap = BitmapFactory.decodeStream(buf);
                if (in != null) {
                    in.close();
                }
                if (buf != null) {
                    buf.close();
                }
                return new BitmapDrawable(bMap);
            } catch (Exception e) {
                Log.e("Error reading file", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            setImage(drawable);
        }
    }


    public void searchByCity(View view) {
//        String city = baseUrl + cityEt +url;
        String country = cityEt.getText().toString().trim().toLowerCase();
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlA + country + urlB, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray jsonArray = response.getJSONArray("weather");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String main = jsonObject.getString("main");
                        String description = jsonObject.getString("description");
                        String iconName = jsonObject.getString("icon");
                        final String iconUri = "http://openweathermap.org/img/w/" + iconName + ".png";

//                        Toast.makeText(MainActivity.this, iconUri, Toast.LENGTH_LONG).show();

                        new DownloadImageTask().execute(iconUri);


//                        iconIv.setImageURI(Uri.parse(iconUri));

//                        String icon = jsonObject.getString("icon");
//                        String full =  "main=" + main + " " + description +"id=" +id + " " +"icon=" +icon;

                        //Toast.makeText(MainActivity.this, full, Toast.LENGTH_LONG).show();

//                        if ("haze".equals(description)) {
//                            iconIv.setImageResource(R.drawable.haze);
//                        }
                    }
//
                    JSONObject main = response.getJSONObject("main");
                    String tempareture = main.getString("temp");

//                    Toast.makeText(MainActivity.this, "TEMP:" + tempareture, Toast.LENGTH_LONG).show();

//                    String pressure = (String) main.get("pressure");
                    String humidity = main.getString("humidity");
//
                    tempTv.setText("T: " + tempareture + "°F");
                    humidityTv.setText(", hum: " + humidity + "%");

                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "Not Found:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    Toast.makeText(MainActivity.this, "Please Turn On Your WIFI or Data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }

}







