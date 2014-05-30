package com.dellingertech.andevcon.roverremote.app;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;

public class RoverManager {

    private long lastUpdate;
    private String lastCommand;

    public enum State {
        Forward("forward"),
        Backward("backward"),
        Left("left"),
        Right("right"),
        Stop("stop");

        private String action;
        private State(String action){
            this.action = action;
        }
        public String getAction(){
            return action;
        }
    }

    State currentState = State.Stop;

    public State getState(){
        return currentState;
    }

    public void forward(){
        currentState = State.Forward;
    }

    public void backward(){
        currentState = State.Backward;
    }

    public void left(){
        currentState = State.Left;
    }

    public void right(){
        currentState = State.Right;
    }

    public void stop(){
        currentState = State.Stop;
    }

    public void sendUpdate(){
        //call service on RPi
        try {
            String command = buildUrl();
            long now = System.currentTimeMillis();
            if(command != null && (!command.equals(lastCommand) || now - lastUpdate > 4000)){
                Log.i(MainActivity.TAG, "Sending update: "+command);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(new HttpGet(buildUrl()));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    String responseString = out.toString();
                    //..more logic
                    lastUpdate = System.currentTimeMillis();
                    lastCommand = command;
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                }
            }
        }catch(Exception e){
            Log.e(MainActivity.TAG, "exception sending update", e);
        }
    }

    private String buildUrl() {
        return String.format("http://192.168.3.1:8080/%s", currentState.getAction());
    }
}
