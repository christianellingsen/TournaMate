package com.tournamate.tournamate;

import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 19-04-2015.
 */
public class ActiveMatchScore_frag extends Fragment implements View.OnClickListener{

    private TextView matchNumber_tv, teamX_tv, teamY_tv, teamXScore_tv, teamYScore_tv, header;
    private Button teamXPlus_b, teamXMinus_b, teamYMinus_b, teamYPlus_b, rank_b, matchlist_b, next_b;
    private int activeMatchNumber;
    private Match m;
    ArrayList<Match> matches;
    View rod;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

            rod = i.inflate(R.layout.active_match_score, container, false);

            header = (TextView) getActivity().findViewById(R.id.textViewNewTournamentHeader);
            matchNumber_tv = (TextView) rod.findViewById(R.id.textViewMatchNumber);
            teamX_tv = (TextView) rod.findViewById(R.id.textViewTeamX);
            teamY_tv = (TextView) rod.findViewById(R.id.textViewTeamY);
            teamXScore_tv = (TextView) rod.findViewById(R.id.textViewTeamXScore);
            teamYScore_tv = (TextView) rod.findViewById(R.id.textViewTeamYScore);
            teamXPlus_b = (Button) rod.findViewById(R.id.buttonTeamXPlus);
            teamYPlus_b = (Button) rod.findViewById(R.id.buttonTeamYPlus);
            teamXMinus_b = (Button) rod.findViewById(R.id.buttonTeamXMinus);
            teamYMinus_b = (Button) rod.findViewById(R.id.buttonTeamYMinus);
            rank_b = (Button) rod.findViewById(R.id.buttonRank);
            matchlist_b = (Button) rod.findViewById(R.id.buttonViewAllMatches);
            next_b = (Button) rod.findViewById(R.id.buttonNextMatch);

            header.setText(MyApplication.tournamentName);

            teamXPlus_b.setOnClickListener(this);
            teamXMinus_b.setOnClickListener(this);
            teamYPlus_b.setOnClickListener(this);
            teamYMinus_b.setOnClickListener(this);
            rank_b.setOnClickListener(this);
            matchlist_b.setOnClickListener(this);
            next_b.setOnClickListener(this);

            matches = MyApplication.matchList;

            Log.d("Debug", "Active match: " + MyApplication.activeMatch);
            Log.d("Debug", "Matches played: " + MyApplication.matchesPlayed);

            if (MyApplication.matchesPlayed + 1 != MyApplication.activeMatch) {
                activeMatchNumber = MyApplication.activeMatch;
            } else {
                activeMatchNumber = (MyApplication.matchesPlayed + 1);
                if(MyApplication.activeMatch < MyApplication.matchList.size()){
                    MyApplication.activeMatch++;
                }
            }


            if (activeMatchNumber <= matches.size()) {
                m = matches.get(activeMatchNumber - 1);

                matchNumber_tv.setText(getString(R.string.activeTournament_Match) + m.getMatchNumber());
                teamX_tv.setText(m.getT1().getTeamName());
                teamY_tv.setText(m.getT2().getTeamName());
            }
            if (MyApplication.matchesPlayed == matches.size() - 2) {
                next_b.setText(getString(R.string.activeTournament_playNext));
            } else if (MyApplication.matchesPlayed == matches.size() - 1) {
                next_b.setText(getString(R.string.activeTournament_done));
            }

            if (MyApplication.isDone) {
                next_b.setText(getString(R.string.activeTournament_seeWinner));
            }

            teamXScore_tv.setText("" + m.getScoreT1());
            teamYScore_tv.setText("" + m.getScoreT2());

            if (m.isPlayed()) {
                matchNumber_tv.setBackgroundColor(Color.parseColor("#0FAE02"));
            }

            if (MyApplication.isOnline) {
                fetchMatchID();
            }

