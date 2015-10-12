package com.christan.tournamate;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Christian on 19-04-2015.
 */
public class RankList_frag extends Fragment {

    ListView lv;
    ArrayList<Team> teams;
    ArrayList<String> teamsStringList;
    View rod;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

        rod = i.inflate(R.layout.rank_list, container, false);
        teams = MyApplication.teams;
        teamsStringList = new ArrayList<>();

        MyApplication.rankTeams();

        lv = (ListView) rod.findViewById(R.id.listViewRankTeams);

        for(Team t : teams){
            if(t.getTeamName().equals("Dummy")) {

            }
            else {
                teamsStringList.add(t.getTeamName());
            }
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

        return rod;
    }


}
