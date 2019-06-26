package com.MikeTheShadow.PokeBotMain.Utils;

import java.io.Serializable;

public class PokemonData implements Serializable
{
    private String name;
    private int[][] colorData;
    public PokemonData(String name, int[][] colorData)
    {
        this.name = name;
        this.colorData = colorData;
    }
    public int[][] getColorData()
    {
        return this.colorData;
    }
    public String getName()
    {
        return this.name;
    }



}
