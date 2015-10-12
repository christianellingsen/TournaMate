package com.tournamate.tournamate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;


/**
 * Created by Christian on 19-05-2015.
 */
public class SpectateTournament_akt extends Activity  {

    String tournamentID = null;
    String tournamentName = null;
    String winnerName = null;
    CustomParseQueryAdapter mAdapter;
    TextView name_tv, winner_tv;
    ListView lv;

    Handler handler = new Handler();

    public SpectateTournament_akt(){
        tournamentID = MyApplication.tournamentID_parse;
        tournamentName = MyApplication.tournamentName;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spectate_match);

        name_tv = (TextView) findViewById(R.id.textViewSpectate_TournamentName);
        winner_tv = (TextView) findViewById(R.id.textViewSpectate_winnerName);

        lv = (ListView) findViewById(R.id.listViewSpectate_lv);

        name_tv.setText(tournamentName);

        if (MyApplication.isDone){
            name_tv.setBackgroundColor(Color.parseColor("#0FAE02"));
            findWinner();
            winner_tv.setText(winnerName);
        }

        final ProgressDialog progress;
        progress = ProgressDialog.show(this, "Collecting tournament data", "", true);
        progress.setCancelable(false);
        progress.show();

        mAdapter = new CustomParseQueryAdapter(this);
        lv.setAdapter(mAdapter);
        mAdapter.loadObjects();
        mAdapter.setAutoload(true);
        progress.dismiss();

        handler.postDelayed(autoRefresh,5000);

    }


    public class CustomParseQueryAdapter extends ParseQueryAdapter<Match> {
        public CustomParseQueryAdapter(Context context){
            super(context, new QueryFactory<Match>() {
                @Override
                public ParseQuery<Match> create() {
                    ParseQuery query = new ParseQuery("Matches");
                    query.whereContains("TournamentID", tournamentID);
                    query.addAscendingOrder("MatchNumber");
                    return query;
                }
            });
        }

        @Override
        public View getItemView(Match m, View v, ViewGroup parent){
            if (v == null){
                v = View.inflate(getContext(), R.layout.match_list_with_score, null);
            }
            super.getItemView(m, v, parent);

            TextView m_name = (TextView)v.findViewById(R.id.textViewMatchList_team);
            m_name.setText(m.getT1NameParse() + " VS " +  m.getT2NameParse());

            TextView m_score = (TextView)v.findViewById(R.id.textViewMatchList_score);
            m_score.setText(m.getT1ScoreParse() + " - " + m.getT2ScoreParse());
            if(m.getIsPlayedParse()){
                m_score.setTextColor(Color.parseColor("#0FAE02"));
            }

            TextView m_number = (TextView) v.findViewById(R.id.textViewMatchList_number);
            m_number.setText(""+m.getMatchNumberParse());

            return v;
        }
    }

    public final Runnable autoRefresh = new Runnable() {

        @Override
        public void run() {
            if (MyApplication.isDone){
                name_tv.setBackgroundColor(Color.parseColor("#0FAE02"));
                findWinner();
                winner_tv.setText(winnerName);
            }
            lv.setAdapter(mAdapter);
            handler.postDelayed(autoRefresh,5000);
        }
    };

    public void findWinner(){
        ParseQuery query = new ParseQuery("Tournaments");
        query.getInBackground(tournamentID,new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject t, ParseException e) {
                if(e==null){
                    winnerName = t.getString("Winner");
                }
            }
        });
    }
}
