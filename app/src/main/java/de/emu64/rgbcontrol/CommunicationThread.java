package de.emu64.rgbcontrol;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import android.os.Handler;

import java.lang.InterruptedException;

/**
 * Created by thorsten on 02.04.16.
 */
public class CommunicationThread extends Thread {

    private Handler handler;
    private boolean isRunning;
    private boolean newCommand = false;
    private String command;
    private boolean isConnected = false;
    private int state = 0;
    private Socket client;
    private PrintWriter out;

    private int connect_attempts;
    private String ip;
    private int port;

    public CommunicationThread(Handler handler)
    {
        this.handler = handler;
    }

    public void StopRunning()
    {
        isRunning = false;
    }

    public void ConnectESP(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
        this.connect_attempts = 3;
        state = 1;
    }

    public void SetCommand(String command)
    {
        this.command = command;
        this.newCommand = true;
    }

    @Override
    public void run() {
        isRunning = true;
        state = 0;

        while (isRunning)
        {
            switch (state) {
                // Leerlauf
                case 0:
                {
                    break;
                }

                // Es wird hier Versucht sich mit dem ESP Modul zu verbinden
                case 1:
                {
                    try {
                        client = new Socket(ip, port);
                        out = new PrintWriter(client.getOutputStream(),true);

                        isConnected = true;
                        state = 2;

                    } catch (UnknownHostException e) {
                        connect_attempts--;
                        if(connect_attempts <= 0)
                        {
                            // Alle Versuche verbraucht
                            state = 0;
                            // Message senden
                            handler.sendEmptyMessage(0);
                        }

                    } catch (IOException e) {
                        connect_attempts--;
                        if(connect_attempts <= 0)
                        {
                            // Alle Versuche verbraucht
                            state = 0;
                            // Message senden
                            handler.sendEmptyMessage(0);
                        }
                    }
                    break;
                }

                case 2: {
                    if ((newCommand == true) && (isConnected == true)) {
                        newCommand = false;
                        out.write(command + "\r\n");
                        out.flush();
                    }
                    break;
                }
            }
        }
    }
}

