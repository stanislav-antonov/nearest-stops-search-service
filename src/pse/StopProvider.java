package pse;

import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Arrays;
import java.util.logging.Logger;

import static pse.Helper.rcOk;

public class StopProvider {

    private final Config mConfig;
    private final Logger mLogger;

    StopProvider(Config config, Logger logger) {
        mConfig = config;
        mLogger = logger;
    }

    public List<Stop> getList() throws Exception {
        final URI uri = mConfig.getStopDatasourceUrl();
        final URL url = new URL(uri.toString());

        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        // connection.setRequestProperty("Content-Type", "application/json");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        connection.connect();

        final int status = connection.getResponseCode();
        if (status != rcOk) {
            return null;
        }

        final InputStream inputStream = connection.getInputStream();
        final InputStreamReader reader = new InputStreamReader(inputStream);
        final Stop[] stops = new Gson().fromJson(reader, Stop[].class);

        return Arrays.asList(stops);
    }

    static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
