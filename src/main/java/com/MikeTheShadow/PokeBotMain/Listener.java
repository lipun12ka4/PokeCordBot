package com.MikeTheShadow.PokeBotMain;

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
            String username = Main.api.getSelfUser().getName().toLowerCase().split(" ")[0].replaceAll("[^a-zA-Z0-9]", "");
            String input =  msg.getMessage().getEmbeds().get(0).getTitle().toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
            if(msg.getMessage().getEmbeds().get(0).getTitle().contains("Congratulations") && input.contains(username))
            {
                if(msg.getMessage().getEmbeds().get(0).getDescription().contains("100!"))
                {
                    assert  Main.levelList.size() > 0;
                    msg.getChannel().sendMessage(Main.PREFIX + "select " + Main.levelList.get(0));
                    Main.levelList.remove(0);
                }
                return;
            }
        }
        catch (Exception e)
        {
            return;
        }
        if(msg.getMessage().getEmbeds().get(0).getDescription().contains("Guess the pokémon and type"))
        {
            try
            {
                MessageEmbed embed = msg.getMessage().getEmbeds().get(0);
                System.out.println("DEBUG URL: " + embed.getImage().getUrl());
                URL url = new URL(embed.getImage().getUrl());
                try
                {
                    //BufferedImage image = ImageIO.read(url);
                    PokeSolverThread solve = new PokeSolverThread("PokeThread",msg.getTextChannel(),url);
                    solve.start();
                }
                catch (Exception e)
                {
                    System.out.println("THIS IS A HTTP.AGENT ERROR! Please report it in the not catching pokemon thread thansk!");
                    e.printStackTrace();

                }


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
        }
    }

}