package com.MikeTheShadow.PokeBotMain;
import com.MikeTheShadow.PokeBotMain.Utils.ImageReader;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class PokeSolverThread implements Runnable
{
    private  Thread thread;
    private String threadName;
    private TextChannel channel;
    private BufferedImage imageToRead;
    private URL pokemonURL;
    PokeSolverThread(String name, TextChannel chan,URL url) throws IOException
    {
        this.channel = chan;
        threadName = name;
        System.setProperty("http.agent", "Mozilla/5.0");
        BufferedImage image = getImageFromURL(url);
        this.imageToRead = image;
        this.pokemonURL = url;
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
        channel.sendMessage(Main.PREFIX + "catch " + pokemonName).complete();
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
    private BufferedImage getImageFromURL(URL url) throws IOException
    {
        URLConnection openConnection = url.openConnection();
        boolean check = true;

        try
        {

            openConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            openConnection.connect();

            if (openConnection.getContentLength() > 8000000)
            {
                System.out.println(" file size is too big.");
                check = false;
            }
        } catch (Exception e)
        {
            System.out.println("Couldn't create a connection to the link, please recheck the link.");
            check = false;
            e.printStackTrace();
        }
        if (check)
        {
            BufferedImage img = null;
            try
            {
                InputStream in = new BufferedInputStream(openConnection.getInputStream());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int n = 0;
                while (-1 != (n = in.read(buf)))
                {
                    out.write(buf, 0, n);
                }
                out.close();
                in.close();
                byte[] response = out.toByteArray();
                img = ImageIO.read(new ByteArrayInputStream(response));
                return img;
            } catch (Exception e)
            {
                System.out.println(" couldn't read an image from this link.");
                e.printStackTrace();
            }
        }
        return null;
    }

}
