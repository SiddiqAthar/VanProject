package com.example.privatevanmanagement.utils;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SendNotification extends AsyncTask<Object, Void, String> {
    @Override
    protected String doInBackground(Object... params) {
        ArrayList<String> users = (ArrayList<String>) params[0];
        String body = (String) params[1];
        try {

            final String apiKey = "AAAAVWpukD4:APA91bF2NLmTyCpQfJYPT7_nZNZ4O2OJKeIEvAn5ZjRRmsOu-TByqLWZWVN7Tkp1BlncDdx-1JdEgrviefrJm2njnuwqP0g4rSAA5kVGtVLEfPF4L8hkiv6ZzQn3fM-nvJGA7OtonbqX";
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key=" + apiKey);
            conn.setDoOutput(true);
            for(int i=0;i<users.size();i++)
            {
                JSONObject message = new JSONObject();
                message.put("to", users.get(i));
                message.put("priority", "high");

                JSONObject notification = new JSONObject();
                notification.put("title", "New Announcment");
                notification.put("body", body);
                message.put("data", notification);
                OutputStream os = conn.getOutputStream();
                os.write(message.toString().getBytes());
                os.flush();
                os.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }

             return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("msg", s);

    }
}
