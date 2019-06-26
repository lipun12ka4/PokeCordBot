package com.MikeTheShadow.PokeBotMain.Utils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GeneratePokedex
{
    private int spaceSize = 40;
    private List<PokemonData> pokemonData = new ArrayList<>();
    public List<PokemonData> loadPokedex() {

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
    public void loadImagesToArray()
    {
        File folder = new File("pokedex");
        File[] pokemonIndex = folder.listFiles();
        if(pokemonIndex != null)
        {
            for(int i = 0; i < pokemonIndex.length; i++)
            {
                try
                {
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    public void start()
    {
        File folder = new File("pokedex");
        File[] pokemonIndex = folder.listFiles();
        assert pokemonIndex != null;
        for(int i = 0; i < pokemonIndex.length; i++)
        {
            try
            {
                CustomImageReader(ImageIO.read(pokemonIndex[i]),pokemonIndex[i].getName().substring(0,pokemonIndex[i].getName().length() - 4));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        WriteObjectToFile(pokemonData,"pokedex.dat");
        System.out.println("New pokedex generated");
    }
    public void WriteObjectToFile(Object serObj,String filepath)
    {

        try
        {

            FileOutputStream fileOut = new FileOutputStream(filepath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serObj);
            objectOut.close();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public double CustomImageReader(BufferedImage knownPokemon,String pokemonName)
    {
        List<Integer> xlist = new ArrayList<>();
        List<Integer> ylist = new ArrayList<>();
        for(int x = 0; x < 240;x += spaceSize)
        {
            xlist.add(x);
            ylist.add(x);
        }
        long difference = 0;
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
        //System.out.println("Registered: " + pokemonName);
        if(difference > 0) return -1;
        return 0;
    }


}