package net.home.apps.plexremote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class remote extends Activity {

    // Volume
    private static int vol = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);

        // Get buttons
        ImageView bttn_left = (ImageView)findViewById(R.id.bttn_left);
        ImageView bttn_right = (ImageView)findViewById(R.id.bttn_right);
        ImageView bttn_up = (ImageView)findViewById(R.id.bttn_top);
        ImageView bttn_down = (ImageView)findViewById(R.id.bttn_down);
        ImageView bttn_ok = (ImageView)findViewById(R.id.bttn_OK);
        ImageView bttn_back = (ImageView)findViewById(R.id.bttn_back);

        // Player buttons
        ImageView bttn_rewind = (ImageView)findViewById(R.id.bttn_rewind);
        ImageView bttn_play = (ImageView)findViewById(R.id.bttn_play);
        ImageView bttn_pause = (ImageView)findViewById(R.id.bttn_pause);
        ImageView bttn_fastforward = (ImageView)findViewById(R.id.bttn_fastforward);


        //Click Left Button
        bttn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                api_prep("moveLeft", "navigation");
            }
        });

        bttn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Right button pushed
                api_prep("moveRight", "navigation");
            }
        });

        bttn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Down button pushed
                api_prep("moveDown", "navigation");
            }
        });

        bttn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Up button pushed
                api_prep("moveUp", "navigation");
            }
        });

        bttn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // OK button pushed
                api_prep("select", "navigation");
            }
        });

        bttn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                api_prep("back", "navigation");
            }
        });

        // Player Controls
        bttn_rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                api_prep("stepback", "playback");
            }
        });

        bttn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                api_prep("play", "playback");
            }
        });

        bttn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                api_prep("pause", "playback");
            }
        });

        bttn_fastforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                api_prep("stepforward", "playback");
            }
        });
    }


    /* DOSN'T WORK
    // Save changes on rotation
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("vol", String.valueOf(vol));
    }
    */

    // Capture Hardware Volume Controller
    // We'll use the volume rocker to increase and decrease volume
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int action = event.getAction();
        Log.i("Plex Remote", "Volume " + keyCode + " " + action);
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    if(vol < 100)
                    {
                        vol = vol + 5;
                    }

                    Log.i("Plex Remote","Volume = " + String.valueOf(vol));
                    api_prep("setParameters?type=video&volume=" + String.valueOf(vol), "playback");
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    if(vol > 0 )
                    {
                        vol = vol - 5;
                    }
                    Log.i("Plex Remote","Volume = " + String.valueOf(vol));
                    api_prep("setParameters?type=video&volume=" + String.valueOf(vol), "playback");
                }
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    private void api_prep(String api_method, String api_controller)
    {
        Context ctx = getApplicationContext();
        // Get the prefs --later
        //String server = "192.168.1.10";
        //String port = "3005";
        //String client = "192.168.10.1";
        String server_ip = Settings.Read(ctx,"Key_Server_IP" );
        String server_port = Settings.Read(ctx, "Key_Server_Port");

        String client_ip = Settings.Read(ctx,"Key_Client_IP");

        //String api_url = "http://" + server +":" + port + "/system/players/" + client + "/" + api_controller + "/" + api_method;
        String api_url = "http://" + server_ip + ":" + server_port + "/player/" + api_controller + "/" + api_method;
        Log.i("Plex Remote", api_url);

        // Call the API
        new api(api_url);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.remote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
