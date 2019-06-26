package com.MikeTheShadow.PokeBotMain;
import net.dv8tion.jda.core.entities.MessageChannel;
import java.util.Random;


/*
Simple thread that infinitely spams CHARACTER with some variation in timing to avoid any potential time based anticheat
systems TODO keep and eye out for anti-cheat system implementation
 */
public class OnConnect implements Runnable
{
    //try threading
    private  Thread thread;
    private String threadName;
    private MessageChannel channel;

    OnConnect(String name,MessageChannel chan)
    {
        this.channel = chan;
        threadName = name;
    }
    public void run()
    {
        while(true)
        {
            try
            {
                if(!Main.restarting)
                {
                    channel.sendMessage(Main.CHARACTER).complete();
                    Random ran = new Random();
                    Thread.sleep(700 + ran.nextInt(500));
                }
            }
            catch (Exception e)
            {

            }
        }

    }
    public void start()
    {
        if(thread == null)
        {
            thread = new Thread(this,threadName);
            thread.start();
        }
    }




}
