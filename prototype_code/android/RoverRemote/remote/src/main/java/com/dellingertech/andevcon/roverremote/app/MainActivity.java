package com.dellingertech.andevcon.roverremote.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends Activity {

    public static final String TAG = "RoverRemote";
    WebView webView;
    Button reload;
    Button stop;
    Button forward;
    Button backward;
    Button left;
    Button right;

    boolean active = false;
    RoverManager roverManager;
    Handler handler = new Handler();
    Runnable updater = new Runnable() {
        @Override
        public void run() {
            AsyncTask updaterTask = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    roverManager.sendUpdate();
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    if(active) {
                        handler.postDelayed(updater, 20);
                    }
                }
            };
            updaterTask.execute();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        roverManager = new RoverManager();
        webView = (WebView) findViewById(R.id.webView);
        reload = (Button) findViewById(R.id.reloadcam);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.reload();
            }
        });

        stop = (Button) findViewById(R.id.stop);
        forward = (Button) findViewById(R.id.forward);
        backward = (Button) findViewById(R.id.backward);
        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right);

        ControlsListener cl = new ControlsListener(roverManager);
        stop.setOnClickListener(cl);
        forward.setOnClickListener(cl);
        backward.setOnClickListener(cl);
        left.setOnClickListener(cl);
        right.setOnClickListener(cl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.loadUrl("http://192.168.3.1:8081/stream_simple.html");
        active = true;
        handler.post(updater);
    }

    @Override
    protected void onPause() {
        super.onPause();
        active = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
