package com.MikeTheShadow.PokeBotMain;

public class Shutdown implements Runnable
{
    //try threading
    private  Thread thread;
    private String threadName;

    Shutdown(String name)
    {
        threadName = name;
    }
    public void run()
    {
            if(thread == null)
            {
                thread = new Thread(this,threadName);
                thread.start();
                return;
            }
            try
            {
                if((int)MainPokeBotWindow.timeout.getValue() < 1)
                {
                    return;
                }
                Main.Output("Will shutdown automatically in " + MainPokeBotWindow.timeout.getValue() + " hours.");
                Thread.sleep((int)MainPokeBotWindow.timeout.getValue() * 3600000);
                Main.stopped = true;
                Main.api.shutdown();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
    }
}
