package com.MikeTheShadow.PokeBotMain;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class Listener extends ListenerAdapter
{
    //public static int storageInt = 0;
    @Override
    public void onMessageReceived(MessageReceivedEvent msg)
    {
        if(!msg.getAuthor().isBot() || !msg.getAuthor().getId().equals("365975655608745985") || msg.getChannel().getType() == ChannelType.PRIVATE)return;

        if(msg.getMessage().getEmbeds().size() < 1) return;

        try
        {
            if(msg.getMessage().getEmbeds().get(0).getDescription().contains(" is now"))
            {
                return;
            }
        }
        catch (Exception e)
        {
            return;
        }
        if(msg.getMessage().getEmbeds().get(0).getDescription().contains("Guess the pokémon and type p!catch <pokémon> to catch it!"))
        {
            try
            {
                MessageEmbed embed = msg.getMessage().getEmbeds().get(0);
                System.setProperty("http.agent", "Chrome");
                URL url = new URL(embed.getImage().getUrl());
                /*
                InputStream in = new BufferedInputStream(url.openStream());
                OutputStream out = new BufferedOutputStream(new FileOutputStream("tempfiles/temp" + storageInt + ".jpg"));
                for ( int i; (i = in.read()) != -1; )
                {
                    out.write(i);
                }
                in.close();
                out.close();
                */
                BufferedImage image = ImageIO.read(url);
                //storageInt++;
                PokeSolverThread solve = new PokeSolverThread("PokeThread",msg.getTextChannel(),image);
                solve.start();
            }
            catch (Exception e)
            {
                e.getMessage();
            }
        }
    }
    @Override
    public void onReady(ReadyEvent event)
    {
        try
        {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }
        catch (Exception e)
        {

        }
        Main.CHANNEL = Main.api.getTextChannelById(Main.channelid);
        if(Main.channelid != null && Main.sendMessages)
        {
            Main.Output("Setting channel to: " + Main.CHANNEL.getName());
            OnConnect newThread = new OnConnect("MessageThread",Main.CHANNEL);
            newThread.start();
            //Restart restartThread = new Restart("restartThread");
            //restartThread.run();
        }
    }

}