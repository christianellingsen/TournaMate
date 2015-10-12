package com.christan.tournamate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by Christian on 17-03-2015.
 */
public class MainMenu_akt extends Activity implements View.OnClickListener {

    Button newTournament_b, findTournament_b, resume_b, favorite_b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_menu);


        newTournament_b = (Button) findViewById(R.id.buttonStartNew);
        findTournament_b = (Button) findViewById(R.id.buttonFindTournament);
        resume_b = (Button) findViewById(R.id.buttonResumeTournament);
        favorite_b = (Button) findViewById(R.id.buttonStartFavorite);

        newTournament_b.setOnClickListener(this);
        findTournament_b.setOnClickListener(this);
        resume_b.setOnClickListener(this);
        favorite_b.setOnClickListener(this);

    }

    public void onClick(View v) {

        if (v== newTournament_b){
            MyApplication.resumingTournament =false;
            Intent i = new Intent(this, NewTournament_akt.class);
            startActivity(i);
        }
        else if (v==findTournament_b){
            Intent i = new Intent(this,FindTournament_akt.class);
            startActivity(i);
        }
        else if(v==resume_b){
            Intent i = new Intent(this,ResumeTournament_akt.class);
            startActivity(i);
        }
        else if (v==favorite_b){
            Toast.makeText(this,getString(R.string.mainMenu_commingSoonToast),Toast.LENGTH_SHORT).show();
        }
    }
}
