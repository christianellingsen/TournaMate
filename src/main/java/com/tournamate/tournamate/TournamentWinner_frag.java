package com.christan.tournamate;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Christian on 20-04-2015.
 */
public class TournamentWinner_frag extends Fragment implements View.OnClickListener {

    private TextView winnerName_tv,header;
    private Button rank_b, matchList_b, end_b;
    private Team winner;
    ListView lv;
    ArrayList<Team> teams;
    ArrayList<String> teamsStringList;
    ArrayList<Match> matches;
    ArrayList<String> matchesStringList;
    View rod;
    DBAdapter db_adapter = new DBAdapter(getActivity());

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

        rod = i.inflate(R.layout.tournament_winner, container, false);
        matchesStringList = new ArrayList<>();
        teams = MyApplication.teams;
        matches = MyApplication.matchList;
        teamsStringList = new ArrayList<>();

        header = (TextView) getActivity().findViewById(R.id.textViewNewTournamentHeader);
        winnerName_tv = (TextView) rod.findViewById(R.id.textViewWinner_teamName);
        rank_b = (Button) rod.findViewById(R.id.buttonWinner_rank);
        matchList_b = (Button) rod.findViewById(R.id.buttonWinner_matchList);
        end_b = (Button) rod.findViewById(R.id.buttonWinner_end);

        header.setText(getString(R.string.tournamentWinner_header));

        rank_b.setOnClickListener(this);
        matchList_b.setOnClickListener(this);
        end_b.setOnClickListener(this);

        MyApplication.rankTeams();

        winner = MyApplication.teams.get(0);

        winnerName_tv.setText(winner.getTeamName());

        lv = (ListView) rod.findViewById(R.id.listViewWinner);

        for(Team t : teams){
            if(t.getTeamName().equals("Dummy")) {

            }
            else {
                teamsStringList.add(t.getTeamName());
                Log.d("Debug", "" + t.getTeamName() + " has played " + t.getMatechesPlayed() + " matches");
            }
        }
        for (Match m : matches){
            Team t1 = m.getT1();
            Team t2 = m.getT2();

            matchesStringList.add(t1.getTeamName() + " VS " + t2.getTeamName());
        }

        ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.rank_tv,R.id.textViewRankTV_name,teamsStringList) {
            @Override
            public View getView(int position, View cachedView, ViewGroup parent) {
                View view = super.getView(position, cachedView, parent);

                TextView teamRank = (TextView) view.findViewById(R.id.textViewRankTV_Rank);
                teamRank.setText("" + (position+1));

                TextView played = (TextView) view.findViewById(R.id.textViewRankTV_Played);
                played.setText(""+teams.get(position).getMatechesPlayed());

                TextView won = (TextView) view.findViewById(R.id.textViewRankTV_Won);
                won.setText(""+teams.get(position).getMatchesWon());

                TextView lost = (TextView) view.findViewById(R.id.textViewRankTV_Lost);
                lost.setText(""+teams.get(position).getMatchesLost());

                TextView draw = (TextView) view.findViewById(R.id.textViewRankTV_Draw);
                draw.setText(""+teams.get(position).getMatchesDraw());

                TextView points = (TextView) view.findViewById(R.id.textViewRankTV_Points);
                points.setText(""+teams.get(position).getOverAllScore());

                return view;
            }
        };
        lv.setAdapter(adapter);

        // Write to parse
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tournaments");
        // Retrieve the object by id
        query.getInBackground(MyApplication.tournamentID_parse, new GetCallback<ParseObject>() {
            public void done(ParseObject t, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    t.put("Winner",winner.getTeamName());
                    t.put("isDone",true);
                    t.saveInBackground();
                }
            }
        });
        updateOnDevice();
        return rod;

    }

    @Override
    public void onClick(View v){
        if (v==rank_b){
            ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.rank_tv,R.id.textViewRankTV_name,teamsStringList) {
                @Override
                public View getView(int position, View cachedView, ViewGroup parent) {
                    View view = super.getView(position, cachedView, parent);

                    TextView teamRank = (TextView) view.findViewById(R.id.textViewRankTV_Rank);
                    teamRank.setText("" + (position+1));

                    TextView played = (TextView) view.findViewById(R.id.textViewRankTV_Played);
                    played.setText(""+teams.get(position).getMatechesPlayed());

                    TextView won = (TextView) view.findViewById(R.id.textViewRankTV_Won);
                    won.setText(""+teams.get(position).getMatchesWon());

                    TextView lost = (TextView) view.findViewById(R.id.textViewRankTV_Lost);
                    lost.setText(""+teams.get(position).getMatchesLost());

                    TextView draw = (TextView) view.findViewById(R.id.textViewRankTV_Draw);
                    draw.setText(""+teams.get(position).getMatchesDraw());

                    TextView points = (TextView) view.findViewById(R.id.textViewRankTV_Points);
                    points.setText(""+teams.get(position).getOverAllScore());

                    return view;
                }
            };
            lv.setAdapter(adapter);

        }
        else if (v==matchList_b){
            ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.match_list_with_score,R.id.textViewMatchList_team,matchesStringList) {
                @Override
                public View getView(int position, View cachedView, ViewGroup parent) {
                    View view = super.getView(position, cachedView, parent);

                    TextView teamNumber = (TextView) view.findViewById(R.id.textViewMatchList_number);
                    teamNumber.setText("" + (position + 1));

                    TextView score = (TextView) view.findViewById(R.id.textViewMatchList_score);
                    score.setText(matches.get(position).getScoreT1() + " - " + matches.get(position).getScoreT2());

                    if (matches.get(position).isPlayed()){
                        score.setTextColor(Color.parseColor("#0FAE02"));
                    }

                    return view;
                }
            };
            lv.setAdapter(adapter);
        }
        else if (v==end_b){

            Intent i = new Intent(getActivity(),MainMenu_akt.class);
            startActivity(i);
        }
    }
    public void updateOnDevice(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date  = dateFormat.format(new Date());
        openDB();
        db_adapter.updateRowTournament(MyApplication.tournamentID_sql,MyApplication.tournamentName,MyApplication.type,MyApplication.activeMatch,MyApplication.matchesPlayed, String.valueOf(MyApplication.isDone).toString() ,String.valueOf(MyApplication.isOnline),MyApplication.tournamentID_parse ,date);
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
        db_adapter = new DBAdapter(getActivity());
        db_adapter.open();
    }

    public void closeDB(){
        db_adapter.close();
    }

}
