package com.MikeTheShadow.PokeBotMain;
import com.MikeTheShadow.PokeBotMain.Utils.ImageReader;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class PokeSolverThread implements Runnable
{
    private  Thread thread;
    private String threadName;
    private TextChannel channel;
    private BufferedImage imageToRead;
    private URL pokemonURL;
    PokeSolverThread(String name, TextChannel chan,BufferedImage image,URL url) throws IOException
    {
        this.channel = chan;
        threadName = name;
        this.imageToRead = image;
        this.pokemonURL = url;
    }
    public void run()
    {
        try
        {
            Main.Output("DEBUG: dex size = " + Main.pokemonData.size());
            //Index all the pokemon into an array
            File folder = new File("pokedex");
            File[] pokemonIndex = folder.listFiles();
            if(pokemonIndex != null)
            {
                ImageReader imageReader = new ImageReader(imageToRead,Main.pokemonData);
                String pokemonName = imageReader.start();
                //For realistic catching
                if(Main.realisticCatch)Thread.sleep(3000 + new Random().nextInt(1000));
                //logic for user settings

                if(pokemonName == null)
                {
                    writeToFile();
                    Main.Output("Could not find pokemon! Please follow instructions in unknown folder");
                }
                else if(Main.catchOnlyWhiteListed && Main.whitelist.contains(pokemonName) && channel == Main.CHANNEL)
                {
                    sendCatchMessage(pokemonName);
                }
                else if(!Main.catchOnlyWhiteListed && channel == Main.CHANNEL)
                {
                    sendCatchMessage(pokemonName);
                }
                else if(Main.catchOutsideChannel && channel != Main.CHANNEL && Main.whitelist.contains(pokemonName))
                {
                    sendCatchMessage(pokemonName);
                }
                else if(Main.catchEverythingEverywhere)
                {
                    sendCatchMessage(pokemonName);
                }
                MainPokeBotWindow.output.select(MainPokeBotWindow.output.getRows() - 1);
            }
        }
        //Basically if a pokemon can't be found
        catch (Exception e)
        {
                writeToFile();
        }
    }
    private void writeToFile()
    {
        Random random = new Random(1520921095);
        try
        {
            String filename = "Unknown/id" + random.nextInt(1000000)+".txt";
            File file = new File(filename);
            file.createNewFile();
            FileWriter fw = new FileWriter(filename);
            fw.write(pokemonURL.getFile());
            fw.close();
        }
        catch(Exception e)
        {
            Main.Output(e.getMessage());
        }
    }

    private void sendCatchMessage(String pokemonName)
    {
        channel.sendTyping().complete();
        channel.sendMessage("p!catch " + pokemonName).complete();
        if(Main.showOnlyWhiteListed)
        {
            if(Main.whitelist.contains(pokemonName.toLowerCase()))Main.Output("Found a " + pokemonName + "!");
        }
        else
        {
            Main.Output("Found a " + pokemonName + "!");
        }
    }

    void start()
    {
        if(thread == null)
        {
            thread = new Thread(this,threadName);
            thread.start();
        }
    }
}
