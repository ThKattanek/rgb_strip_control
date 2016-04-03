package de.emu64.rgbcontrol;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private static Socket client1;
    private static PrintWriter out;
    private static BufferedReader in;

    private static SeekBar seek_bar_red;
    private static TextView out_text_red;
    private static SeekBar seek_bar_green;
    private static TextView out_text_green;
    private static SeekBar seek_bar_blue;
    private static TextView out_text_blue;

    private static ImageButton image_button;

    private static CheckBox check_red;
    private static CheckBox check_green;
    private static CheckBox check_blue;

    private static RadioButton single_color_mode;
    private static RadioButton gardient_mode;

    private  CommunicationThread com_thread;

    int iRed ,iGreen= 0, iBlue = 0;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(MainActivity.this, "Fehler beim Verbinden mit ESP", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RGBSeekBar();

        single_color_mode = (RadioButton) findViewById(R.id.single_color_mode);
        gardient_mode = (RadioButton) findViewById(R.id.gardient_mode);

        single_color_mode.setOnClickListener(new RadioButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                com_thread.SetCommand("colormode=singlecolor");
            }
        });

        gardient_mode.setOnClickListener(new RadioButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                com_thread.SetCommand("colormode=gardient");
            }
        });

        com_thread = new CommunicationThread(handler);
        com_thread.start();

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        com_thread.ConnectESP("10.10.0.250", 4040);
    }

    public void ClickColor(View view)
    {
        Toast.makeText(MainActivity.this, "Was drückst du mich ?", Toast.LENGTH_SHORT).show();
    }
    
    public void RGBSeekBar() {

        seek_bar_red = (SeekBar) findViewById(R.id.seekBar_red);
        seek_bar_green = (SeekBar) findViewById(R.id.seekBar_green);
        seek_bar_blue = (SeekBar) findViewById(R.id.seekBar_blue);

        out_text_red = (TextView) findViewById(R.id.out_red);
        out_text_green = (TextView) findViewById(R.id.out_green);
        out_text_blue = (TextView) findViewById(R.id.out_blue);

        out_text_red.setText("Rot: " + iRed);
        out_text_green.setText("Grün: " + iGreen);
        out_text_blue.setText("Blau: " + iBlue);

        check_red = (CheckBox) findViewById(R.id.check_red);
        check_green = (CheckBox) findViewById(R.id.check_green);
        check_blue = (CheckBox) findViewById(R.id.check_blue);

        image_button = (ImageButton) findViewById(R.id.imageButton);

        image_button.setBackgroundColor(0xff000000 | (iRed << 16) | (iGreen << 8) | iBlue);


        seek_bar_red.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        iRed = seekBar.getProgress();
                        out_text_red.setText("Rot: " + iRed);
                        image_button.setBackgroundColor(0xff000000 | (iRed << 16) | (iGreen << 8) | iBlue);
                        com_thread.SetCommand("red=" + iRed);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        out_text_red.setText("Rot: " + iRed);
                        image_button.setBackgroundColor(0xff000000 | (iRed << 16) | (iGreen << 8) | iBlue);
                        com_thread.SetCommand("red=" + iRed);
                    }
                }
        );

        seek_bar_green.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        iGreen = seekBar.getProgress();
                        out_text_green.setText("Grün: " + iGreen);
                        image_button.setBackgroundColor(0xff000000 | (iRed << 16) | (iGreen << 8) | iBlue);
                        com_thread.SetCommand("green=" + iGreen);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        out_text_green.setText("Grün: " + iGreen);
                        image_button.setBackgroundColor(0xff000000 | (iRed << 16) | (iGreen << 8) | iBlue);
                        com_thread.SetCommand("green=" + iGreen);
                    }
                }
        );

        seek_bar_blue.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        iBlue = seekBar.getProgress();
                        out_text_blue.setText("Blau: " + iBlue);
                        image_button.setBackgroundColor(0xff000000 | (iRed << 16) | (iGreen << 8) | iBlue);
                        com_thread.SetCommand("blue=" + iBlue);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        out_text_blue.setText("Blau: " + iBlue);
                        image_button.setBackgroundColor(0xff000000 | (iRed << 16) | (iGreen << 8) | iBlue);
                        com_thread.SetCommand("blue=" + iBlue);
                    }
                }
        );

        check_red.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    seek_bar_red.setProgress(255);
                    com_thread.SetCommand("red=on");
                } else {
                    seek_bar_red.setProgress(0);
                    com_thread.SetCommand("red=off");
                }
            }
        });

        check_green.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    seek_bar_green.setProgress(255);
                    com_thread.SetCommand("green=on");
                } else {
                    seek_bar_green.setProgress(0);
                    com_thread.SetCommand("green=off");
                }
            }
        });

        check_blue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    seek_bar_blue.setProgress(255);
                    com_thread.SetCommand("blue=on");
                } else {
                    seek_bar_blue.setProgress(0);
                    com_thread.SetCommand("blue=off");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            finish();
            System.exit(0x0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://de.emu64.rgbcontrol/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://de.emu64.rgbcontrol/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
