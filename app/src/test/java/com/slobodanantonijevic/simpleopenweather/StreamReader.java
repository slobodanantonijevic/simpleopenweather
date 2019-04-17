package com.slobodanantonijevic.simpleopenweather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamReader {

    public static String readFromStream(String location) {

        InputStream stream = StreamReader.class.getResourceAsStream(location);
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        try {

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                if (builder.length() > 0) {
                    builder.append("\n");
                }
                builder.append(line);
            }
        } catch (IOException ioe) {

            return null;
        }

        return builder.toString();
    }
}
