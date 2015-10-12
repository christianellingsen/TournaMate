package com.christan.tournamate;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.ParseException;

/**
 * Created by Christian on 06-04-2015.
 */
public class MakeTeams_frag extends Fragment implements View.OnClickListener{


    private ToggleButton noTeams_tb, random_tb, manually_tb;
    private TextView header;
    private Button done_b;
    private View rod;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

        rod = i.inflate(R.layout.make_teams, container, false);
        header = (TextView) getActivity().findViewById(R.id.textViewNewTournamentHeader);
        header.setText(getString(R.string.maketeams_header));

        MyApplication.teams.clear();

        noTeams_tb = (ToggleButton) rod.findViewById(R.id.toggleButtonNoTeams);
        random_tb = (ToggleButton) rod.findViewById(R.id.toggleButtonRandom);
        manually_tb = (ToggleButton) rod.findViewById(R.id.toggleButtonManually);
        done_b = (Button) rod.findViewById(R.id.buttonDoneTeams);

        noTeams_tb.setOnClickListener(this);
        random_tb.setOnClickListener(this);
        manually_tb.setOnClickListener(this);
        done_b.setOnClickListener(this);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentContentTeams,new DefineNoTeams_frag())
                .addToBackStack(null)
                .commit();

        return rod;
    }

    @Override
    public void onClick(View v) {
       if (v==noTeams_tb){
           random_tb.setChecked(false);
           manually_tb.setChecked(false);

           getFragmentManager().beginTransaction()
                   .replace(R.id.fragmentContentTeams,new DefineNoTeams_frag())
                   .addToBackStack(null)
                   .commit();
       }
       else if (v==random_tb){
           if(MyApplication.selectedPlayerSet.size()<3){
               Toast.makeText(getActivity(), getString(R.string.maketeams_3players),Toast.LENGTH_SHORT).show();
           }
           else {
               noTeams_tb.setChecked(false);
               manually_tb.setChecked(false);

               getFragmentManager().beginTransaction()
                       .replace(R.id.fragmentContentTeams, new DefineRandomTeam_frag())
                       .addToBackStack(null)
                       .commit();
           }
       }
       else if (v==manually_tb){
            noTeams_tb.setChecked(false);
            random_tb.setChecked(false);
            Toast.makeText(getActivity(),getString(R.string.mainMenu_commingSoonToast),Toast.LENGTH_SHORT).show();
       }
       else if (v==done_b){
           MyApplication.type = "Round Robin";
           getFragmentManager().beginTransaction()
                   .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)
                   .replace(R.id.fragmentContent, new TournamentReady_frag())
                   .addToBackStack(null)
                   .commit();
       }
    }
}
