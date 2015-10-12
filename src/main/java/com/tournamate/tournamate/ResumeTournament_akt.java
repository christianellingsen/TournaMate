package com.christan.tournamate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Christian on 19-05-2015.
 */
public class ResumeTournament_akt extends Activity implements  View.OnClickListener{

    Button delete_b,resume_b, toggle_b;
    TextView title_tv;
    ListView lv;
    int showIncomplete_flag = 1;
    final ArrayList<Tournament> completeTournaments = new ArrayList();
    final ArrayList<String> namesComplete = new ArrayList();
    final ArrayList<Tournament> notCompleteTournaments = new ArrayList();
    final ArrayList<String> namesNotComplete = new ArrayList();
    private DBAdapter dbAdapter = new DBAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.resume_tournament);

        toggle_b = (Button) findViewById(R.id.buttonCompleteMatches);
        resume_b = (Button) findViewById(R.id.buttonResumeResumeSelected);
        delete_b = (Button) findViewById(R.id.buttonResumeDelete);
        title_tv = (TextView) findViewById(R.id.textViewShowingMatcbes);
        lv = (ListView) findViewById(R.id.listViewResumeTournament);

        toggle_b.setOnClickListener(this);
        delete_b.setOnClickListener(this);
        resume_b.setOnClickListener(this);
        //lv.setOnItemClickListener(this);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        Tournament t_dummy = new Tournament();
        t_dummy.setName("");
        t_dummy.setCreatedAt("");
        t_dummy.setIsDone(false);
        completeTournaments.add(t_dummy);

        updateList();

    }

    @Override
    public void onClick(View v) {
        if (v==delete_b){
                int cntChoice = lv.getCount();
                SparseBooleanArray sparseBooleanArray = lv.getCheckedItemPositions();

                for(int position = 0; position < cntChoice; position++){

                    if(sparseBooleanArray.get(position)) {

                        long selectedTournamentID = 0;

                        if (showIncomplete_flag == 0) {
                            selectedTournamentID = completeTournaments.get(position).getObjectID_sql();
                        } else if(showIncomplete_flag == 1) {
                            selectedTournamentID = notCompleteTournaments.get(position).getObjectID_sql();
                        }

                        dbAdapter.open();
                        dbAdapter.deleteRow(selectedTournamentID);
                        dbAdapter.close();
                    }
                }
                updateList();
        }
        else if (v==toggle_b) {
            switch (showIncomplete_flag) {
                case 0:
                    toggle_b.setText(getString(R.string.resume_tournament_buttonShowComplete));
                    title_tv.setText(getString(R.string.resume_tournament_titleNotComplete));
                    title_tv.setBackgroundColor(Color.parseColor("#ffbf2626"));
                    showIncomplete_flag = 1;
                    break;
                case 1:
                    toggle_b.setText(getString(R.string.resume_tournament_buttonNotComplete));
                    title_tv.setText(getString(R.string.resume_tournament_titleComplete));
                    title_tv.setBackgroundColor(Color.parseColor("#FF0FAE02"));
                    showIncomplete_flag = 0;
                    break;
            }
            updateList();
        }
        else if (v==resume_b) {
            int cntChoice = lv.getCount();
            int selCount = 0;
            SparseBooleanArray sparseBooleanArray = lv.getCheckedItemPositions();
            for(int i = 0; i < cntChoice;i++){
                if (sparseBooleanArray.get(i)){
                    selCount++;
                }
            }
            if (selCount>1){
                Toast.makeText(this,getString(R.string.resume_tournament_resumeOne),Toast.LENGTH_SHORT).show();
            }
            else {
                for (int position = 0; position < cntChoice; position++) {

                    if (sparseBooleanArray.get(position)) {

                        ArrayList<Match> collectedMatches = new ArrayList<>();
                        ArrayList<Team> collectedTeams = new ArrayList<>();
                        ArrayList<String> teamNameList = new ArrayList<>();
                        teamNameList.add("");
                        String teamNames = "";

                        long selectedTournamentID = 0;
                        String selectedTournamentName = "";

                        if (showIncomplete_flag == 0) {
                            selectedTournamentID = completeTournaments.get(position).getObjectID_sql();
                            selectedTournamentName = completeTournaments.get(position).getName();
                        } else if(showIncomplete_flag == 1) {
                            selectedTournamentID = notCompleteTournaments.get(position).getObjectID_sql();
                            selectedTournamentName = notCompleteTournaments.get(position).getName();
                        }


                        MyApplication.matchList.clear();
                        MyApplication.teams.clear();

                        MyApplication.tournamentID_sql = selectedTournamentID;
                        MyApplication.tournamentName = selectedTournamentName;

                        dbAdapter.open();
                        Cursor c = dbAdapter.getRow(MyApplication.DATABASE_TABLE_TOURNAMENTS, selectedTournamentID);
                        if (c.moveToFirst()) {
                            MyApplication.type = c.getString(MyApplication.COL_TOURNAMENTS_TYPE);
                            MyApplication.matchesPlayed = c.getInt(MyApplication.COL_TOURNAMENTS_PLAYED);
                            MyApplication.activeMatch = c.getInt(MyApplication.COL_TOURNAMENTS_ACTIVE);
                            MyApplication.tournamentID_parse = c.getString(MyApplication.COL_TOURNAMENTS_PARSE);
                            if (c.getString(MyApplication.COL_TOURNAMENTS_DONE).equals("true")) {
                                MyApplication.isDone = true;
                            } else {
                                MyApplication.isDone = false;
                            }
                            if (c.getString(MyApplication.COL_TOURNAMENTS_ONLINE).equals("true")) {
                                MyApplication.isOnline = true;
                            } else {
                                MyApplication.isOnline = false;
                            }
                        }
                        Log.d("DB", "From database. Tournament: " + MyApplication.tournamentName + MyApplication.tournamentID_sql + MyApplication.type + MyApplication.activeMatch + MyApplication.matchesPlayed);
                        c.close();
                        Cursor c_match = dbAdapter.getMatches(MyApplication.DATABASE_TABLE_MATCHES, selectedTournamentID);
                        if (c_match.moveToFirst()) {
                            do {
                                Match m = new Match();
                                m.setMatchID_sql(Long.parseLong(c_match.getString(MyApplication.COL_ROWID)));
                                m.setT1ID_sql(Long.parseLong(c_match.getString(MyApplication.COL_MATCHES_T1ID)));
                                m.setT2ID_sql(Long.parseLong(c_match.getString(MyApplication.COL_MATCHES_T2ID)));
                                m.setScoreT1(c_match.getInt(MyApplication.COL_MATCHES_T1SCORE));
                                m.setScoreT2(c_match.getInt(MyApplication.COL_MATCHES_T2SCORE));
                                m.setMatchNumber(c_match.getInt(MyApplication.COL_MATCHES_NUMBER));
                                Log.d("DB", "Adding match: " + m.getMatchNumber());
                                if (c_match.getString(MyApplication.COL_MATCHES_PLAYED).equals("true")) {
                                    m.setPlayed(true);
                                } else {
                                    m.setPlayed(false);
                                }

                                Cursor c_team1 = dbAdapter.getTeam(MyApplication.DATABASE_TABLE_TEAMS, m.getT1ID_sql());
                                Log.d("DB", "Cursor 1 open " + m.getT1ID_sql());
                                if (c_team1.moveToFirst()) {
                                    Team t1 = new Team();
                                    t1.setTeamName(c_team1.getString(MyApplication.COL_TEAMS_NAME));
                                    t1.setMatchesWon(c_team1.getInt(MyApplication.COL_TEAMS_WON));
                                    t1.setMatchesLost(c_team1.getInt(MyApplication.COL_TEAMS_LOST));
                                    t1.setMatchesDraw(c_team1.getInt(MyApplication.COL_TEAMS_DRAW));
                                    t1.setOverAllScore(c_team1.getInt(MyApplication.COL_TEAMS_SCORE));
                                    t1.setMatechesPlayed(c_team1.getInt(MyApplication.COL_TEAMS_PLAYED));
                                    t1.setTeamID_sql(m.getT1ID_sql());
                                    m.setT1(t1);
                                    String nameT1 = t1.getTeamName().toLowerCase().trim();
                                    Log.d("DB", " Team1: " + nameT1 + " list: " + teamNames);
                                    if (teamNames.contains(nameT1) == false) {
                                        Log.d("DB", " Team1: " + nameT1 + " is new. Adding to collectedTeams");
                                        collectedTeams.add(t1);
                                        teamNames += nameT1;
                                    } else {
                                        Log.d("DB", "Already in list. Not adding");
                                    }

                                }
                                c_team1.close();
                                Log.d("DB", "Cursor 1 closes");
                                Cursor c_team2 = dbAdapter.getTeam(MyApplication.DATABASE_TABLE_TEAMS, m.getT2ID_sql());
                                Log.d("DB", "Cursor 2 open " + m.getT2ID_sql());
                                if (c_team2.moveToFirst()) {
                                    Team t2 = new Team();
                                    t2.setTeamName(c_team2.getString(MyApplication.COL_TEAMS_NAME));
                                    t2.setMatchesWon(c_team2.getInt(MyApplication.COL_TEAMS_WON));
                                    t2.setMatchesLost(c_team2.getInt(MyApplication.COL_TEAMS_LOST));
                                    t2.setMatchesDraw(c_team2.getInt(MyApplication.COL_TEAMS_DRAW));
                                    t2.setOverAllScore(c_team2.getInt(MyApplication.COL_TEAMS_SCORE));
                                    t2.setMatechesPlayed(c_team2.getInt(MyApplication.COL_TEAMS_PLAYED));
                                    t2.setTeamID_sql(m.getT2ID_sql());
                                    m.setT2(t2);
                                    String nameT2 = t2.getTeamName().toLowerCase().trim();
                                    Log.d("DB", " Team2: " + nameT2 + " list: " + teamNames);
                                    if (teamNames.contains(nameT2) == false) {
                                        Log.d("DB", " Team2: " + nameT2 + " is new. Adding to collectedTeams");
                                        collectedTeams.add(t2);
                                        teamNames += nameT2;
                                    } else {
                                        Log.d("DB", "Already in list. Not adding");
                                    }
                                }
                                c_team2.close();
                                Log.d("DB", "Cursor 2 closes");
                                Log.d("DB", "From database. Match: " + m.getT1().getTeamName() + m.getT2().getTeamName() + m.getScoreT1() + m.getT2ScoreParse() + m.isPlayed());
                                collectedMatches.add(m);
                            } while (c_match.moveToNext());
                            c_match.close();
                        }
                        dbAdapter.close();

                        MyApplication.matchList = collectedMatches;
                        MyApplication.teams = collectedTeams;

                        MyApplication.resumingTournament = true;

                        Intent i = new Intent(this, NewTournament_akt.class);
                        startActivity(i);
                    }
                }
            }
        }
    }

    public void updateList(){

        notCompleteTournaments.clear();
        completeTournaments.clear();
        namesComplete.clear();
        namesNotComplete.clear();

        final ProgressDialog progress;
        progress = ProgressDialog.show(this, "Looking for tournaments", "", true);
        progress.setCancelable(false);
        progress.show();

        dbAdapter.open();
        Cursor c = dbAdapter.getAllRows(MyApplication.DATABASE_TABLE_TOURNAMENTS);
        if (c.moveToFirst()){
            do {
                String temp_name = c.getString(MyApplication.COL_TOURNAMENTS_NAME);
                String date = c.getString(MyApplication.COL_TOURNAMENTS_DATE);
                String isDone = c.getString(MyApplication.COL_TOURNAMENTS_DONE);
                long id = Long.parseLong(c.getString(MyApplication.COL_ROWID));
                Tournament t = new Tournament();
                t.setName(temp_name);
                t.setCreatedAt(date);
                t.setObjectID_sql(id);
                if(isDone.equals("true")){
                    t.setIsDone(true);
                    completeTournaments.add(t);
                    namesComplete.add(temp_name);
                }
                else{
                    t.setIsDone(false);
                    notCompleteTournaments.add(t);
                    namesNotComplete.add(temp_name);
                }

            } while (c.moveToNext());
            c.close();
        }
        dbAdapter.close();

        if (showIncomplete_flag==0) {
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, namesComplete);
            lv.setAdapter(adapter);
        }
        else if(showIncomplete_flag==1){
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, namesNotComplete);
            lv.setAdapter(adapter);
        }
        progress.dismiss();
    }
}
