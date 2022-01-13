/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.gson.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import utils.api.MakeOptions;

public class Utility {
    private static Gson gson = new GsonBuilder().create();
    
    public static String fetchData(String _url, MakeOptions makeOptions) throws MalformedURLException, IOException {
        URL url = new URL(_url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(makeOptions.getMethod());
        
        for (Map.Entry<String, String> set : makeOptions.getHeaders().entrySet()) {
            con.setRequestProperty(set.getKey(), set.getValue());
        }

        String res = new Scanner(con.getInputStream()).useDelimiter("\\Z").next();
        
        return res;
    }

    public static Date stringToDateFormatter (String input) throws Exception {
            try {
                SimpleDateFormat dateParser = new SimpleDateFormat("dd-mm-yy HH:mm");
                Date date = dateParser.parse(input);
                return date;

            } catch (ParseException e) {
                throw new Exception ("Error while formatting date!");
            }
    }

}
