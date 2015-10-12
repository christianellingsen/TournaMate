package com.christan.tournamate;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Christian on 19-04-2015.
 */
public class RoundRobinMatchList_frag extends Fragment implements AdapterView.OnItemClickListener {

    ListView lv;
    ArrayList<Match> matches;
    ArrayList<String> matchesStringList;
    View rod;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

        rod = i.inflate(R.layout.test_list, container, false);
        matches = MyApplication.matchList;
        matchesStringList = new ArrayList<>();

        lv = (ListView) rod.findViewById(R.id.listView);

        for (Match m : matches){
            Team t1 = m.getT1();
            Team t2 = m.getT2();

            matchesStringList.add(t1.getTeamName() + " VS " + t2.getTeamName());
        }
        Log.d("Debug","Size of matches " +matches.size());
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.match_list_with_score,R.id.textViewMatchList_team,matchesStringList) {
            @Override
            public View getView(int position, View cachedView, ViewGroup parent) {
                View view = super.getView(position, cachedView, parent);
                Log.d("Debug","Matchlist element: match: "+matchesStringList.get(position)+" number: "+matches.get(position).getMatchNumber());

                TextView matchNumber = (TextView) view.findViewById(R.id.textViewMatchList_number);
                matchNumber.setText(""+matches.get(position).getMatchNumber());

                TextView score = (TextView) view.findViewById(R.id.textViewMatchList_score);
                score.setText(matches.get(position).getScoreT1() + " - " + matches.get(position).getScoreT2());

                if (matches.get(position).isPlayed()){
                    score.setTextColor(Color.parseColor("#0FAE02"));
                }

                return view;
            }
        };
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);

        return rod;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        MyApplication.activeMatch = position+1;
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentContent, new ActiveMatchScore_frag())
                .addToBackStack(null)
                .commit();
    }
}
