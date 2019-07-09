package com.MikeTheShadow.PokeBotMain.Utils;

import com.MikeTheShadow.PokeBotMain.Main;

public class VersionChecker
{
    String version;
    public VersionChecker()
    {

    }
    public boolean CheckVersion()
    {
        if(this.version.equals(Main.VERSION)) return true;
        return false;
    }

}
