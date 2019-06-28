package com.MikeTheShadow.PokeBotMain;
import com.MikeTheShadow.PokeBotMain.Utils.ImageReader;
import net.dv8tion.jda.core.entities.MessageChannel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class PokeSolverThread implements Runnable
{
    private  Thread thread;
    private String threadName;
    private MessageChannel channel;
    private BufferedImage imageToRead;
    PokeSolverThread(String name,MessageChannel chan,BufferedImage image)
    {
        this.channel = chan;
        threadName = name;
        this.imageToRead = image;
    }
    public void run()
    {
        try
        {
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
            BufferedImage bi = this.imageToRead;
            Random random = new Random(1520921095);
            File output = new File("unknown/" + random.nextInt(1000000)+".jpg");
            Main.Output("Unknown pokemon error!");
            try
            {
                if(!output.createNewFile())return;
            } catch (IOException e1)
            {
                e1.printStackTrace();
            }
            try
            {
                ImageIO.write(bi,"jpg",output);
            }
            catch (IOException exception)
            {
                Main.Output("Error saving image to file!\n" + exception.getMessage());
            }
        }
    }
    private void writeToFile()
    {
        BufferedImage bi = this.imageToRead;
        Random random = new Random(1520921095);
        File output = new File("Unknownpokemon/" + random.nextInt(1000000)+".jpg");
        try
        {
            ImageIO.write(bi,"jpg",output);
        }
        catch (IOException exception)
        {
            Main.Output("Error saving image to file!\n" + exception.getMessage());
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
