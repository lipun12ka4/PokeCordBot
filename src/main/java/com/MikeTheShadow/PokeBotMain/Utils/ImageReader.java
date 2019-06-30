package com.MikeTheShadow.PokeBotMain.Utils;

import com.MikeTheShadow.PokeBotMain.Main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class ImageReader
{


    private BufferedImage pokemon;
    private List<PokemonData> pokedex;

    public ImageReader(BufferedImage unkownPokemon, List<PokemonData> dataList)
    {
        this.pokemon = unkownPokemon;
        this.pokedex = dataList;
    }
    public String start()
    {
        for (PokemonData pokedex1 : pokedex)
        {
            String pokename = CustomImageReader(pokemon, pokedex1);
            if (pokename != null) return pokename;
        }
        return null;
    }
    private static String CustomImageReader(BufferedImage unknownPokemon, PokemonData pokemonData)
    {
        List<Integer> xlist = new ArrayList<>();
        List<Integer> ylist = new ArrayList<>();
        for(int x = 0; x < 240; x += Main.spacing)
        {
            xlist.add(x);
            ylist.add(x);
        }
        long difference = 0;
        for (int y = 0; y < xlist.size(); y++)
        {
            for (int x = 0; x < ylist.size(); x++)
            {
                int rgbA = unknownPokemon.getRGB(xlist.get(x), ylist.get(y));
                int rgbB = pokemonData.getColorData()[x][y];
                int redA = (rgbA >> 16) & 0xff;
                int greenA = (rgbA >> 8) & 0xff;
                int blueA = (rgbA) & 0xff;
                int redB = (rgbB >> 16) & 0xff;
                int greenB = (rgbB >> 8) & 0xff;
                int blueB = (rgbB) & 0xff;
                difference += Math.abs(redA - redB);
                difference += Math.abs(greenA - greenB);
                difference += Math.abs(blueA - blueB);
                if(difference > 0) break;
            }
            if(difference > 0)break;
        }
        if(difference > 0) return null;
        return pokemonData.getName();
    }
}

