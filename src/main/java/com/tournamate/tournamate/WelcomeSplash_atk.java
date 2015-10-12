package com.tournamate.tournamate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;


/**
 * Created by Christian on 17-03-2015.
 */
public class WelcomeSplash_atk extends Activity implements Runnable {

    Handler handler = new Handler();
    static WelcomeSplash_atk aktivitetDerVisesNu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.icon);
        setContentView(iv);

        if (savedInstanceState == null) {
            handler.postDelayed(this, 3000); // Kør om 3 sekunder
        }
        aktivitetDerVisesNu = this;
    }

    public void run() {
        startActivity(new Intent(this, MainMenu_akt.class));
        aktivitetDerVisesNu.finish();  // Luk velkomst
        aktivitetDerVisesNu = null;
    }

    @Override
    public void finish() {
        super.finish();
        handler.removeCallbacks(this);
    }
}