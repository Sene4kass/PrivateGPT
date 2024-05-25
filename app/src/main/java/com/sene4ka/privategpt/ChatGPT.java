package com.sene4ka.privategpt;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.StrictMode;

import androidx.core.os.BuildCompat;

import com.google.firebase.BuildConfig;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.util.Properties;
import android.content.Context;

public class ChatGPT extends AsyncTask<String, Void, String> {
    Context context;
    public ChatGPT(Context baseContext) {
        context = baseContext;
    }

    public String chatGPT(String text) throws Exception {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        return this.doInBackground(text);
    }

    public static void main(String[] args) throws Exception {
        //chatGPT("Hello, how are you?");
    }

    @Override
    protected String doInBackground(String... strings) {
        String apiKey;
        try {
            String url = "https://api.openai.com/v1/chat/completions";
            Properties properties = new Properties();
            try {
                AssetManager assetManager = context.getAssets();
                InputStream inputStream = assetManager.open("api.properties");
                properties.load(inputStream);
                apiKey = properties.getProperty("api.key");
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            String model = "gpt-4-turbo";
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            // The request body
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + strings[0] + "\"}]}";
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Response from ChatGPT
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuffer resp = new StringBuffer();

            StringBuffer response = new StringBuffer();

            while ((line = br.readLine()) != null) {
                response.append(line);
                if(line.contains("\"content\":")){
                    resp.append(line);
                }
            }
            br.close();

            // calls the method to extract the message.
            return extractMessageFromJSONResponse(resp.toString());
        }
        catch(Exception e){
            return e.toString();
            }
        }
    public static String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content")+ 11;
        System.out.println(response);

        int end = response.lastIndexOf("\"", response.length());

        return response.substring(start, end);

    }
}
