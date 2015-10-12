package com.christan.tournamate;

import android.util.Log;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;

/**
 * Created by Christian on 16-04-2015.
 */
public class RoundRobin_logic {

        private int numberOfMatches;
        private ArrayList<Team> teams;
        private ArrayList<Match> matches = new ArrayList<>();

        public RoundRobin_logic(){
            this.teams = MyApplication.teams;
            this.numberOfMatches = 0;


        }

        public void createMatches() {

            if(teams.size()%2==1){
                Team dummy = new Team();
                dummy.makeTeamName();
                teams.add(dummy);
                Log.d("Debug","Dummy team added");
            }
            numberOfMatches = (teams.size()/2)*(teams.size()-1);
            int pivot = teams.size()/2;
            int matchIterator = 0;
            int matchesMade = 0;
            Log.d("Debug","Number of matches: "+numberOfMatches);

            while (matchIterator<numberOfMatches){
                Log.d("Debug","Matches made: "+matchesMade);
                ArrayList<Team> tList1 = new ArrayList<>();
                for (int i = 0; i<pivot;i++){
                    tList1.add(teams.get(i));
                    Log.d("Debug","Team added to list 1: "+teams.get(i).getTeamName());
                }
                Log.d("Debug","Size of list 1: "+tList1.size());
                ArrayList<Team> tList2 = new ArrayList<>();
                for (int j = teams.size()-1;j>=pivot;j--){
                    tList2.add(teams.get(j));
                    Log.d("Debug","Team added to list 2: "+teams.get(j).getTeamName());
                }
                Log.d("Debug","Size of list 2: "+tList2.size());

                for (int k = 0; k < pivot;k++){
                    Team t1 = tList1.get(k);
                    Team t2 = tList2.get(k);
                    Log.d("Debug","Next match t1: "+t1.getTeamName());
                    Log.d("Debug","Next match t2: "+t2.getTeamName());


                    if ("Dummy".equals(t1.getTeamName()) || "Dummy".equals(t2.getTeamName())){
                        // do nothing
                        matchIterator++;
                        Log.d("Debug","Dummy team detected. Not adding match");
                    }
                    else {

                        final Match m = new Match();
                        m.setT1(t1);
                        m.setT2(t2);
                        m.setMatchNumber(matchesMade + 1);
                        m.pinInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                m.setMatchID(m.getObjectId());
                            }
                        });
                        matches.add(m);

                        matchesMade++;
                        matchIterator++;
                        Log.d("Debug","Match added!");
                        Log.d("Debug","Match info: " + m.getMatchNumber() + " " + m.getT1().getTeamName() + " vs " + m.getT2().getTeamName());
                    }

                }

                rotateTeams();
                Log.d("Debug","Rotating: ");
            }

            MyApplication.matchList = matches;
            Log.d("Debug", "############ DONE ##############");
        }

        public void rotateTeams(){
            ArrayList<Team> tempList = new ArrayList<>();
            tempList.add(teams.get(0));
            Log.d("Debug", "Ratation 0: " + tempList.get(0).getTeamName());
            for (int i = 2;i<teams.size();i++){
                tempList.add(teams.get(i));
                Log.d("Debug","Ratation " + (i-1)+" : "+tempList.get(i-1).getTeamName());
            }
            tempList.add(teams.get(1));
            Log.d("Debug","Ratation " +(tempList.size()-1)+" : "+tempList.get(0).getTeamName());
            teams = tempList;
        }


}
