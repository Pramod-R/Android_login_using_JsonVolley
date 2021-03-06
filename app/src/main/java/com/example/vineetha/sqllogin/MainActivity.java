package com.example.vineetha.sqllogin;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
public class MainActivity extends AppCompatActivity {
    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private EditText etEmail;
    private String res;
    private EditText etPassword;
    private TextView Tx1;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get Reference to variables
        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
       // Tx1=(TextView)findViewById(R.id.textView3);

    }
    // Triggers when LOGIN Button clicked
    public void checkLogin(View arg0) {
        // Get text from email and password field
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        // Initialize  AsyncLogin() class with email and password
        new AsyncLogin().execute(email,password);
    }
    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your php file resides
                url = new URL("http://10.0.2.2/integrate/lognew.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]);
                String query = builder.build().getEncodedQuery();

                System.out.println(query);
                //Toast.makeText(MainActivity.this, , Toast.LENGTH_SHORT).show();
                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
            //    String messageString=editMessage.getText().toString();

                //set string from edittext to textview
              //  tx1.setText(toString(query));

                //clear edittext after sending text to message
                writer.write(query);
                writer.flush();
                writer.close();
                //onPostExecute(res);
                os.close();
                conn.connect();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }
            try {
                int response_code = conn.getResponseCode();
                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {
                    // readInputStream();
                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
       //                 onPostExecute(res);
                    }
                    // Pass data to onPostExecute method
                    return (result.toString());
                } else {
                    return ("unsuccessful");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }
           private String readInputStream(InputStream stream) {
               InputStreamReader inputStreamReader = new InputStreamReader(stream);
               BufferedReader reader = new BufferedReader(inputStreamReader);
               StringBuilder response = new StringBuilder();
               String line = null;
               try {
                   while ((line = reader.readLine()) != null) {
                       response.append(line);
                   }
               } catch (IOException e) {
                   Log.e(TAG, "IOException ", e);
               } catch (Exception e) {
                   Log.e(TAG, "Exception", e);
               } finally {
                   try {
                       if (stream != null)
                           stream.close();
                   } catch (IOException e) {
                       Log.e(TAG, "IOException", e);
                   }
               }
               Log.v(TAG, "Response readInputStream-->" + response.toString());
               return response.toString();
           }
        @Override
        protected void onPostExecute(String result) {
            //this method will be running on UI thread
            res = result;
            Toast.makeText(MainActivity.this,res,Toast.LENGTH_SHORT).show();
            pdLoading.dismiss();

            JSONObject jsonObject= null;

                try {
                    jsonObject=new JSONObject(res);
                    String val=jsonObject.getString("msg");
                    if(val.equals("Sucess")){
                        Intent intent=new Intent(MainActivity.this,SuccessActivity.class);
                        startActivity(intent);
                        etEmail.setText("");
                        etPassword.setText("");
                        //  progressDialog.hide();
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(MainActivity.this, "Enter Valid Details", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            /*if () {
                Intent intent = new Intent(MainActivity.this, SuccessActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
            else
            {
                Toast.makeText(MainActivity.this, "Not successful", Toast.LENGTH_SHORT).show();

            }*/
            /* if (result.equalsIgnoreCase("true")) {
                *//* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 *//*
                Toast.makeText(MainActivity.this, "successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, SuccessActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            } else if (result.equalsIgnoreCase("false")) {
                // If username and password does not match display a error message
                Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                Toast.makeText(MainActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
            }
        */
        }
    }
}
