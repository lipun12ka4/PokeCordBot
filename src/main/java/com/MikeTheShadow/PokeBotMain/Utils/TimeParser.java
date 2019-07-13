package com.MikeTheShadow.PokeBotMain.Utils;

import com.MikeTheShadow.PokeBotMain.Main;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeParser implements Runnable
{
    public String Time;
    Thread thread;
    public TimeParser(String time)
    {
        this.Time = time;
    }
    public boolean ParseTime(int currentTime)
    {
        String[] splitTime = Time.split(",");
        System.out.println("Current Time: " + currentTime);
        for (String time : splitTime)
        {
            if (time.contains("-"))
            {
                String[] abTime = time.split("-");

                int StartTime = Integer.parseInt(abTime[0]);
                int endTime = Integer.parseInt(abTime[1]);
                System.out.println(StartTime + "\n" + endTime);
                if(StartTime <= currentTime && endTime > currentTime)
                {
                    return true;
                }
            }
            else if (Integer.parseInt(time) == currentTime)
            {
                return true;
            }
        }
        return false;
    }
    public void run()
    {

        if(Time.length() < 1)return;
        System.out.println("Schedule found!");
        while(true)
        {
            Date date = new Date();   // given date
            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
            calendar.setTime(date);
            int currentTime = calendar.get(Calendar.HOUR_OF_DAY);
            Main.canRun = ParseTime(currentTime);
            if(Main.stopped)
            {
                return;
            }
            try
            {
                Thread.sleep(3000000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

    }
    public void start()
    {
        if(thread == null)
        {
            thread = new Thread(this,"Time Thread");
            thread.start();
        }
    }
}
