package com.MikeTheShadow.PokeBotMain.Utils;

import java.io.Serializable;

public class PokemonData implements Serializable
{
    private String name;
    private int[][] colorData;
    PokemonData(String name, int[][] colorData)
    {
        this.name = name;
        this.colorData = colorData;
    }
    int[][] getColorData()
    {
        return this.colorData;
    }
    String getName()
    {
        return this.name;
    }



}
