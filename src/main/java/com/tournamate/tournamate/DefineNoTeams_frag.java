package com.tournamate.tournamate;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;


public class DefineNoTeams_frag extends Fragment {

    View rod;
    ListView lv;
    ArrayList<String> selectedPlayers;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

        rod = i.inflate(R.layout.set_no_teams, container, false);

        lv = (ListView) rod.findViewById(R.id.listViewNoTeams);

        selectedPlayers = new ArrayList<>(MyApplication.selectedPlayerSet);
        MyApplication.teams.clear();

        makeTeams();

        ArrayList<String> teamNames = new ArrayList<>();

        for (Team t : MyApplication.teams){
            String teamName = t.getTeamName();
            teamNames.add(teamName);
        }
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.team_tv,R.id.textViewTeamName,teamNames) {
            @Override
            public View getView(int position, View cachedView, ViewGroup parent) {
                View view = super.getView(position, cachedView, parent);

                TextView teamNumber = (TextView) view.findViewById(R.id.textViewTeamNumber);
                teamNumber.setText("" + (position+1));

                return view;
            }
        };
        lv.setAdapter(adapter);

        return rod;
    }

    private void makeTeams(){

        for (int i = 1; i<= selectedPlayers.size();i++){
            Team t = new Team();
            t.addTeamMember(selectedPlayers.get(i-1));
            t.makeTeamName();
            MyApplication.teams.add(t);
        }
    }

}
