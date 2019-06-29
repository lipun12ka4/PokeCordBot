package com.MikeTheShadow.PokeBotMain;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import java.util.Random;

public class Restart implements Runnable
{
    //try threading
    private  Thread thread;
    private String threadName;

    Restart(String name)
    {
        threadName = name;
    }
    public void run()
    {
        while(true)
        {
            if(thread == null)
            {
                thread = new Thread(this,threadName);
                thread.start();
                return;
            }
            try
            {

                Thread.sleep(3400000 + new Random().nextInt(200000));
                Main.Output("Restarting bot please wait...");
                Main.restarting = true;
                Main.api.shutdown();
                Main.api = new JDABuilder(AccountType.CLIENT).setToken(Main.TOKEN).build();
                Main.api.addEventListener(new Listener());
                Main.Output("Restart complete!");
                Main.restarting = false;
                break;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
