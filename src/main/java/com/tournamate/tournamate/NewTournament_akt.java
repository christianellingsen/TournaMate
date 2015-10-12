package com.christan.tournamate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Christian on 06-04-2015.
 */
public class NewTournament_akt extends Activity implements View.OnClickListener{

    ImageView home;
    TextView header;
    DBAdapter db_adapter = new DBAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_tournament);

        header = (TextView) findViewById(R.id.textViewNewTournamentHeader);
        header.setText(getString(R.string.newTournament_header));
        home = (ImageView) findViewById(R.id.imageViewHome);
        home.setOnClickListener(this);

        if (!MyApplication.resumingTournament) {
            AlertDialog.Builder setName = new AlertDialog.Builder(this);

            setName.setTitle(getString(R.string.newTournament_dialogHeader));
            setName.setMessage(getString(R.string.newTournament_dialogText));

            final EditText input = new EditText(this);
            setName.setView(input);
            setName.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = input.getText().toString();
                    MyApplication.tournamentName = value;
                }
            });
            setName.show();

            if (savedInstanceState == null) {
                AddPlayer_frag fragment = new AddPlayer_frag();
                getFragmentManager().beginTransaction()
                        .add(R.id.fragmentContent, fragment)
                        .commit();
            }
        }
        else {
            ActiveMatchScore_frag fragment = new ActiveMatchScore_frag();
            getFragmentManager().beginTransaction()
                    .add(R.id.fragmentContent, fragment)
                    .commit();
        }
    }

    @Override
    public void onClick(View v) {
        if (v ==home) {
            updateOnDevice();
            Intent i = new Intent(this,MainMenu_akt.class);
            startActivity(i);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateOnDevice();
    }

    public void updateOnDevice(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date  = dateFormat.format(new Date());
        openDB();
        db_adapter.updateRowTournament(MyApplication.tournamentID_sql,MyApplication.tournamentName,MyApplication.type,MyApplication.activeMatch,MyApplication.matchesPlayed, String.valueOf(MyApplication.isDone).toString() ,String.valueOf(MyApplication.isOnline),MyApplication.tournamentID_parse,date);
        Log.d("DB", "Tournament updated. Name: " + MyApplication.tournamentName +" type: "+ MyApplication.type +" activematch: "+ MyApplication.activeMatch +" played: "+ MyApplication.matchesPlayed +" done: "+ String.valueOf(MyApplication.isDone).toString() +" online: "+ String.valueOf(MyApplication.isOnline) +" date: "+ date);
        for(Match m : MyApplication.matchList){
            Team t1 = m.getT1();
            Team t2 = m.getT2();
            db_adapter.updateRowTeam(t1.getTeamID_sql(), t1.getTeamName(), t1.getMatchesWon(), t1.getMatchesLost(), t1.getMatchesDraw(), t1.getOverAllScore(), t1.getMatechesPlayed());
            db_adapter.updateRowTeam(t2.getTeamID_sql(), t2.getTeamName(), t2.getMatchesWon(), t2.getMatchesLost(), t2.getMatchesDraw(), t2.getOverAllScore(), t2.getMatechesPlayed());
            db_adapter.updateRowMatch(m.getMatchID_sql(),String.valueOf(m.isPlayed()),(int)MyApplication.tournamentID_sql,(int)t1.getTeamID_sql(),(int)t2.getTeamID_sql(), m.getScoreT1(),m.getScoreT2(),m.getMatchNumber());
        }
        closeDB();
    }

    public void openDB(){
        db_adapter = new DBAdapter(this);
        db_adapter.open();
    }

    public void closeDB(){
        db_adapter.close();
    }


}
