package com.MikeTheShadow.PokeBotMain;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.List;
import javax.swing.JTabbedPane;
import javax.swing.JDesktopPane;
import java.awt.Color;
import java.awt.Button;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
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

    static JTextField channelBox,tokenBox;

    private JFrame frmPokecordmain;
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
                    StartButton.setEnabled(false);
                    Main.SaveProperties();
                    output.add("Bot started!");
                    Main.Start();

                }
                catch(Exception e)
                {
                    //TODO find the error that gets thrown here and call the restart func
                    e.printStackTrace();
                    System.out.println("Still trying");
                    //System.out.println("restarting...");
                    //Restart restartThread = new Restart("restartThread");
                    //restartThread.run();
                }

            }
        });

        JLabel lblPokebot = new JLabel("PokeBot 1.0");
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

        sendMessages = new JCheckBox("Search for pokemon");
        sendMessages.setActionCommand("newcheck");
        sendMessages.setBounds(6, 132, 169, 23);
        SettingsTab.add(sendMessages);

        catchOnlyWhitelisted = new JCheckBox("Catch Only Whitelisted");
        catchOnlyWhitelisted.setBounds(6, 158, 169, 23);
        SettingsTab.add(catchOnlyWhitelisted);

        CatchOutsideChannel = new JCheckBox("Catch Outside channel");
        CatchOutsideChannel.setBounds(6, 184, 169, 23);
        SettingsTab.add(CatchOutsideChannel);

        CatchEverything = new JCheckBox("Catch everything everywhere");
        CatchEverything.setBounds(6, 210, 169, 23);
        SettingsTab.add(CatchEverything);

        realisticCatch = new JCheckBox("Realistic catch");
        realisticCatch.setActionCommand("Realistic catch");
        realisticCatch.setBounds(6, 236, 169, 23);
        SettingsTab.add(realisticCatch);

        ShowOnlyWhitelisted = new JCheckBox("Show Only Whitelisted");
        ShowOnlyWhitelisted.setBounds(6, 262, 169, 23);
        SettingsTab.add(ShowOnlyWhitelisted);

        tokenBox = new JTextField();
        tokenBox.setBounds(89, 83, 86, 20);
        SettingsTab.add(tokenBox);
        tokenBox.setColumns(10);

        JLabel lblNotRecommendedEspecially = new JLabel("Not recommended especially if you're in public discords with this account");
        lblNotRecommendedEspecially.setBounds(181, 214, 670, 14);
        SettingsTab.add(lblNotRecommendedEspecially);

        channelBox = new JTextField();
        channelBox.setBounds(89, 105, 86, 20);
        SettingsTab.add(channelBox);
        channelBox.setColumns(10);

        JLabel lblChannelId = new JLabel("CHANNEL ID");
        lblChannelId.setBounds(6, 108, 73, 14);
        SettingsTab.add(lblChannelId);

        JLabel lblToken = new JLabel("TOKEN");
        lblToken.setBounds(6, 86, 73, 14);
        SettingsTab.add(lblToken);

        //Load setup
        try
        {
            if(!Main.LoadSetup())
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
    public static void load()
    {
        catchOnlyWhitelisted.setSelected(Main.catchOnlyWhiteListed);
        sendMessages.setSelected(Main.sendMessages);
        realisticCatch.setSelected(Main.realisticCatch);
        ShowOnlyWhitelisted.setSelected(Main.showOnlyWhiteListed);
        CatchEverything.setSelected(Main.catchEverythingEverywhere);
        CatchOutsideChannel.setSelected(Main.catchOutsideChannel);
    }

}
