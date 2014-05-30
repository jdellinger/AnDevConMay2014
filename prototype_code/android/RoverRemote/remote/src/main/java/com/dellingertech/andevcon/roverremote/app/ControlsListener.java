package com.dellingertech.andevcon.roverremote.app;

import android.view.View;

public class ControlsListener implements View.OnClickListener{

    RoverManager roverManager;

    public ControlsListener(RoverManager roverManager) {
        this.roverManager = roverManager;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.forward:
                roverManager.forward();
                break;
            case R.id.backward:
                roverManager.backward();
                break;
            case R.id.left:
                roverManager.left();
                break;
            case R.id.right:
                roverManager.right();
                break;
            case R.id.stop:
                roverManager.stop();
                break;
        }
    }
}
