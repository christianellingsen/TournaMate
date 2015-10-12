package com.tournamate.tournamate;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Christian on 06-04-2015.
 */
public class DefineRandomTeam_frag extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private View rod;

    private Button makeTeams_b;
    private SeekBar seekBar;
    private ListView lv;
    private TextView teamSize_tv;

    ArrayList<String> selectedPlayerNames;

    int maxTeamSize;
    int minTeamSize;
    int numberOfTeams;
    int reguestedTeamSize;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState){

        rod = i.inflate(R.layout.set_random_teams, container, false);

        makeTeams_b = (Button) rod.findViewById(R.id.buttonMakeTeams);
        seekBar = (SeekBar) rod.findViewById(R.id.seekBar);
        lv = (ListView) rod.findViewById(R.id.listViewRandomTeams);
        teamSize_tv = (TextView) rod.findViewById(R.id.textViewTeamSize);

        selectedPlayerNames = new ArrayList<>(MyApplication.selectedPlayerSet);

        makeTeams_b.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);


        maxTeamSize = MyApplication.selectedPlayerSet.size()-1;
        minTeamSize = 2;

        seekBar.setMax(maxTeamSize-minTeamSize);
        teamSize_tv.setText("" + minTeamSize);
        makeTeams();
        updateList();

        return rod;
    }



    @Override
    public void onClick(View v) {
        if (v==makeTeams_b){
            makeTeams();
            updateList();
        }
    }

    public void updateList(){
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
    }

    public void makeTeams() {
        //Log.d("MakeTeams","############ START MAKETEAMS ###########");
        int remainder = MyApplication.selectedPlayerSet.size() % reguestedTeamSize;
        numberOfTeams = MyApplication.selectedPlayerSet.size() / reguestedTeamSize;
        if (remainder != 0) {
            numberOfTeams += 1;
        }
        selectedPlayerNames = new ArrayList<>(MyApplication.selectedPlayerSet);
        Collections.shuffle(selectedPlayerNames);
        MyApplication.teams.clear();

        //Log.d("MakeTeams", "Selected players: " + selectedPlayerNames.toString());
        //Log.d("MakeTeams","Number of teams: "+numberOfTeams);

        int numberOfMembersAdded = 0;
        while (MyApplication.teams.size()<numberOfTeams){
            //Log.d("MakeTeams","Making a team");

            Team t = new Team();
            if (selectedPlayerNames.size()-numberOfMembersAdded < reguestedTeamSize){
                for(int i = numberOfMembersAdded; i<selectedPlayerNames.size();i++){
                    t.addTeamMember(selectedPlayerNames.get(numberOfMembersAdded));
                    numberOfMembersAdded++;
                    //Log.d("MakeTeams","Making uneven team");
                }
            }
            else {
                for (int i = 0; i < reguestedTeamSize; i++) {
                    t.addTeamMember(selectedPlayerNames.get(numberOfMembersAdded));
                    numberOfMembersAdded++;
                    //Log.d("MakeTeams","Making a normal size team");
                }
            }
            t.makeTeamName();
            MyApplication.teams.add(t);

            //Log.d("MakeTeams","Team: "+t.getTeamName());
            //Log.d("MakeTeams","team counter: "+(teamNumberCounter-1));
            //Log.d("MakeTeams","team members added : "+(numberOfMembersAdded));

        }
        //Log.d("MakeTeams","########## DONE MAKING TEAMS ###############");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        reguestedTeamSize = minTeamSize + progress;
        teamSize_tv.setText(""+reguestedTeamSize);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
