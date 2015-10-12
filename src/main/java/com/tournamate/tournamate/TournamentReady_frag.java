package com.tournamate.tournamate;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Christian on 05-04-2015.
 */
public class TournamentReady_frag extends Fragment implements View.OnClickListener {

    // Define graphical elements
    TextView selectedType_tv, header;
    Button start_b;
    ToggleButton onOffLine_b;
    ListView lv;
    View rod;
    Boolean tCreated = false;
    DBAdapter db_adapter = new DBAdapter(getActivity());

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

        rod = i.inflate(R.layout.tournament_ready, container, false);

        header = (TextView) getActivity().findViewById(R.id.textViewNewTournamentHeader);
        lv = (ListView) rod.findViewById(R.id.listViewTeams);
        selectedType_tv = (TextView) rod.findViewById(R.id.textViewSelectedType);
        start_b = (Button) rod.findViewById(R.id.buttonStart);
        onOffLine_b = (ToggleButton) rod.findViewById(R.id.toggleButtonONOFFline);

        header.setText(getString(R.string.tournamentResume_header));
        selectedType_tv.setText(MyApplication.type);

        MyApplication.activeMatch = 1;
        MyApplication.matchesPlayed = 0;
        MyApplication.isDone = false;
        MyApplication.tournamentID_parse = "";

        ArrayList<String> teamNames = new ArrayList<>();

        for (Team t : MyApplication.teams) {
            String teamName = t.getTeamName();
            teamNames.add(teamName);
        }
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.team_tv, R.id.textViewTeamName, teamNames) {
            @Override
            public View getView(int position, View cachedView, ViewGroup parent) {
                View view = super.getView(position, cachedView, parent);

                TextView teamNumber = (TextView) view.findViewById(R.id.textViewTeamNumber);
                teamNumber.setText("" + (position + 1));

                return view;
            }
        };
        lv.setAdapter(adapter);

        start_b.setOnClickListener(this);
        onOffLine_b.setOnClickListener(this);

        return rod;
    }

    public void onClick(View v) {
        if (v == start_b) {

            if (onOffLine_b.isChecked()) {

                MyApplication.isOnline = true;

                final ProgressDialog progress;
                progress = ProgressDialog.show(getActivity(), getString(R.string.tournamentResume_dialogHeader), getString(R.string.tournamentResume_dialogText), true);
                progress.setCancelable(false);
                progress.show();

                new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object... arg0) {
                        Log.d("Parse", "Async 1 started");
                        final Tournament tournament = new Tournament();
                        tournament.putName(MyApplication.tournamentName);
                        tournament.putWinner("No winner yet");
                        tournament.putIsDone(MyApplication.isDone);
                        tournament.saveInBackground(new SaveCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    // Success!
                                    MyApplication.tournamentID_parse = tournament.getObjectId();
                                    tCreated = true;
                                    Log.d("Parse", "Tournament created with id: " + tournament.getObjectId());
                                } else {
                                    // Failure!
                                }
                            }
                        });


                        return Log.d("Parse", "Async 1 done");
                    }


                }.execute();

                new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object... arg0) {
                        Log.d("Parse", "Tournament is done, async 2 started. Matchlist size: "+ MyApplication.matchList.size());
                        while (!tCreated);
                        int match_counter = 1;
                        for (Match m : MyApplication.matchList) {
                            Log.d("Parse", "Inside for loop!");
                            Log.d("Parse", "Saving match to parse. Teams: " + m.getT1().getTeamName() + " vs " + m.getT2().getTeamName());
                            final Match Match = new Match();
                            Match.putTeam1Name(m.getT1().getTeamName());
                            Match.putTeam2Name(m.getT2().getTeamName());
                            Match.putTeam1Score(m.getScoreT1());
                            Match.putTeam2Score(m.getScoreT2());
                            Match.putIsPlayed(m.isPlayed());
                            Match.putTournamentID(MyApplication.tournamentID_parse);
                            Match.setTournamentID(MyApplication.tournamentID_parse);
                            Match.putTournamentName(MyApplication.tournamentName);
                            Match.putMatchNumber(match_counter);
                            match_counter++;
                            Match.saveInBackground(new SaveCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        // Success!
                                        Log.d("Parse", "Match created with id: " + Match.getObjectId());

                                    } else {
                                        // Failure!
                                        Log.d("Parse", "Failed to save match to parse");
                                    }
                                }
                            });
                            while (!Match.saveInBackground().isCompleted()) ;

                        }

                        return Log.d("Parse", "Done saving matches to parse");
                    }

                    @Override
                    protected void onPostExecute(Object result){
                        Log.d("Parse", "Match post execute done");
                        progress.dismiss();
                    }
                }.execute();
            }
            else {
                MyApplication.isOnline = false;
            }

            //Start tournament
            if (MyApplication.type.equals("Round Robin")) {
                RoundRobin_logic rr;
                MyApplication.matchList.clear();
                MyApplication.matchesPlayed = 0;

                rr = new RoundRobin_logic();
                rr.createMatches();

                ActiveMatchScore_frag fragment = new ActiveMatchScore_frag();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContent, fragment)
                        .commit();
            }
            // Save to local data store
            saveOnDevice();

        }
    }

    public void saveOnDevice(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date  = dateFormat.format(new Date());

        long db_t_id, db_t1_id, db_t2_id, db_m_id;
        openDB();
        db_t_id = db_adapter.insertRowTournament(MyApplication.tournamentName,MyApplication.type,MyApplication.activeMatch,MyApplication.matchesPlayed, String.valueOf(MyApplication.isDone).toString() ,String.valueOf(MyApplication.isOnline),MyApplication.tournamentID_parse,date);
        MyApplication.tournamentID_sql = db_t_id;
        Log.d("DB", "Tournament saved. Name: " + MyApplication.tournamentName +" type: "+ MyApplication.type +" activematch: "+ MyApplication.activeMatch +" played: "+ MyApplication.matchesPlayed +" done: "+ String.valueOf(MyApplication.isDone).toString() +" online: "+ String.valueOf(MyApplication.isOnline) +" date: "+ date);
        for(Match m : MyApplication.matchList){
            Team t1 = m.getT1();
            Team t2 = m.getT2();
            db_t1_id = db_adapter.insertRowTeams(t1.getTeamName(),t1.getMatchesWon(),t1.getMatchesLost(),t1.getMatchesDraw(),t1.getOverAllScore(),t1.getMatechesPlayed());
            db_t2_id = db_adapter.insertRowTeams(t2.getTeamName(),t2.getMatchesWon(),t2.getMatchesLost(),t2.getMatchesDraw(),t2.getOverAllScore(),t2.getMatechesPlayed());
            db_m_id = db_adapter.insertRowMatches(String.valueOf(m.isPlayed()),(int)db_t_id,(int)db_t1_id, m.getScoreT1(),m.getScoreT2(),(int)db_t2_id, m.getMatchNumber());
            t1.setTeamID_sql(db_t1_id);
            t2.setTeamID_sql(db_t2_id);
            m.setMatchID_sql(db_m_id);
            m.setT1ID_sql(db_t1_id);
            m.setT2ID_sql(db_t2_id);
        }
        closeDB();
    }

    public void openDB(){
        db_adapter = new DBAdapter(getActivity());
        db_adapter.open();
    }

    public void closeDB(){
        db_adapter.close();
    }

}
