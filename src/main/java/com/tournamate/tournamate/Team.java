package com.tournamate.tournamate;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by Christian on 16-04-2015.
 */

@ParseClassName("Team")
public class Team extends ParseObject{

    private String teamName = "";
    private int matchesWon, matchesLost, matchesDraw, matechesPlayed, overAllScore;
    private ArrayList<String> teamMembers = new ArrayList<>();
    private String matchID;
    private long teamID_sql;


    public Team(){

    }

    public void addTeamMember(String newTeamMember){
        teamMembers.add(newTeamMember);
    }

    public String getTeamName(){

        return teamName;
    }

    public void matchResult(String result){
        if ("won".equals(result)){
            matchesWon++;
            matechesPlayed++;
            Log.d("Debug", this.getTeamName() + " won a game!");
        }
        else if ("lost".equals(result)){
            matchesLost++;
            matechesPlayed++;
            Log.d("Debug", this.getTeamName() + " lost a game!");
        }
        else {
            matchesDraw++;
            matechesPlayed++;
            Log.d("Debug", this.getTeamName() + " played draw!");
        }
    }

    public void addToOverAllScore(int points){
        this.overAllScore += points;
    }

    public int getMatchesWon() {
        return matchesWon;
    }

    public void setMatchesWon(int matchesWon){
        this.matchesWon = matchesWon;
    }

    public int getOverAllScore(){
        return overAllScore;
    }

    public void setOverAllScore(int overAllScore){
        this.overAllScore= overAllScore;
    }

    public int getMatchesLost() {
        return matchesLost;
    }

    public void setMatchesLost(int matchesLost) {
        this.matchesLost = matchesLost;
    }

    public int getMatchesDraw() {
        return matchesDraw;
    }

    public void setMatchesDraw(int matchesDraw) {
        this.matchesDraw = matchesDraw;
    }

    public int getMatechesPlayed() {
        return matechesPlayed;
    }

    public void setMatechesPlayed(int matechesPlayed) {
        this.matechesPlayed = matechesPlayed;
    }

    public void setMatchID(String value){this.matchID=value;}
    public String getMatchID(){return matchID;}

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void makeTeamName(){
        teamName = "Dummy";
        if(teamMembers.size()>0){
            teamName = "";
            for(String name : teamMembers) {
                teamName += name;
                teamName += " & ";
            }
            teamName = teamName.substring(0,teamName.length()-3);
        }
    }

    public long getTeamID_sql() {
        return teamID_sql;
    }

    public void setTeamID_sql(long teamID_sql) {
        this.teamID_sql = teamID_sql;
    }
}
