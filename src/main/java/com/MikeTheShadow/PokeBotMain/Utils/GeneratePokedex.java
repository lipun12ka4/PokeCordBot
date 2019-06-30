package com.MikeTheShadow.PokeBotMain.Utils;

import com.MikeTheShadow.PokeBotMain.Main;
import com.MikeTheShadow.PokeBotMain.MainPokeBotWindow;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GeneratePokedex implements Runnable
{
    private  Thread thread;
    private String threadName;
    public GeneratePokedex(String name)
    {
        this.threadName = name;
    }
    public void run()
    {
        File dex = new File("pokedex.dat");
        if(!dex.exists())
        {
            Main.Output("Dex does not exist generating...");
            generateNewDex();
            Main.Output("Dex created!");
        }
        Main.pokemonData = loadPokedex();
        MainPokeBotWindow.loadImagelabel.setText("Complete!");
        Main.StartMainThread();
    }
    public void start()
    {
        if(thread == null)
        {
            thread = new Thread(this,threadName);
            thread.start();
        }
    }
    public List<PokemonData> loadPokedex()
    {

        try {
            FileInputStream fileIn = new FileInputStream("pokedex.dat");
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Object obj = objectIn.readObject();
            objectIn.close();
            Main.pokemonData = (List<PokemonData>) obj;
            System.out.println("pokedex loaded to memory!");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Main.pokemonData;
    }
    public void generateNewDex()
    {
        File folder = new File("pokedex");
        File[] pokemonIndex = folder.listFiles();
        assert pokemonIndex != null;

        MainPokeBotWindow.pokemonLoadingBar.setMaximum(pokemonIndex.length);
        MainPokeBotWindow.pokemonLoadingBar.setValue(0);
        for (File pokemon : pokemonIndex)
        {
            try
            {
                String pokename = pokemon.getName().substring(0, pokemon.getName().length() - 4).toLowerCase();
                MainPokeBotWindow.loadImagelabel.setText("Loading image: " + pokename);
                if(pokename.equals("typenull"))
                {
                    pokename = "type: null";
                }
                CustomImageReader(ImageIO.read(pokemon), pokename);
                MainPokeBotWindow.pokemonLoadingBar.setValue(MainPokeBotWindow.pokemonLoadingBar.getValue() + 1);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        WriteObjectToFile(Main.pokemonData);
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
        for(int x = 0; x < 240; x += Main.spacing)
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
        Main.pokemonData.add(new PokemonData(pokemonName,storageInts));
        System.out.println(pokemonName);
    }


}