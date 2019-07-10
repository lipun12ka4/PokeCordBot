package com.MikeTheShadow.PokeBotMain.Utils;

import com.MikeTheShadow.PokeBotMain.Main;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class VersionChecker
{
    static String version;
    public static boolean CheckVersion()
    {
        int code;
        try
        {
            code = getResponseCode("https://github.com/MikeTheShadow/PokeCordBot/blob/master/" + Main.VERSION);
        }
        catch (Exception e)
        {
            code = -1;
            e.printStackTrace();
        }

        if(code == 200) return true;
        return false;
    }
    public static int getResponseCode(String urlString) throws MalformedURLException, IOException
    {
        URL u = new URL(urlString);
        HttpURLConnection huc =  (HttpURLConnection)  u.openConnection();
        huc.setRequestMethod("GET");
        huc.connect();
        return huc.getResponseCode();
    }
}
