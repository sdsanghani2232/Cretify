package com.sd.certify.helper_class;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Html_View extends AsyncTask<String,Void,String> {

    private OnHtmlFetchedListener listener;

    public Html_View(OnHtmlFetchedListener listener)
    {
        this.listener =listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream stream = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
            }
            return builder.toString();
        } catch (IOException e) {
            Log.d("data","Error");
            return null;
        }
    }

    protected void onPostExecute(String content) {
        if(content != null)
        {
            listener.OnHtmlFetched(content);
        }
    }

    public interface OnHtmlFetchedListener
    {
        void OnHtmlFetched(String code);
    }
}
