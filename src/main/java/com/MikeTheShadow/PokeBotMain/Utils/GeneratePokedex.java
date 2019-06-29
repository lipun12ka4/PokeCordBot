package com.MikeTheShadow.PokeBotMain.Utils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GeneratePokedex
{
    private List<PokemonData> pokemonData = new ArrayList<>();
    public List<PokemonData> loadPokedex()
    {

        try {
            FileInputStream fileIn = new FileInputStream("pokedex.dat");
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Object obj = objectIn.readObject();
            objectIn.close();
            pokemonData = (List<PokemonData>) obj;
            System.out.println("pokedex loaded to memory!");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this.pokemonData;
    }
    public void start()
    {
        File folder = new File("pokedex");
        File[] pokemonIndex = folder.listFiles();
        assert pokemonIndex != null;
        for (File pokemon : pokemonIndex)
        {
            try
            {
                //typenull
                String pokename = pokemon.getName().substring(0, pokemon.getName().length() - 4).toLowerCase();
                if(pokename.equals("typenull"))
                {
                    pokename = "type: null";
                }
                CustomImageReader(ImageIO.read(pokemon), pokename);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        WriteObjectToFile(pokemonData);
        System.out.println("New pokedex generated");
    }
    private void WriteObjectToFile(Object serObj)
    {
        try
        {

            FileOutputStream fileOut = new FileOutputStream("pokedex.dat");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serObj);
            objectOut.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private void CustomImageReader(BufferedImage knownPokemon, String pokemonName)
    {
        List<Integer> xlist = new ArrayList<>();
        List<Integer> ylist = new ArrayList<>();
        int spaceSize = 35;
        for(int x = 0; x < 240; x += spaceSize)
        {
            xlist.add(x);
            ylist.add(x);
        }
        int[][] storageInts = new int[xlist.size()][xlist.size()];
        for (int y = 0; y < xlist.size(); y++)
        {
            for (int x = 0; x < ylist.size(); x++)
            {
                int rgbB = knownPokemon.getRGB(xlist.get(x), ylist.get(y));
                storageInts[x][y] = rgbB;
            }
        }
        pokemonData.add(new PokemonData(pokemonName,storageInts));
    }


}