package com.MikeTheShadow.PokeBotMain;

import com.MikeTheShadow.PokeBotMain.Utils.GeneratePokedex;
import com.MikeTheShadow.PokeBotMain.Utils.PokemonData;
import com.MikeTheShadow.PokeBotMain.Utils.VersionChecker;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main
{
    static JDA api;
    static List<String> whitelist = new ArrayList<>();
    static TextChannel CHANNEL = null;
    private static String TOKEN = null;
    static String CHARACTER = ".";
    static String PREFIX = "p!";
    static String channelid = null;
    static boolean sendMessages = true;
    static boolean catchOnlyWhiteListed = false;
    static boolean catchOutsideChannel = false;
    static boolean catchEverythingEverywhere = false;
    static boolean realisticCatch = true;
    static boolean showOnlyWhiteListed = false;
    //list of pokemon to level
    static List<String> levelList = new ArrayList<>();
    //version checking
    public static final String VERSION = "1.2.1";
    //New pokemon data much lighter and way more efficient
    public static List<PokemonData> pokemonData = new ArrayList<>();

    //use this to change the image spacing size
    public static int spacing = 35;

    //restart booleans etc
    static boolean stopped = false;

    static void Start()
    {
        if(!VersionChecker.CheckVersion())
        {
            Output("Bot outdated! Please download the latest version!");
            return;
        }
        //Load the user/legendary lists
        LoadLists();
        GeneratePokedex genDex = new GeneratePokedex("genDex");
        genDex.start();
    }
    public static void StartMainThread()
    {
        try
        {
            api = new JDABuilder(AccountType.CLIENT).setToken(TOKEN).build();
            api.addEventListener(new Listener());
            Shutdown shutdownThread = new Shutdown("restart after delay");
            shutdownThread.run();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public static void Output(String output)
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        MainPokeBotWindow.output.add(sdf.format(cal.getTime()) + ": " + output);
    }

    static boolean LoadSetup() throws Exception
    {
        Properties properties = new Properties();
        File propFile = new File("pokebot.properties");
        if(!propFile.exists())
        {
            CreateProperties();
            return true;
        }
        FileInputStream inStream = new FileInputStream(propFile);
        properties.load(inStream);
        try
        {
            Main.Output("Loading properties file...");
            PREFIX = properties.getProperty("PREFIX");
            CHARACTER = properties.getProperty("CHARACTER");
            channelid = properties.getProperty("CHANNELID");
            TOKEN = properties.getProperty("TOKEN");
            sendMessages = Boolean.parseBoolean(properties.getProperty("SENDMESSAGES").toLowerCase());
            catchOnlyWhiteListed = Boolean.parseBoolean(properties.getProperty("WHITELIST").toLowerCase());
            catchOutsideChannel = Boolean.parseBoolean(properties.getProperty("CATCHOUTSIDE").toLowerCase());
            catchEverythingEverywhere = Boolean.parseBoolean(properties.getProperty("CATCHEVERYTHING").toLowerCase());
            realisticCatch = Boolean.parseBoolean(properties.getProperty("REALISTICCATCH").toLowerCase());
            showOnlyWhiteListed = Boolean.parseBoolean(properties.getProperty("SHOWONLYWHITELIST").toLowerCase());
            try
            {
                levelList = Arrays.asList(properties.getProperty("LEVELLIST").split(" "));
            }
            catch (Exception e)
            {
                levelList = null;
                System.out.println("levelList empty ignoring...");
            }
            if(properties.getProperty("CHARACTER") != null)CHARACTER = properties.getProperty("CHARACTER");
            if(TOKEN == null || TOKEN.length() < 5)return true;
            MainPokeBotWindow.tokenBox.setText(TOKEN);
            MainPokeBotWindow.channelBox.setText(channelid);
            MainPokeBotWindow.SpamBox.setText(CHARACTER);
            MainPokeBotWindow.prefixBox.setText(PREFIX);
            if(levelList != null)
            {
                MainPokeBotWindow.pokemonLevelList.setText("");
                for (String pokemon:levelList)
                {
                    MainPokeBotWindow.pokemonLevelList.append(pokemon + "\n");
                }
            }
            MainPokeBotWindow.load();
            Main.Output("Complete!");
            SaveProperties();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Main.Output("Please fill out settings tab");
            CreateProperties();
        }
        return false;
    }
    static void CreateProperties() throws IOException
    {
        Properties properties = new Properties();
        properties.setProperty("CHANNELID","");
        properties.setProperty("TOKEN","");
        properties.setProperty("WHITELIST","false");
        properties.setProperty("CATCHOUTSIDE","false");
        properties.setProperty("CATCHEVERYTHING","false");
        properties.setProperty("REALISTICCATCH","false");
        properties.setProperty("SHOWONLYWHITELIST","false");
        properties.setProperty("SENDMESSAGES","false");
        properties.setProperty("CHARACTER","putcharhere");
        properties.setProperty("PREFIX","p!");
        properties.setProperty("LEVELLIST","");
        properties.store(new FileOutputStream("pokebot.properties"),null);
    }
    static void SaveProperties()
    {
        catchOnlyWhiteListed = MainPokeBotWindow.catchOnlyWhitelisted.isSelected();
        catchOutsideChannel = MainPokeBotWindow.CatchOutsideChannel.isSelected();
        catchEverythingEverywhere = MainPokeBotWindow.CatchEverything.isSelected();
        realisticCatch = MainPokeBotWindow.realisticCatch.isSelected();
        showOnlyWhiteListed = MainPokeBotWindow.ShowOnlyWhitelisted.isSelected();
        sendMessages = MainPokeBotWindow.sendMessages.isSelected();
        TOKEN = MainPokeBotWindow.tokenBox.getText();
        channelid = MainPokeBotWindow.channelBox.getText();
        CHARACTER = MainPokeBotWindow.SpamBox.getText();
        PREFIX = MainPokeBotWindow.prefixBox.getText();
        levelList = Arrays.asList(MainPokeBotWindow.pokemonLevelList.getText().split("\n"));
        Properties properties = new Properties();
        properties.setProperty("CHANNELID",channelid);
        properties.setProperty("TOKEN",TOKEN);
        properties.setProperty("WHITELIST",String.valueOf(catchOnlyWhiteListed));
        properties.setProperty("CATCHOUTSIDE",String.valueOf(catchOutsideChannel));
        properties.setProperty("CATCHEVERYTHING",String.valueOf(catchEverythingEverywhere));
        properties.setProperty("REALISTICCATCH",String.valueOf(realisticCatch));
        properties.setProperty("SHOWONLYWHITELIST",String.valueOf(showOnlyWhiteListed));
        properties.setProperty("SENDMESSAGES",String.valueOf(sendMessages));
        properties.setProperty("CHARACTER",CHARACTER);
        properties.setProperty("PREFIX",PREFIX);
        if(levelList.size() > 0)
        {
            StringBuilder levelString = new StringBuilder();
            for (String pokeName : levelList)
            {
                if(pokeName.length() > 0)
                {
                    levelString.append(pokeName).append(" ");
                }

            }
            levelString = new StringBuilder(levelString.substring(0, levelString.length() - 1));
            properties.setProperty("LEVELLIST", levelString.toString());
        }
        try
        {
            properties.store(new FileOutputStream("pokebot.properties"),null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private static void LoadLists()
    {
        Main.Output("Setting lists...");
        try
        {
            File filter = new File("lists");
            File[] FileList = filter.listFiles((file, name) -> name.endsWith(".list"));
            assert FileList != null;
            for (File file : FileList)
            {
                String readout = file.getName();
                int total = 0;
                Scanner sc = new Scanner(file);
                while (sc.hasNextLine())
                {
                    total++;
                    whitelist.add(sc.nextLine().toLowerCase());
                }
                Main.Output("List found >" + (readout.substring(0, readout.length() - 5)) + "< pokemon found: " + total);
            }
        }
        catch(Exception e)
        {
            Main.Output("No lists found!");
        }
    }
}