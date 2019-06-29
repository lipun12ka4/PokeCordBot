package com.MikeTheShadow.PokeBotMain;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import javax.imageio.ImageIO;
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
                //get and send the url to the thread
                URL url = new URL(embed.getImage().getUrl());
                PokeSolverThread solve = new PokeSolverThread("PokeThread",msg.getTextChannel(),url);
                solve.start();
            }
            catch (IOException e)
            {
                e.printStackTrace();
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
        catch (Exception ignored)
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