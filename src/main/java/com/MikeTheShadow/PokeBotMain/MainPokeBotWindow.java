package com.MikeTheShadow.PokeBotMain;

import java.awt.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class MainPokeBotWindow
{

    //define the checkboxes
    static JCheckBox sendMessages;
    static JCheckBox catchOnlyWhitelisted;
    static JCheckBox CatchOutsideChannel;
    static JCheckBox CatchEverything;
    static JCheckBox realisticCatch;
    static JCheckBox ShowOnlyWhitelisted;

    static JTextField SpamBox;
    static JTextField channelBox,tokenBox,prefixBox;
    static JTextArea pokemonLevelList;
    public static JProgressBar pokemonLoadingBar;
    private JFrame frmPokecordmain;
    public static JLabel loadImagelabel;
    //for timer
    public static JSpinner timeout;
    static List output;
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() ->
        {
            try {
                MainPokeBotWindow window = new MainPokeBotWindow();
                window.frmPokecordmain.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    /**
     * Create the application.
     */
    private MainPokeBotWindow() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize()
    {
        frmPokecordmain = new JFrame();
        frmPokecordmain.setResizable(false);
        frmPokecordmain.setTitle("PokeCord");
        frmPokecordmain.setBounds(100, 100, 900, 700);
        frmPokecordmain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmPokecordmain.getContentPane().setLayout(null);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setName("MainTabPane");
        tabbedPane.setToolTipText("paneTab");
        tabbedPane.setBounds(0, 0, 894, 671);
        frmPokecordmain.getContentPane().add(tabbedPane);

        JDesktopPane PokeCordMainTab = new JDesktopPane();
        PokeCordMainTab.setBackground(Color.WHITE);
        PokeCordMainTab.setName("PokeBot");
        PokeCordMainTab.setToolTipText("PokeCord");
        tabbedPane.addTab("PokeBot", null, PokeCordMainTab, null);
        PokeCordMainTab.setLayout(null);

        output = new List();
        output.setName("output");
        output.setBounds(0, 0, 341, 643);
        PokeCordMainTab.add(output);

        Button StartButton = new Button("Start");
        StartButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0)
            {
                try
                {
                    Main.SaveProperties();
                    if(Main.LoadSetup())
                    {
                        output.add("Please fill out the Settings tab!");
                        return;
                    }
                    Main.stopped = false;
                    StartButton.setEnabled(false);
                    Main.SaveProperties();
                    output.add("Bot started!");
                    Main.Start();

                }
                catch(Exception e)
                {
                    //Should be no error here?
                    e.printStackTrace();
                    Main.Output("Program crashed!");
                }

            }
        });

        //Loading bar to load data
        pokemonLoadingBar = new JProgressBar();
        pokemonLoadingBar.setBounds(575, 571, 195, 27);
        PokeCordMainTab.add(pokemonLoadingBar);
        //loading bar label
        loadImagelabel = new JLabel("Loading image:");
        loadImagelabel.setBounds(575, 546, 200, 14);
        PokeCordMainTab.add(loadImagelabel);

        JLabel lblPokebot = new JLabel("PokeBot 1.2");
        lblPokebot.setFont(new Font("Tahoma", Font.PLAIN, 51));
        lblPokebot.setBounds(347, 11, 333, 71);
        PokeCordMainTab.add(lblPokebot);

        StartButton.setFont(new Font("Dialog", Font.PLAIN, 40));
        StartButton.setActionCommand("StartProgram");
        StartButton.setBounds(347, 438, 164, 71);
        PokeCordMainTab.add(StartButton);

        Button StopButton = new Button("Stop");
        StopButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                Main.api.removeEventListener(new Listener());
                Main.stopped = true;
                Main.api.shutdownNow();
                output.add("Shutting down...");
                StartButton.setEnabled(true);
            }
        });
        StopButton.setName("Stop");
        StopButton.setFont(new Font("Dialog", Font.PLAIN, 40));
        StopButton.setActionCommand("StopProgram");
        StopButton.setBounds(347, 527, 164, 71);
        PokeCordMainTab.add(StopButton);

        JDesktopPane SettingsTab = new JDesktopPane();
        SettingsTab.setBackground(Color.WHITE);
        SettingsTab.setName("Settings");
        SettingsTab.setToolTipText("Settings");
        tabbedPane.addTab("Settings", null, SettingsTab, null);

        sendMessages = new JCheckBox("Find Pokemon");
        sendMessages.setActionCommand("newcheck");
        sendMessages.setBounds(6, 132, 169, 23);
        SettingsTab.add(sendMessages);

        catchOnlyWhitelisted = new JCheckBox("Catch Only Whitelisted");
        catchOnlyWhitelisted.setBounds(6, 158, 169, 23);
        SettingsTab.add(catchOnlyWhitelisted);

        CatchOutsideChannel = new JCheckBox("Catch Outside channel");
        CatchOutsideChannel.setBounds(6, 184, 169, 23);
        SettingsTab.add(CatchOutsideChannel);

        CatchEverything = new JCheckBox("Catch anything anywhere");
        CatchEverything.setBounds(6, 210, 169, 23);
        SettingsTab.add(CatchEverything);

        realisticCatch = new JCheckBox("Realistic catch");
        realisticCatch.setActionCommand("Realistic catch");
        realisticCatch.setBounds(6, 236, 169, 23);
        SettingsTab.add(realisticCatch);

        ShowOnlyWhitelisted = new JCheckBox("Show Only Whitelisted");
        ShowOnlyWhitelisted.setBounds(6, 262, 169, 23);
        SettingsTab.add(ShowOnlyWhitelisted);


        JLabel prefixLbl = new JLabel("PREFIX");
        prefixLbl.setBounds(6, 45, 90, 14);
        SettingsTab.add(prefixLbl);

        prefixBox = new JTextField();
        prefixBox.setBounds(89, 40, 86, 20);
        SettingsTab.add(prefixBox);
        prefixBox.setColumns(10);

        tokenBox = new JTextField();
        tokenBox.setBounds(89, 80, 86, 20);
        SettingsTab.add(tokenBox);
        tokenBox.setColumns(10);

        SpamBox = new JTextField();
        SpamBox.setBounds(89, 60, 86, 20);
        SettingsTab.add(SpamBox);
        SpamBox.setColumns(10);

        JLabel lblSpamChar = new JLabel("FIND POKE");
        lblSpamChar.setBounds(6, 65, 90, 14);
        SettingsTab.add(lblSpamChar);

        JLabel Spamchardesclabel = new JLabel("This is what will be spammed over and over again");
        Spamchardesclabel.setBounds(181, 63, 347, 14);
        SettingsTab.add(Spamchardesclabel);

        JLabel lblNotRecommendedEspecially = new JLabel("Not recommended especially if you're in public discords with this account");
        lblNotRecommendedEspecially.setBounds(181, 214, 670, 14);
        SettingsTab.add(lblNotRecommendedEspecially);

        channelBox = new JTextField();
        channelBox.setBounds(89, 105, 86, 20);
        SettingsTab.add(channelBox);
        channelBox.setColumns(10);

        JLabel lblChannelId = new JLabel("CHANNEL ID");
        lblChannelId.setBounds(6, 108, 90, 14);
        SettingsTab.add(lblChannelId);

        JLabel lblToken = new JLabel("TOKEN");
        lblToken.setBounds(6, 86, 73, 14);
        SettingsTab.add(lblToken);

        //This is for the lists
        pokemonLevelList = new JTextArea();
        pokemonLevelList.setBounds(6, 315, 169, 195);
        pokemonLevelList.setBackground(SystemColor.inactiveCaption);
        SettingsTab.add(pokemonLevelList);

        JLabel lblPokemonToLevel = new JLabel("Put pokemon ID's to level up below (one ID per line)");
        lblPokemonToLevel.setBounds(6, 292, 300, 14);
        SettingsTab.add(lblPokemonToLevel);

        //timeout
        timeout = new JSpinner();
        timeout.setBounds(352, 412, 40, 20);
        PokeCordMainTab.add(timeout);

        JLabel lblShutdownAfteramount = new JLabel("Shutdown after (amount) hours (0 means its runs  forever)");
        lblShutdownAfteramount.setBounds(347, 396, 400, 14);
        PokeCordMainTab.add(lblShutdownAfteramount);
        //Load setup
        try
        {
            if(Main.LoadSetup())
            {
                output.add("Please fill out the Settings tab!");
            }
        }
        catch (Exception e)
        {
            try
            {
                Main.CreateProperties();
            } catch (IOException e1)
            {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }
    static void load()
    {
        catchOnlyWhitelisted.setSelected(Main.catchOnlyWhiteListed);
        sendMessages.setSelected(Main.sendMessages);
        realisticCatch.setSelected(Main.realisticCatch);
        ShowOnlyWhitelisted.setSelected(Main.showOnlyWhiteListed);
        CatchEverything.setSelected(Main.catchEverythingEverywhere);
        CatchOutsideChannel.setSelected(Main.catchOutsideChannel);
    }

}
