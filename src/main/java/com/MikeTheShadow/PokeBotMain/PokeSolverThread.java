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
                output.createNewFile();
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
    //Compare the two images on a random pixelList there is a small chance that it'll screw up but hasn't happened yet
    private static int CustomImageReader(BufferedImage imgA, BufferedImage imgB)
    {
        int width1 = imgA.getWidth();
        int width2 = imgB.getWidth();
        int height1 = imgA.getHeight();
        int height2 = imgB.getHeight();

        if ((width1 != width2) || (height1 != height2)) return -1;
        else
        {
            Random random = new Random();
            ArrayList<Integer> xlist = new ArrayList<>();
            ArrayList<Integer> ylist = new ArrayList<>();
            int pixel_amount = 100;
            do
            {
                int ran1 = random.nextInt(height1);
                int ran2 = random.nextInt(width1);

                if (!xlist.contains(ran1) || xlist.size() <= pixel_amount)
                {
                    xlist.add(ran1);
                }
                if (!ylist.contains(ran2) || ylist.size() <= pixel_amount)
                {
                    ylist.add(ran2);
                }

            } while (ylist.size() < pixel_amount || xlist.size() < pixel_amount);
            long difference = 0;
            for (int y = 0; y < xlist.size(); y++)
            {
                for (int x = 0; x < ylist.size(); x++)
                {
                    int rgbA = imgA.getRGB(xlist.get(x), ylist.get(y));
                    int rgbB = imgB.getRGB(xlist.get(x), ylist.get(y));
                    int redA = (rgbA >> 16) & 0xff;
                    int greenA = (rgbA >> 8) & 0xff;
                    int blueA = (rgbA) & 0xff;
                    int redB = (rgbB >> 16) & 0xff;
                    int greenB = (rgbB >> 8) & 0xff;
                    int blueB = (rgbB) & 0xff;
                    difference += Math.abs(redA - redB);
                    difference += Math.abs(greenA - greenB);
                    difference += Math.abs(blueA - blueB);
                    if(difference > 0) return -1;
                }
            }
            return 0;
        }
    }
    //Old method of image reading still usable but 10x slower. New version can guess a pokemon faster than the image can load for the average user lol
    @Deprecated
    public static double ImageReader(BufferedImage imgA, BufferedImage imgB)
    {
        int width1 = imgA.getWidth();
        int width2 = imgB.getWidth();
        int height1 = imgA.getHeight();
        int height2 = imgB.getHeight();

        if ((width1 != width2) || (height1 != height2)) return -1;
        else
        {
            long difference = 0;
            for (int y = 0; y < height1; y++)
            {
                for (int x = 0; x < width1; x++)
                {
                    int rgbA = imgA.getRGB(x, y);
                    int rgbB = imgB.getRGB(x, y);
                    int redA = (rgbA >> 16) & 0xff;
                    int greenA = (rgbA >> 8) & 0xff;
                    int blueA = (rgbA) & 0xff;
                    int redB = (rgbB >> 16) & 0xff;
                    int greenB = (rgbB >> 8) & 0xff;
                    int blueB = (rgbB) & 0xff;
                    difference += Math.abs(redA - redB);
                    difference += Math.abs(greenA - greenB);
                    difference += Math.abs(blueA - blueB);
                    if(difference > 0) return -1;
                }
            }
            return 0;
        }
    }



}