        return rod;
    }

    public void updateScore(String team, int newScore){
        if("t1".equals(team)){
            m.setScoreT1(newScore);
            teamXScore_tv.setText(""+newScore);
        }
        else {
            m.setScoreT2(newScore);
            teamYScore_tv.setText(""+newScore);
        }
    }


    @Override
    public void onClick(View v) {

        if (v==rank_b){
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContent, new RankList_frag())
                    .addToBackStack(null)
                    .commit();
        }

        else if(v==matchlist_b){
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContent, new RoundRobinMatchList_frag())
                    .addToBackStack(null)
                    .commit();
        }

        else if(v==next_b){
                    if (!m.isPlayed()) {
                        MyApplication.matchesPlayed++;
                    }
                    if (m.getScoreT1() > m.getScoreT2()) {
                        m.setWinner(m.getT1());
                        m.getT1().matchResult("won");
                        m.getT2().matchResult("lost");
                        Log.d("Debug", m.getT1().getTeamName() + " won");
                    } else if (m.getScoreT1() < m.getScoreT2()) {
                        m.setWinner(m.getT2());
                        m.getT1().matchResult("lost");
                        m.getT2().matchResult("won");
                        Log.d("Debug", m.getT2().getTeamName() + " won");
                    } else {
                        m.getT1().matchResult("draw");
                        m.getT2().matchResult("draw");
                        Log.d("Debug", "Is was a draw");
                    }

                    m.getT1().addToOverAllScore(Integer.parseInt(teamXScore_tv.getText().toString()));
                    m.getT2().addToOverAllScore(Integer.parseInt(teamYScore_tv.getText().toString()));

                    m.setPlayed(true);

                    for (Match mm : matches) {
                        if (!mm.isPlayed()) {
                            MyApplication.activeMatch = matches.indexOf(mm) + 1;
                            Log.d("Debug", "Next match: " + (matches.indexOf(mm) + 1));
                            break;
                        }
                    }

                    Log.d("Match", "Match " + activeMatchNumber + " done!");
                    Log.d("Match", "Score " + m.getScoreT1() + " - " + m.getScoreT2());
                    if (m.getWinner() != null) {
                        Log.d("Match", "Winner is " + m.getWinner().getTeamName());
                    } else Log.d("Match", "Match draw!");

                    if (MyApplication.matchesPlayed == matches.size()) {
                        MyApplication.isDone=true;
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContent, new TournamentWinner_frag())
                                .addToBackStack(null)
                                .commit();
                    } else {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContent, new ActiveMatchScore_frag())
                                .addToBackStack(null)
                                .commit();
                    }
                    if (MyApplication.isOnline) {
                        saveToParse();
                    }
        }

        else if(v==teamXPlus_b){
            int score = Integer.parseInt(teamXScore_tv.getText().toString());
            score++;
            updateScore("t1",score);
            if(MyApplication.isOnline){saveToParse();}

        }
        else if(v==teamXMinus_b){
            int score = Integer.parseInt(teamXScore_tv.getText().toString());
            if(score!=0){
                score--;
            }
            updateScore("t1",score);
            if(MyApplication.isOnline){saveToParse();}

        }
        else if(v==teamYPlus_b){
            int score = Integer.parseInt(teamYScore_tv.getText().toString());
            score++;
            updateScore("t2",score);
            if(MyApplication.isOnline){saveToParse();}

        }
        else if(v==teamYMinus_b){
            int score = Integer.parseInt(teamYScore_tv.getText().toString());
            if(score!=0){
                score--;
            }
            updateScore("t2",score);
            if(MyApplication.isOnline){saveToParse();}
        }
    }

    public void saveToParse(){
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... arg0) {
                Log.d("Parse", "Laver query på " + m.getMatchID());
                while (m.getMatchID() == null) ;
                ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Matches");
                query2.getInBackground(m.getMatchID(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject m_parse, ParseException e) {
                        if(e == null) {
                            m_parse.put("Team1Score", m.getScoreT1());

                            m_parse.put("Team2Score", m.getScoreT2());
                            if (m.isPlayed()) {
                                m_parse.put("isPlayed", true);
                            }
                            m_parse.saveInBackground();
                            Log.d("Parse","Saved match info to parse");
                        }
                        else{
                            Log.d("Parse","Failed to update match info to parse");
                        }
                    }
                });
                return null;
            }
        }.execute();
    }

    public void fetchMatchID() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... arg0) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Matches");
                Log.d("Parse","Fetching for match:"+m.getT1().getTeamName()+" vs "+ m.getT2().getTeamName()+ " with tournament id: "+MyApplication.tournamentID_parse);
                query.whereEqualTo("TournamentID", MyApplication.tournamentID_parse);
                query.whereContains("Team1Name", m.getT1().getTeamName());
                query.whereContains("Team2Name", m.getT2().getTeamName());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        if (e == null) {
                            if (parseObjects.size() > 1) {
                                Log.d("Parse", "Flere en 1 objekt fundet " + parseObjects.size());
                            } else {
                                m.setMatchID(parseObjects.get(0).getObjectId());
                                Log.d("Parse", "Match Query! Result: number of matches found: " + parseObjects.size() + " And first ID: " + parseObjects.get(0).getObjectId());
                            }
                        } else {
                            Log.d("Parse", "Update match went wrong");
                        }
                    }
                });
                return null;
            }
        }.execute();
    }

}
