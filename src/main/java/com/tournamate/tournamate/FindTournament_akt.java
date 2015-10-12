package com.tournamate.tournamate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;

/**
 * Created by Christian on 26-04-2015.
 */
public class FindTournament_akt extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{

    EditText search_et;
    Button search_b;
    ListView lv;
    final ArrayList<Tournament> result = new ArrayList();
    private CustomParseQueryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_tournament);

        search_et = (EditText) findViewById(R.id.editTextSearch);
        search_b = (Button) findViewById(R.id.buttonSearch);
        lv = (ListView) findViewById(R.id.listViewSearchResult);

        search_et.setOnClickListener(this);
        search_b.setOnClickListener(this);
        lv.setOnItemClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v==search_b){
            result.clear();
            final ProgressDialog progress;
            progress = ProgressDialog.show(this, "Looking for tournaments", "", true);
            progress.setCancelable(false);
            progress.show();

            mAdapter = new CustomParseQueryAdapter(this);
            lv.setAdapter(mAdapter);
            mAdapter.loadObjects();
            mAdapter.setAutoload(true);
            progress.dismiss();
        }
    }

    public class CustomParseQueryAdapter extends ParseQueryAdapter<Tournament>{
        public CustomParseQueryAdapter(Context context){
            super(context, new QueryFactory<Tournament>() {
                @Override
                public ParseQuery<Tournament> create() {
                    ParseQuery query = new ParseQuery("Tournaments");
                    query.whereContains("Name", search_et.getText().toString());
                    return query;
                }
            });
        }

        @Override
        public View getItemView(Tournament t, View v, ViewGroup parent){
            if (v == null){
                v = View.inflate(getContext(), R.layout.tournament_list_tv, null);
            }

            super.getItemView(t, v, parent);

            TextView tournamentName = (TextView)v.findViewById(R.id.textViewTournamentName);
            tournamentName.setText(t.getNameParse());

            TextView tournamentDate = (TextView)v.findViewById(R.id.textViewTournamentCreatedAt);
            tournamentDate.setText(t.getDateParse());

            if (t.getIsDoneParse()){
                tournamentName.setText(tournamentName.getText().toString()+ "\n (Completed)");
                tournamentName.setTextColor(Color.parseColor("#0FAE02"));
                tournamentDate.setTextColor(Color.parseColor("#0FAE02"));
            }

            Tournament temp_t = new Tournament();
            temp_t.setObjectID(t.getObjectIdParse());
            temp_t.setName(t.getNameParse());
            temp_t.setIsDone(t.getIsDoneParse());
            result.add(temp_t);

            return v;
        }
    }


    public void onItemClick(AdapterView<?> list, View v, int position, long id){
        Tournament t = result.get(position);
        //Toast.makeText(this, "Choosen tournament: " + t.getName() + " and pos: " + position, Toast.LENGTH_SHORT).show();
        MyApplication.tournamentID_parse = t.getObjectIdLocal();
        MyApplication.tournamentName = t.getName();
        MyApplication.isDone = t.getIsDone();

        Intent i = new Intent(this, SpectateTournament_akt.class);
        startActivity(i);

    }
}
